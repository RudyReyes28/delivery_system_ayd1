package org.entregasayd.sistemasentregas.utils;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GeneradorCodigo {

    public String getCodigo(String prefijo, LocalDate fecha, long contador){
        return prefijo + "-" + fecha + "-" + contador;
    }

    public String getCode(){
        return UUID.randomUUID().toString();
    }
}
