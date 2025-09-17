package org.entregasayd.sistemasentregas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_autenticacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAutenticacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long idToken;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "tipo_token", nullable = false)
    private String tipoToken;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "codigo_verificacion")
    private String codigoVerificacion;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "estado")
    private String estado = "ACTIVO";

    @PrePersist
    public void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    public enum TipoToken {
        VERIFICACION_EMAIL,
        RESET_PASSWORD,
        _2FA,
        LOGIN
    }

    public enum Estado {
        ACTIVO,
        USADO,
        EXPIRADO
    }
}
