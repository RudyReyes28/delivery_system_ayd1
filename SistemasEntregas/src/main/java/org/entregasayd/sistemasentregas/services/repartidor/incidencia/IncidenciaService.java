package org.entregasayd.sistemasentregas.services.repartidor.incidencia;

import org.entregasayd.sistemasentregas.dto.IncidenciaDTO;
import org.entregasayd.sistemasentregas.models.Guia;
import org.entregasayd.sistemasentregas.models.Incidencia;
import org.entregasayd.sistemasentregas.models.Repartidor;
import org.entregasayd.sistemasentregas.repositories.GuiaRespository;
import org.entregasayd.sistemasentregas.repositories.IncidenciaRepository;
import org.entregasayd.sistemasentregas.repositories.RepartidorRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.entregasayd.sistemasentregas.utils.GeneradorCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncidenciaService {
    @Autowired
    private IncidenciaRepository repository;
    @Autowired
    private GuiaRespository guiaRespository;
    @Autowired
    private RepartidorRepository repartidorRepository;

    private GeneradorCodigo generarCodigo = new GeneradorCodigo();


    public Incidencia create(IncidenciaDTO incidenciaDTO){
        Guia guia = guiaRespository.findById(
                incidenciaDTO.getIdGuia()).orElseThrow(()-> new ErrorApi(404, "Guia no encontrada"));
        //idRepartidor en realidad es el id del usuario logeado
        Repartidor repartidor = repartidorRepository.findByEmpleado_Usuario_IdUsuario(incidenciaDTO.getIdRepartidor());
        if(repartidor == null){
            throw new ErrorApi(404, "Repartidor no encontrado");
        }
        ;//repartidorRepository.findById(incidenciaDTO.getIdRepartidor()).orElseThrow(()-> new ErrorApi(404, "Repartidor no encontrado"));

        Incidencia nuevaIncidencia = new Incidencia();
        nuevaIncidencia.setCodigoIncidencia(
                generarCodigo.getCodigo("INC", LocalDate.now(), getIncidencias().size() + 1 )
        );
        nuevaIncidencia.setGuia(guia);
        nuevaIncidencia.setRepartidor(repartidor);
        nuevaIncidencia.setTipoIncidencia(incidenciaDTO.getTipoIncidencia());
        nuevaIncidencia.setSeveridad(incidenciaDTO.getSeveridad());
        nuevaIncidencia.setDescripcion(incidenciaDTO.getDescripcion());
        nuevaIncidencia.setRequiereDevolucion(incidenciaDTO.getRequiereDevolucion());
        nuevaIncidencia.setSolucionAplicada(incidenciaDTO.getSolucionAplicada());
        nuevaIncidencia.setCostoAdicional(incidenciaDTO.getCostoAdicional());
        return repository.save(nuevaIncidencia);
    }

    public Incidencia update(IncidenciaDTO incidenciaDTO){

        Incidencia actualizarIncidencia = repository.findById(
                incidenciaDTO.getIdIncidencia()).orElseThrow(()-> new ErrorApi(404, "Incidencia no encontrado"));
        actualizarIncidencia.setTipoIncidencia(incidenciaDTO.getTipoIncidencia());
        actualizarIncidencia.setSeveridad(incidenciaDTO.getSeveridad());
        actualizarIncidencia.setDescripcion(incidenciaDTO.getDescripcion());
        actualizarIncidencia.setSolucionAplicada(incidenciaDTO.getSolucionAplicada());
        actualizarIncidencia.setRequiereDevolucion(incidenciaDTO.getRequiereDevolucion());
        actualizarIncidencia.setCostoAdicional(incidenciaDTO.getCostoAdicional());
        actualizarIncidencia.setFechaAtencion(incidenciaDTO.getFechaAtencion());
        actualizarIncidencia.setFechaResolucion(incidenciaDTO.getFechaResolucion());
        actualizarIncidencia.setEstado(incidenciaDTO.getEstado());
        return repository.save(actualizarIncidencia);
    }

    public List<Incidencia> getIncidenciasPorGuia(Long idGuia){
        return repository.findByGuiaIdGuia(idGuia);
    }

    public List<Incidencia> getByTipoIncidencia(String tipoIncidencia){
        return repository.findByTipoIncidencia(Incidencia.TipoIncidencia.valueOf(tipoIncidencia));
    }

    public List<Incidencia> getBySeveridad(String severidad){
        return repository.findBySeveridad(Incidencia.Severidad.valueOf(severidad));
    }

    public List<Incidencia> getByEstado(String estado){
        return repository.findByEstado(Incidencia.Estado.valueOf(estado));
    }

    public List<Incidencia> getIncidencias(){
        return repository.findAll();
    }
}
