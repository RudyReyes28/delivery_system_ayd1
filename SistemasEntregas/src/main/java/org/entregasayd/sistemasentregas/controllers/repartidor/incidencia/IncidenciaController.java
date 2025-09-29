package org.entregasayd.sistemasentregas.controllers.repartidor.incidencia;

import org.entregasayd.sistemasentregas.dto.IncidenciaDTO;
import org.entregasayd.sistemasentregas.mapper.IncidenciaMap;
import org.entregasayd.sistemasentregas.services.repartidor.incidencia.IncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {
    @Autowired
    private IncidenciaService incidenciaService;
    @Autowired
    private IncidenciaMap incidenciaMapper;

    @PostMapping("/crear")
    public IncidenciaDTO crearIncidencia(@RequestBody IncidenciaDTO incidenciaDTO){
        return incidenciaMapper.mapToDTO(incidenciaService.create(incidenciaDTO));
    }

    @PutMapping("/actualizar")
    public IncidenciaDTO actualizarIncidencia(@RequestBody IncidenciaDTO incidenciaDTO){
        return incidenciaMapper.mapToDTO(incidenciaService.update(incidenciaDTO));
    }

    @GetMapping("/obtener-por-guia/{id}")
    public List<IncidenciaDTO> obtenerIncidencia(@PathVariable Long id){
        return incidenciaService.getIncidenciasPorGuia(id).stream().map(incidenciaMapper::mapToDTO).toList();
    }

    @GetMapping("/obtener-por-tipo/{tipoIncidencia}")
    public List<IncidenciaDTO> obtenerIncidenciaPorTipo(@PathVariable String tipoIncidencia){
        return incidenciaService.getByTipoIncidencia(tipoIncidencia).stream().map(incidenciaMapper::mapToDTO).toList();
    }

    @GetMapping("/obtener-por-severidad/{severidad}")
    public List<IncidenciaDTO> obtenerIncidenciaPorSeveridad(@PathVariable String severidad){
        return incidenciaService.getBySeveridad(severidad).stream().map(incidenciaMapper::mapToDTO).toList();
    }

    @GetMapping("/obtener-por-estado/{estado}")
    public List<IncidenciaDTO> obtenerIncidenciaPorEstado(@PathVariable String estado){
        return incidenciaService.getByEstado(estado).stream().map(incidenciaMapper::mapToDTO).toList();
    }

    @GetMapping("/listar")
    public List<IncidenciaDTO> listarIncidencias(){
        return incidenciaService.getIncidencias().stream().map(incidenciaMapper::mapToDTO).toList();
    }
}
