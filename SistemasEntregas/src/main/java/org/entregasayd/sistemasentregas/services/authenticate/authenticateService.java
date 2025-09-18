package org.entregasayd.sistemasentregas.services.authenticate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.repositories.UsuarioRepository;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.authenticate.*;
import org.entregasayd.sistemasentregas.repositories.PersonaRepository;
import org.entregasayd.sistemasentregas.repositories.DireccionRepository;
import org.entregasayd.sistemasentregas.repositories.RolRepository;
import org.entregasayd.sistemasentregas.repositories.TokenAutenticacionRepository;
import org.entregasayd.sistemasentregas.services.email.EmailService;
import org.entregasayd.sistemasentregas.utils.Encriptation;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class authenticateService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private TokenAutenticacionRepository tokenAutenticacionRepository;
    
    @Autowired
    private EmailService emailService;

    private Encriptation passwordEncoder = new Encriptation();

    @Transactional
    public Usuario registerUser(RegisterUserDTO userDTO) {
        // Crear y guardar la dirección
        Direccion direccion = new Direccion();
        direccion.setTipoDireccion(Direccion.TipoDireccion.valueOf(userDTO.getTipoDireccion()));
        direccion.setMunicipio(userDTO.getMunicipio());
        direccion.setDepartamento(userDTO.getDepartamento());
        direccion.setPais(userDTO.getPais());
        direccion.setCodigoPostal(userDTO.getCodigoPostal());
        direccion.setReferencias(userDTO.getReferencias());
        direccionRepository.save(direccion);

        // Crear y guardar la persona
        Persona persona = new Persona();
        persona.setNombre(userDTO.getNombre());
        persona.setApellido(userDTO.getApellido());
        persona.setFechaNacimiento(LocalDate.parse(userDTO.getFechaNacimiento()));
        persona.setDpi(userDTO.getDpi());
        persona.setCorreo(userDTO.getCorreo());
        persona.setTelefono(userDTO.getTelefono());
        persona.setDireccion(direccion);
        personaRepository.save(persona);

        // Crear y guardar el usuario
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        //Buscamos por el nombre del rol, si no existe lanza una excepción
        //Nombre rol = "CLIENTE"
        Rol rol = rolRepository.findByNombre("CLIENTE");
        usuario.setRol(rol);
        usuario.setNombreUsuario(userDTO.getNombreUsuario());
        usuario.setContraseniaHash(passwordEncoder.encrypt(userDTO.getContraseniaHash()));
        usuario.setTwoFactorEnable(userDTO.isTwoFactorEnable());
        usuario.setEstado(Usuario.Estado.valueOf(userDTO.getEstado()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequest) throws Exception {
        // Buscar el usuario por nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(loginRequest.getNombreUsuario());
        if(usuario == null){
            throw new Exception("Nombre de usuario o contraseña incorrectos");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(loginRequest.getContrasenia(), usuario.getContraseniaHash())) {
            // Incrementar intentos fallidos
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            
            // Si los intentos fallidos llegan a 5, suspender el usuario
            if (usuario.getIntentosFallidos() >= 5) {
                usuario.setEstado(Usuario.Estado.SUSPENDIDO);
            }
            
            usuarioRepository.save(usuario);
            throw new Exception("Nombre de usuario o contraseña incorrectos");
        }
        
        // Verificar si el usuario está activo
        if (usuario.getEstado() != Usuario.Estado.ACTIVO) {
            throw new Exception("El usuario no está activo. Contacte al administrador.");
        }
        
        // Manejar autenticación de dos factores
        if (usuario.isTwoFactorEnable()) {
            // Obtener información de la persona
            Persona persona = usuario.getPersona();
            
            // Generar código de verificación (6 dígitos)
            String codigoVerificacion = String.format("%06d", new Random().nextInt(1000000));
            
            // Crear un token aleatorio de 10 caracteres
            String token = generateRandomToken(10);
            
            // Crear token en la base de datos
            TokenAutenticacion tokenAuth = new TokenAutenticacion();
            tokenAuth.setIdUsuario(usuario.getIdUsuario());
            tokenAuth.setTokenHash(token);
            tokenAuth.setTipoToken("2FA"); // Aseguramos que sea consistente con la búsqueda
            tokenAuth.setFechaExpiracion(LocalDateTime.now().plusMinutes(15)); // 15 minutos
            tokenAuth.setCodigoVerificacion(codigoVerificacion);
            tokenAuth.setEstado("ACTIVO"); // Aseguramos que se establezca explícitamente
            TokenAutenticacion savedToken = tokenAutenticacionRepository.save(tokenAuth);
            
            log.info("Token guardado: ID={}, Hash={}, Tipo={}, Estado={}", 
                     savedToken.getIdToken(), savedToken.getTokenHash(), 
                     savedToken.getTipoToken(), savedToken.getEstado());
            
            // Enviar el código de verificación por correo electrónico
            try {
                emailService.sendVerificationCode(persona.getCorreo(), codigoVerificacion);
            } catch (MessagingException e) {
                throw new Exception("Error al enviar el correo electrónico: " + e.getMessage());
            }
            
            return LoginResponseDTO.builder()
                    .autenticacion(new LoginResponseDTO.Autenticacion(true))
                    .credenciales(LoginResponseDTO.Credenciales.builder()
                            .mensaje("Código de verificación enviado al correo")
                            .idUsuario(usuario.getIdUsuario())
                            .token(token)
                            .build())
                    .build();
        } else {
            // No se requiere autenticación de dos factores
            // Resetear intentos fallidos y actualizar última fecha de acceso
            usuario.setIntentosFallidos(0);
            usuario.setUltimaFechaAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
            
            // Obtener información del rol
            Rol rol = usuario.getRol();
            
            return LoginResponseDTO.builder()
                    .autenticacion(new LoginResponseDTO.Autenticacion(false))
                    .credenciales(LoginResponseDTO.Credenciales.builder()
                            .mensaje("Inicio de sesión exitoso")
                            .idUsuario(usuario.getIdUsuario())
                            .rol(rol.getIdRol())
                            .nombreRol(rol.getNombre())
                            .nombreUsuario(usuario.getNombreUsuario())
                            .build())
                    .build();
        }
    }
    
    private String generateRandomToken(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return token.toString();
    }

    @Transactional
    public Map<String, Object> verify2FACode(Verify2FADTO verify2FADTO) throws Exception {
        String token = verify2FADTO.getToken();
        String codigoVerificacion = verify2FADTO.getCodigoVerificacion();
        
        if (token == null || codigoVerificacion == null) {
            throw new Exception("Token y código de verificación son requeridos");
        }
        
        log.info("Token recibido: {}, Código de verificación recibido: {}", token, codigoVerificacion);
        
        // Buscar todos los tokens activos
        /*List<TokenAutenticacion> allTokens = tokenAutenticacionRepository.findAll();
        log.info("Total de tokens en la base de datos: {}", allTokens.size());
        for (TokenAutenticacion t : allTokens) {
            log.info("Token en DB: {} - Tipo: {} - Estado: {}", t.getTokenHash(), t.getTipoToken(), t.getEstado());
        }*/
        
        // Buscar el token específico
        Optional<TokenAutenticacion> tokenOptional = tokenAutenticacionRepository.findByTokenHash(token);
        log.info("Token encontrado por hash: {}", tokenOptional.isPresent());
        
        if (tokenOptional.isPresent()) {
            TokenAutenticacion foundToken = tokenOptional.get();
            log.info("Token tipo: {}, Estado: {}, Código: {}", foundToken.getTipoToken(), foundToken.getEstado(), foundToken.getCodigoVerificacion());
        }
        
        // Buscar el token con los dos criterios
        Optional<TokenAutenticacion> tokenWithBoth = tokenAutenticacionRepository.findByTokenHashAndTipoToken(token, "2FA");
        log.info("Token encontrado con ambos criterios: {}", tokenWithBoth.isPresent());
        
        if (tokenOptional.isEmpty()) {
            throw new Exception("Token no encontrado");
        }
        
        TokenAutenticacion tokenRecord = tokenOptional.get();
        
        // Verificamos el tipo del token
        if (!tokenRecord.getTipoToken().equals("2FA")) {
            log.info("Tipo de token incorrecto. Esperado: 2FA, Actual: {}", tokenRecord.getTipoToken());
            throw new Exception("Tipo de token incorrecto");
        }
        
        // Verificar el estado del token
        if (!"ACTIVO".equals(tokenRecord.getEstado())) {
            log.info("Estado de token incorrecto. Esperado: ACTIVO, Actual: {}", tokenRecord.getEstado());
            throw new Exception("Token no está activo");
        }
        
        // Verificar si el token ha expirado
        if (LocalDateTime.now().isAfter(tokenRecord.getFechaExpiracion())) {
            tokenRecord.setEstado("EXPIRADO");
            tokenAutenticacionRepository.save(tokenRecord);
            throw new Exception("El token ha expirado");
        }
        
        // Verificar el código de verificación
        if (!tokenRecord.getCodigoVerificacion().equals(codigoVerificacion)) {
            throw new Exception("Código de verificación incorrecto");
        }
        
        // Marcar el token como 'USADO'
        tokenRecord.setEstado("USADO");
        tokenAutenticacionRepository.save(tokenRecord);
        
        // Obtener el usuario asociado al token
        Long idUsuario = tokenRecord.getIdUsuario();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOptional.get();
        Rol rol = usuario.getRol();
        
        // Actualizar información del usuario
        usuario.setUltimaFechaAcceso(LocalDateTime.now());
        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Código de verificación autenticado correctamente");
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("rol", rol.getIdRol());
        response.put("nombreRol", rol.getNombre());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        
        return response;
    }

    @Transactional
    public Map<String, Object> recoverPassword(RecoverPasswordDTO recoverPasswordDTO) throws Exception {
        String correo = recoverPasswordDTO.getCorreo();
        
        if (correo == null || correo.isEmpty()) {
            throw new Exception("El correo es requerido");
        }
        
        // Buscar la persona por correo
        Persona persona = personaRepository.findByCorreo(correo);
        if (persona == null) {
            throw new Exception("Correo no registrado");
        }
        

        // Buscar el usuario asociado a la persona
        Optional<Usuario> usuarioOptional = usuarioRepository.findByPersona(persona);
        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOptional.get();
        
        // Generar código de verificación (6 dígitos)
        String codigoVerificacion = String.format("%06d", new Random().nextInt(1000000));
        
        // Crear un token aleatorio
        String token = generateRandomToken(20);
        
        // Crear token en la base de datos
        TokenAutenticacion tokenAutenticacion = new TokenAutenticacion();
        tokenAutenticacion.setIdUsuario(usuario.getIdUsuario());
        tokenAutenticacion.setTokenHash(token);
        tokenAutenticacion.setTipoToken("RESET_PASSWORD");
        tokenAutenticacion.setCodigoVerificacion(codigoVerificacion);
        tokenAutenticacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15)); // 15 minutos
        tokenAutenticacion.setEstado("ACTIVO");
        tokenAutenticacionRepository.save(tokenAutenticacion);
        
        // Enviar correo electrónico con el código de verificación
        try {
            emailService.sendRecoveryCode(persona.getCorreo(), codigoVerificacion);
        } catch (MessagingException e) {
            throw new Exception("Error al enviar el correo electrónico: " + e.getMessage());
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Código de recuperación enviado al correo");
        response.put("user", usuario.getIdUsuario());
        response.put("token", token);
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> verifyRecoveryCode(Verify2FADTO verifyRecoveryDTO) throws Exception {
        String token = verifyRecoveryDTO.getToken();
        String codigoVerificacion = verifyRecoveryDTO.getCodigoVerificacion();
        
        if (token == null || codigoVerificacion == null) {
            throw new Exception("Token y código de verificación son requeridos");
        }
        
        // Buscar el token en la base de datos
        Optional<TokenAutenticacion> tokenOptional = tokenAutenticacionRepository
                .findByTokenHashAndTipoToken(token, "RESET_PASSWORD");
        
        if (tokenOptional.isEmpty() || !tokenOptional.get().getEstado().equals("ACTIVO")) {

            throw new Exception("Token inválido");
        }
        
        TokenAutenticacion tokenRecord = tokenOptional.get();
        
        // Verificar si el token ha expirado
        if (LocalDateTime.now().isAfter(tokenRecord.getFechaExpiracion())) {
            tokenRecord.setEstado("EXPIRADO");
            tokenAutenticacionRepository.save(tokenRecord);
            throw new Exception("El token ha expirado");
        }
        
        // Verificar el código de verificación
        if (!tokenRecord.getCodigoVerificacion().equals(codigoVerificacion)) {
            throw new Exception("Código de verificación incorrecto");
        }
        
        // Marcar el token como 'USADO'
        tokenRecord.setEstado("USADO");
        tokenAutenticacionRepository.save(tokenRecord);
        
        // Obtener el usuario asociado al token
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(tokenRecord.getIdUsuario());
        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOptional.get();
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Código de verificación autenticado correctamente");
        response.put("token", tokenRecord.getTokenHash());
        response.put("idUsuario", tokenRecord.getIdUsuario());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        
        return response;
    }
    
    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {
        Long idUsuario = resetPasswordDTO.getIdUsuario();
        String token = resetPasswordDTO.getToken();
        String nuevaContrasenia = resetPasswordDTO.getNuevaContrasenia();
        
        if (idUsuario == null || token == null || nuevaContrasenia == null) {
            throw new Exception("ID de usuario, token y nueva contraseña son requeridos");
        }
        
        // Buscar el token en la base de datos
        Optional<TokenAutenticacion> tokenOptional = tokenAutenticacionRepository
                .findByTokenHashAndTipoToken(token, "RESET_PASSWORD");
        
        if (tokenOptional.isEmpty() || !tokenOptional.get().getEstado().equals("USADO") 
                || !tokenOptional.get().getIdUsuario().equals(idUsuario)) {
            throw new Exception("Token inválido");
        }
        
        TokenAutenticacion tokenRecord = tokenOptional.get();
        
        // Verificar si el token ha expirado
        if (LocalDateTime.now().isAfter(tokenRecord.getFechaExpiracion())) {
            throw new Exception("El token ha expirado");
        }
        
        // Buscar el usuario
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        
        // Actualizar la contraseña del usuario
        Usuario usuario = usuarioOptional.get();
        usuario.setContraseniaHash(passwordEncoder.encrypt(nuevaContrasenia));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void toggle2FAStatus(Long idUsuario) throws Exception {
        // Buscar el usuario por id
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOptional.get();
        
        // Cambiar el estado de autenticación de dos factores
        usuario.setTwoFactorEnable(!usuario.isTwoFactorEnable());
        
        // Guardar los cambios
        usuarioRepository.save(usuario);
        
        log.info("Cambio de estado 2FA para usuario {}: nuevo estado={}", idUsuario, usuario.isTwoFactorEnable());
    }
}
