package org.entregasayd.sistemasentregas.controllers.authenticate;


import org.entregasayd.sistemasentregas.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.entregasayd.sistemasentregas.dto.authenticate.registerUserDTO;
import org.entregasayd.sistemasentregas.services.authenticate.authenticateService;

@RestController
@RequestMapping("/login")
class authenticateController {

    private final authenticateService authService;
    @Autowired
    public authenticateController(authenticateService authService) {
        this.authService = authService;
    }

    //Registro de usuario
    @PostMapping("/register")
    public Usuario registerUserController(@RequestBody registerUserDTO userDTO) {
        return authService.registerUser(userDTO);

    }
    /* Ejemplo request body
    {
        "tipoDireccion": "RESIDENCIA",
        "municipio": "Guatemala",
        "departamento": "Guatemala",
        "pais": "Guatemala",
        "codigoPostal": "01001",
        "referencias": "Cerca del parque",
        "nombre": "Juan",
        "apellido": "Perez",
        "fechaNacimiento": "1990-01-01",
        "dpi": "1234567890101",
        "correo": "rudyoxlajrudy@gmail.com",
        "telefono": "12345678",
        "nombreUsuario": "juanperez",
        "contraseniaHash": "juan123"
    }


    */



}
