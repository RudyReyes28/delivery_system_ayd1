package org.entregasayd.sistemasentregas.services.filestorage;

import org.entregasayd.sistemasentregas.utils.GeneradorCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3StorageService {

    @Value("${aws.region}")
    private String region;

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    private final S3Client s3Client;
    private final GeneradorCodigo generadorCodigo = new GeneradorCodigo();

    public S3StorageService() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }
    public String getUrl(MultipartFile file) throws IOException {
        String pathFile = generadorCodigo.getCode().replace("-", "") + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(secretAccessKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        //return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + pathFile;
        return pathFile;
    }
}
