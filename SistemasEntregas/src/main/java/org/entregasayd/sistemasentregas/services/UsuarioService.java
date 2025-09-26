package org.entregasayd.sistemasentregas.services;

import jakarta.transaction.Transactional;
import org.entregasayd.sistemasentregas.dto.user.EmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.dto.user.UsuarioResponseDto;
import org.entregasayd.sistemasentregas.mapper.EmpleadoMap;
import org.entregasayd.sistemasentregas.mapper.UsuarioMap;
import org.entregasayd.sistemasentregas.models.Empleado;
import org.entregasayd.sistemasentregas.models.Persona;
import org.entregasayd.sistemasentregas.models.Usuario;
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
    private UsuarioMap userMap;
    @Autowired
    private EmpleadoMap empleadoMap;
    private Encriptation encriptation;

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
    public EmpleadoRequestDTO create(Persona persona, Usuario usuario, Empleado empleado){
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
            persona.setIdPersona(null);
            usuario.setIdUsuario(null);
            Persona personaSave = personaService.create(persona);
            usuario.setPersona(personaSave);
            usuario.setContraseniaHash(encriptation.encrypt(usuario.getContraseniaHash()));

            Usuario usuarioSave = usuarioRepository.save(usuario);
            //registrar empleado
            empleado.setUsuario(usuarioSave);
            return empleadoMap.toDto(empleadoService.create(empleado));
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
