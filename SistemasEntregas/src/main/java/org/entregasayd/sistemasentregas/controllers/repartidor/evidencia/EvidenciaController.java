package org.entregasayd.sistemasentregas.controllers.repartidor.evidencia;

import org.entregasayd.sistemasentregas.dto.EvidenciaDTO;
import org.entregasayd.sistemasentregas.mapper.EvidenciaMap;
import org.entregasayd.sistemasentregas.services.filestorage.FileStorageService;
import org.entregasayd.sistemasentregas.services.repartidor.evidencia.EvidenciaService;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {
    @Autowired
    private EvidenciaService evidenciaService;
    @Autowired
    private EvidenciaMap evidenciaMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/create")
    public EvidenciaDTO create(@RequestBody EvidenciaDTO evidenciaDTO){
        return evidenciaMapper.mapToDTO(evidenciaService.create(evidenciaDTO));
    }

    @PutMapping("/update")
    public EvidenciaDTO update(@RequestBody EvidenciaDTO evidenciaDTO){
        return evidenciaMapper.mapToDTO(evidenciaService.update(evidenciaDTO));
    }

    @GetMapping("/get-by-guia/{idGuia}")
    public List<EvidenciaDTO> getEvidenciasPorGuia(@PathVariable Long idGuia){
        return evidenciaService.getEvidenciasPorGuia(idGuia).stream().map(evidenciaMapper::mapToDTO).toList();
    }
}
