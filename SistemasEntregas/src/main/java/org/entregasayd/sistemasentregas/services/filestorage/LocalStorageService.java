package org.entregasayd.sistemasentregas.services.filestorage;

import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.entregasayd.sistemasentregas.utils.GeneradorCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService {

    @Value("${storage.path}")
    private String storagePath;

    private final GeneradorCodigo generadorCodigo = new GeneradorCodigo();

    public LocalStorageService() {}

    public String getUrl(MultipartFile file) throws IOException {
        String pathFile = generadorCodigo.getCode().replace("-", "") + file.getOriginalFilename();
        Path path = Paths.get(storagePath, pathFile);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return pathFile;
    }
}
