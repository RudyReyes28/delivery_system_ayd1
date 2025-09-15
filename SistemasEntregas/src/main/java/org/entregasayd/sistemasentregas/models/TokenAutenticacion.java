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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "token_hash", nullable = false, length = 512)
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_token", nullable = false)
    private TipoToken tipoToken;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "codigo_verificacion", length = 20)
    private String codigoVerificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = Estado.ACTIVO;
        }
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
