package org.entregasayd.sistemasentregas.mapper;

import org.entregasayd.sistemasentregas.dto.user.UsuarioResponseDto;
import org.entregasayd.sistemasentregas.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioMap {

    @Mappings({
            @Mapping(source = "idUsuario", target = "idUsuario"),
            @Mapping(source = "persona.idPersona", target = "idPersona"),
            @Mapping(source = "rol.idRol", target = "idRol"),
            @Mapping(source = "rol.nombre", target = "nombreRol"),
            @Mapping(source = "nombreUsuario", target = "nombreUsuario"),
            @Mapping(source = "twoFactorEnable", target = "twoFactorEnabled"),
            @Mapping(source = "intentosFallidos", target = "intentosFallidos"),
            @Mapping(source = "ultimaFechaAcceso", target = "ultimaFechaAcceso"),
            @Mapping(source = "estado", target = "estado"),
            @Mapping(source = "fechaCreacion", target = "fechaCreacion"),
            @Mapping(source = "fechaUltimaActualizacion", target = "fechaUltimaActualizacion"),

            // Datos de Persona
            @Mapping(source = "persona.nombre", target = "nombre"),
            @Mapping(source = "persona.apellido", target = "apellido"),
            @Mapping(source = "persona.fechaNacimiento", target = "fechaNacimiento"),
            @Mapping(source = "persona.dpi", target = "dpi"),
            @Mapping(source = "persona.correo", target = "correo"),
            @Mapping(source = "persona.telefono", target = "telefono"),
            @Mapping(expression = "java(personaDireccion(usuario))", target = "direccion"),
            @Mapping(source = "persona.estado", target = "estadoPersona")
    })
    UsuarioResponseDto toDto(Usuario usuario);

    default String personaDireccion(Usuario usuario) {
        if (usuario.getPersona() == null || usuario.getPersona().getDireccion() == null) {
            return null;
        }
        var dir = usuario.getPersona().getDireccion();
        return dir.getMunicipio() + ", " + dir.getDepartamento() + ", " + dir.getPais();
    }


}
