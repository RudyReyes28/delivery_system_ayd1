package org.entregasayd.sistemasentregas.services.repartidor.evidencia;

import org.entregasayd.sistemasentregas.dto.EvidenciaDTO;
import org.entregasayd.sistemasentregas.models.EvidenciaEntrega;
import org.entregasayd.sistemasentregas.models.Guia;
import org.entregasayd.sistemasentregas.repositories.EvidenciaEntregaRepository;
import org.entregasayd.sistemasentregas.repositories.GuiaRespository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenciaService {
    @Autowired
    private EvidenciaEntregaRepository repository;
    @Autowired
    private GuiaRespository guiaRespository;

    @Value("${s3.bucket.backend}")
    private String S3_BUCKET_BACKEND;

    @Value("${storage.path.local}")
    private String URL_BASE_LOCAL;

    @Value("${aws.region}")
    private String region;

    @Value("${storage.type}")
    private String storageType;

    public EvidenciaEntrega create(EvidenciaDTO evidenciaDTO){
        Guia guia = guiaRespository.findById(evidenciaDTO.getIdGuia()).
                orElseThrow(()-> new  ErrorApi(404,"Guia no encontrada." ));

        EvidenciaEntrega evidencia = new EvidenciaEntrega();
        evidencia.setGuia(guia);
        return getEvidenciaEntrega(evidenciaDTO, evidencia);
    }

    public EvidenciaEntrega update(EvidenciaDTO evidenciaDTO){
        EvidenciaEntrega evidenciaEntrega = repository.findById(evidenciaDTO.getIdEvidenciaEntrega()).
                orElseThrow(()-> new ErrorApi(404,"Evidencia no encontrada."));
        return getEvidenciaEntrega(evidenciaDTO, evidenciaEntrega);
    }

    private EvidenciaEntrega getEvidenciaEntrega(EvidenciaDTO evidenciaDTO, EvidenciaEntrega evidenciaEntrega) {
        evidenciaEntrega.setTipoEvidencia(evidenciaDTO.getTipoEvidencia());
        evidenciaEntrega.setUrlArchivo(evidenciaDTO.getUrlArchivo());
        evidenciaEntrega.setNombreArchivo(evidenciaDTO.getNombreArchivo());
        evidenciaEntrega.setNombreReceptor(evidenciaDTO.getNombreReceptor());
        evidenciaEntrega.setParentescoReceptor(evidenciaDTO.getParentescoReceptor());
        evidenciaEntrega.setObservaciones(evidenciaDTO.getObservaciones());
        return repository.save(evidenciaEntrega);
    }

    public List<EvidenciaEntrega> getEvidencias(){
        return repository.findAll();
    }

    public List<EvidenciaEntrega> getEvidenciasPorGuia(Long idGuia){
        List<EvidenciaEntrega> evidencias = repository.findByGuiaIdGuia(idGuia);
        for(EvidenciaEntrega evidenciaEntrega : evidencias){
            if(storageType.equals("local")){
                evidenciaEntrega.setUrlArchivo(URL_BASE_LOCAL + evidenciaEntrega.getUrlArchivo());
            }else {
                String urlS3 = "https://" + S3_BUCKET_BACKEND + ".s3." + region + ".amazonaws.com/" + evidenciaEntrega.getUrlArchivo();
                evidenciaEntrega.setUrlArchivo(urlS3);
            }
        }
        return evidencias;
    }

}
