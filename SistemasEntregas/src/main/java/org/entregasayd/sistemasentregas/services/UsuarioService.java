package org.entregasayd.sistemasentregas.services;

import jakarta.transaction.Transactional;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoComisionDTO;
import org.entregasayd.sistemasentregas.dto.contrato.ContratoRequestDTO;
import org.entregasayd.sistemasentregas.dto.user.EmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.dto.user.UsuarioResponseDto;
import org.entregasayd.sistemasentregas.mapper.EmpleadoMap;
import org.entregasayd.sistemasentregas.mapper.UsuarioMap;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.repositories.EmpleadoRepository;
import org.entregasayd.sistemasentregas.repositories.UsuarioRepository;
import org.entregasayd.sistemasentregas.utils.Encriptation;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PersonaService personaService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ContratoService contratoService;
    @Autowired
    private UsuarioMap userMap;
    @Autowired
    private EmpleadoMap empleadoMap;
    private Encriptation encriptation;
    @Autowired
    private DireccionService direccionService;

    public UsuarioService() {
        this.encriptation = new Encriptation();
    }
    public List<UsuarioResponseDto> findAll() {
        List<Usuario> listUsers = usuarioRepository.findAll();
        return listUsers.stream().map(userMap::toDto).collect(Collectors.toList());
    }

    public Usuario findByUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Usuario findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null);
    }

    @Transactional
    public EmpleadoRequestDTO create(
            Direccion direccion,
            Persona persona,
            Usuario usuario,
            Empleado empleado,
            ContratoRequestDTO contrato,
            ContratoComisionDTO comision){
        try {
            if(findByUsername(usuario.getNombreUsuario()) != null){
                throw new ErrorApi(400,"El nombre de usuario ya se encuentra en uso.");
            }
            if(personaService.findByDpi(persona.getDpi()) != null){
                throw new ErrorApi(400,"El dpi ya existe");
            }
            if(personaService.findByCorreo(persona.getCorreo()) != null){
                throw new ErrorApi(400,"El correo ya se encuentra en uso");
            }

            if(empleadoRepository.getByCodigoEmpleado(empleado.getCodigoEmpleado()) != null){
                throw new ErrorApi(400,String.format("El codigo %s de empleado ya se encuentra en uso.", empleado.getCodigoEmpleado()));
            }

            if(empleadoRepository.findByNumeroIgss(empleado.getNumeroIgss()) != null){
                throw new ErrorApi(400,String.format("El el numero de iggs '%s' de empleado ya se encuentra en uso.", empleado.getNumeroIgss()));
            }

            Direccion direccionSave = direccionService.create(direccion);
            persona.setDireccion(direccionSave);
            persona.setIdPersona(null);

            //guardamos los datos de la persona
            Persona personaSave = personaService.create(persona);

            usuario.setIdUsuario(null);
            usuario.setPersona(personaSave);
            usuario.setContraseniaHash(encriptation.encrypt(usuario.getContraseniaHash()));
            //guardamos los datos de usuario
            Usuario usuarioSave = usuarioRepository.save(usuario);
            //registrar empleado
            empleado.setUsuario(usuarioSave);
            Empleado empleadoSave = empleadoService.create(empleado);
            //registrar contrato inicial
            contrato.setIdEmpleado(empleadoSave.getIdEmpleado());
            contratoService.createWithComision(contrato, comision);

            return empleadoMap.toDto(empleadoSave);
        } catch (Exception e){
            throw new ErrorApi(500,"Lo sentimos, hubo un error al crear el usuario" + e.getMessage());
        }
    }

    @Transactional
    public UsuarioResponseDto update(Persona persona, Usuario usuario) {
        try {
            Persona personaUpdate = personaService.findById(persona.getIdPersona());
            if(personaUpdate == null){
                throw new ErrorApi(404,"Persona no encontrada");
            }
            personaUpdate.setNombre(persona.getNombre());
            personaUpdate.setApellido(persona.getApellido());
            personaUpdate.setFechaNacimiento(persona.getFechaNacimiento());
            personaUpdate.setDpi(persona.getDpi());
            personaUpdate.setCorreo(persona.getCorreo());
            personaUpdate.setTelefono(persona.getTelefono());

            Usuario usuarioUpdate = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);
            if(usuarioUpdate == null){
                throw new ErrorApi(404,"Usuario no encontrado");
            }
            usuarioUpdate.setPersona(personaUpdate);
            usuarioUpdate.setNombreUsuario(usuario.getNombreUsuario());
            usuarioUpdate.setEstado(usuario.getEstado());
            return userMap.toDto(usuarioRepository.save(usuarioUpdate));
        }catch (Exception e){
            throw new ErrorApi(500,"Lo sentimos, hubo un error al actualizar el usuario");
        }
    }

}
