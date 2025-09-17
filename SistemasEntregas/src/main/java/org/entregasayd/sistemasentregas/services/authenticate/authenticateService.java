package org.entregasayd.sistemasentregas.services.authenticate;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.repositories.UsuarioRepository;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.authenticate.registerUserDTO;
import org.entregasayd.sistemasentregas.repositories.PersonaRepository;
import org.entregasayd.sistemasentregas.repositories.DireccionRepository;
import org.entregasayd.sistemasentregas.repositories.RolRepository;
import org.entregasayd.sistemasentregas.utils.Encriptation;
import java.time.LocalDate;

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

    private Encriptation passwordEncoder = new Encriptation();

    public Usuario registerUser(registerUserDTO userDTO) {
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

}
