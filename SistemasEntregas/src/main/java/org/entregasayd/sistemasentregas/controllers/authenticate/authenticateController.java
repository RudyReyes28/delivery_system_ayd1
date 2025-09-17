package org.entregasayd.sistemasentregas.controllers.authenticate;


import org.entregasayd.sistemasentregas.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.entregasayd.sistemasentregas.dto.authenticate.*;
import org.entregasayd.sistemasentregas.services.authenticate.authenticateService;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> registerUserController(@RequestBody RegisterUserDTO userDTO) {
        try {
            Usuario usuario = authService.registerUser(userDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("status", "success");

            return ResponseEntity.ok(response);

        }catch(Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al registrar el usuario: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.status(500).body(response);
        }


    }

    //Login de usuario
    @PostMapping
    public ResponseEntity<?> loginUserController(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    // Verify 2FA Code
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FAController(@RequestBody Verify2FADTO verify2FADTO) {
        try {
            Map<String, Object> response = authService.verify2FACode(verify2FADTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá la traza completa en el log del servidor
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("details", "Token: " + verify2FADTO.getToken() + ", Código: " + verify2FADTO.getCodigoVerificacion());
            
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
    
    // Recover Password
    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPasswordController(@RequestBody RecoverPasswordDTO recoverPasswordDTO) {
        try {
            Map<String, Object> response = authService.recoverPassword(recoverPasswordDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
    
    // Verify Recovery Code
    @PostMapping("/verify-recovery")
    public ResponseEntity<?> verifyRecoveryCodeController(@RequestBody Verify2FADTO verifyRecoveryDTO) {
        try {
            Map<String, Object> response = authService.verifyRecoveryCode(verifyRecoveryDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
    
    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasswordController(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            authService.resetPassword(resetPasswordDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Contraseña actualizada correctamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
    
    // Toggle 2FA Status
    @PutMapping("/toggle-2fa/{idUsuario}")
    public ResponseEntity<?> toggle2FAStatusController(@PathVariable Long idUsuario) {
        try {
            authService.toggle2FAStatus(idUsuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Autenticación de dos factores actualizada correctamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(e.getMessage().contains("no encontrado") ? 404 : 500).body(errorResponse);
        }
    }
}
