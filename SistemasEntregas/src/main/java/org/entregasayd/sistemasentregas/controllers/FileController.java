package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.services.filestorage.FileStorageService;
import org.entregasayd.sistemasentregas.services.filestorage.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private S3StorageService s3StorageService;

    @Value("${storage.type}")
    private String storageType;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String contentType = file.getContentType();
//            String pathFile = generadorCodigo.getCode().replace("-", "") + file.getOriginalFilename();
//            Path path = Paths.get(url + File.separator + pathFile);
//            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            String pathFile = "";

            if(storageType.equals("local")){
                pathFile = fileStorageService.getUrl(file);
            }else if(storageType.equals("s3")){
                pathFile = s3StorageService.uploadToS3(file);
            }

            Map<String, String> map = new HashMap<>();
            map.put("url", pathFile);
            return ResponseEntity.ok().body(map);
        } catch (IOException e) {
            return ResponseEntity.status(409).body("Error al subir el archivo: " + e.getMessage());
        }

    }
}
