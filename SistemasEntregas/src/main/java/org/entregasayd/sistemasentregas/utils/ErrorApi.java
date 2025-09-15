package org.entregasayd.sistemasentregas.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorApi extends Error{
    private final int status;

    public ErrorApi(int status, String message) {
        super(message);
        this.status = status;
    }
}
