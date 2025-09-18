package org.entregasayd.sistemasentregas.controllers;

import jakarta.validation.Valid;
import org.entregasayd.sistemasentregas.dto.user.EmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.dto.user.RegistroEmpleadoRequestDTO;
import org.entregasayd.sistemasentregas.dto.user.UsuarioRequestDdto;
import org.entregasayd.sistemasentregas.dto.user.UsuarioResponseDto;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.services.DireccionService;
import org.entregasayd.sistemasentregas.services.PersonaService;
import org.entregasayd.sistemasentregas.services.RolService;
import org.entregasayd.sistemasentregas.services.UsuarioService;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DireccionService direccionService;
    @Autowired
    private RolService rolService;

    @PostMapping("/create")
    public UsuarioResponseDto createUser(@RequestBody RegistroEmpleadoRequestDTO register) {

        UsuarioRequestDdto usuario = register.getUsuarioRequestDdto();
        EmpleadoRequestDTO empleado = register.getEmpleadoRequestDdto();

        Persona personaSave = new Persona();
        personaSave.setNombre(usuario.getNombre());
        personaSave.setApellido(usuario.getApellido());
        personaSave.setFechaNacimiento(usuario.getFechaNacimiento());
        personaSave.setDpi(usuario.getDpi());
        personaSave.setCorreo(usuario.getCorreo());
        personaSave.setTelefono(usuario.getTelefono());

        Direccion direccionSave = direccionService.findById(usuario.getIdDireccion());
        if (direccionSave == null) {
            throw new ErrorApi(400, "La direccion no existe");
        }
        personaSave.setDireccion(direccionSave);

        Rol rolSave = rolService.findById(usuario.getIdRol());
        if (rolSave == null) {
            throw new ErrorApi(400, "El rol no es valido");
        }

        Usuario usuarioSave = new Usuario();
        usuarioSave.setRol(rolSave);
        usuarioSave.setNombreUsuario(usuario.getNombreUsuario());
        usuarioSave.setContraseniaHash(usuario.getContrasena());

        //Empleado
        Empleado empleadoSave = new Empleado();
        empleadoSave.setCodigoEmpleado(empleado.getCodigoEmpleado());
        empleadoSave.setNumeroIgss(empleado.getNumeroIgss());
        empleadoSave.setNumeroIrtra(empleado.getNumeroIrtra());
        empleadoSave.setTipoSangre(empleado.getTipoSangre());
        empleadoSave.setEstadoCivil(empleado.getEstadoCivil());
        empleadoSave.setNumeroDependientes(empleado.getNumeroDependientes());
        empleadoSave.setContactoEmergenciaNombre(empleado.getContactoEmergenciaNombre());
        empleadoSave.setContactoEmergenciaTelefono(empleado.getContactoEmergenciaTelefono());
        empleadoSave.setEstadoEmpleado(empleado.getEstadoEmpleado());
        empleadoSave.setFechaIngreso(empleado.getFechaIngreso());

        return usuarioService.create(personaSave, usuarioSave, empleadoSave);
    }

    @PutMapping("/update")
    public UsuarioResponseDto updateUser(@RequestBody UsuarioResponseDto usuario) {
        Persona personaUpdate = new Persona();
        personaUpdate.setIdPersona(usuario.getIdPersona());
        personaUpdate.setNombre(usuario.getNombre());
        personaUpdate.setApellido(usuario.getApellido());
        personaUpdate.setFechaNacimiento(usuario.getFechaNacimiento());
        personaUpdate.setDpi(usuario.getDpi());
        personaUpdate.setCorreo(usuario.getCorreo());
        personaUpdate.setTelefono(usuario.getTelefono());

        Usuario usuarioUpdate = new Usuario();
        usuarioUpdate.setIdUsuario(usuario.getIdUsuario());
        usuarioUpdate.setPersona(personaUpdate);
        usuarioUpdate.setNombreUsuario(usuario.getNombreUsuario());
        usuarioUpdate.setEstado(usuario.getEstado());
        return usuarioService.update(personaUpdate, usuarioUpdate);
    }

    @GetMapping("/all")
    public List<UsuarioResponseDto> findAll() {
        return usuarioService.findAll();
    }
}
