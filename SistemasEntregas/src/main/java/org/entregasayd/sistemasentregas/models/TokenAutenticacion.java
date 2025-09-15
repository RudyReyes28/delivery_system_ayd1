package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token_autenticacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAutenticacion {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;
    @Column(name = "tipo_token", nullable = false, columnDefinition = "ENUM('verificacion_email', 'reset_password', '2fa', 'login')")
    private String tipoToken;
    @Column(name = "fecha_creacion", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private String fechaCreacion;
    @Column(name = "fecha_expiracion", nullable = false, columnDefinition = "DATETIME")
    private String fechaExpiracion;
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('activo', 'usado', 'expirado') DEFAULT 'activo'")
    private String estado;
    @Column(name = "codigo_verificacion", nullable = false, length = 10)
    private String codigoVerificacion;
}
