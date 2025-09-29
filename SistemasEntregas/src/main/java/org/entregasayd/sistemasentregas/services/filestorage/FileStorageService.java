package org.entregasayd.sistemasentregas.services.filestorage;

import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.entregasayd.sistemasentregas.utils.GeneradorCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${storage.path}")
    private String storagePath;

    @Value("${storage.type}")
    private String storageType;

    private final GeneradorCodigo generadorCodigo = new GeneradorCodigo();


    public String getUrl(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String pathFile = generadorCodigo.getCode().replace("-", "") + originalFilename;
        Path path = Paths.get(storagePath, pathFile);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return pathFile;
    }


}
