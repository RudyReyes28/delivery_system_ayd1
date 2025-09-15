package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "fecha_nacimiento", columnDefinition = "DATE")
    private String fechaNacimiento;

    @Column(name = "dpi", nullable = true, length = 20)
    private String dpi;

    @Column(name = "correo", nullable = false, unique = true, length = 255)
    private String correo;

    @Column(name = "telefono", nullable = true, length = 20)
    private String telefono;

    @Column(name = "direccion_completa", nullable = true, columnDefinition = "TEXT")
    private String direccionCompleta;

    @Column(name = "ciudad", nullable = true, length = 100)
    private String ciudad;

    @Column(name = "pais", nullable = true, length = 100)
    private String pais;

    @Column(name = "codigo_postal", nullable = true, length = 10)
    private String codigoPostal;

    @Column(name = "fecha_registro", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private String fechaRegistro;

    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo', 'inactivo') DEFAULT 'activo'")
    private String estado;
}

