package org.entregasayd.sistemasentregas.services.fidelizacion;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.entregasayd.sistemasentregas.repositories.*;
import org.entregasayd.sistemasentregas.models.*;
import org.entregasayd.sistemasentregas.dto.fidelizacion.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Service
public class LoyaltyProgramService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaFidelizacionRepository empresaFidelizacionRepository;

    @Autowired
    private NivelFidelizacionRepository nivelFidelizacionRepository;

    //Obtener todas las empresas con y sin su programa de fidelizacion y nivel
    public ArrayList<GetCompaniesFidelizacionDTO> getAllCompanies() {
        //Obtenemos todas las empresas
        ArrayList<Empresa> empresas = (ArrayList<Empresa>) empresaRepository.findAll();
        ArrayList<GetCompaniesFidelizacionDTO> result = new ArrayList<>();

        //Recorremos las empresas y buscamos su programa de fidelizacion y nivel
        for (Empresa empresa : empresas) {
            GetCompaniesFidelizacionDTO.EmpresaFidelizacionDTO empresaFidelizacionDTO = null;
            GetCompaniesFidelizacionDTO.NivelFindelizacionDTO nivelFindelizacionDTO = null;
            EmpresaFidelizacion empresaFidelizacion = empresaFidelizacionRepository.findByEmpresaIdEmpresa(empresa.getIdEmpresa());
            if (empresaFidelizacion != null) {
                empresaFidelizacionDTO = new GetCompaniesFidelizacionDTO.EmpresaFidelizacionDTO(
                        empresaFidelizacion.getIdEmpresaFidelizacion(),
                        empresaFidelizacion.getMes(),
                        empresaFidelizacion.getAnio(),
                        empresaFidelizacion.getTotalEntregas(),
                        empresaFidelizacion.getMontoTotalEntregas(),
                        empresaFidelizacion.getTotalCancelaciones(),
                        empresaFidelizacion.getDescuentoAplicado(),
                        empresaFidelizacion.getPenalizacionesAplicadas()
                );
                NivelFindelizacion nivel = empresaFidelizacion.getNivelFidelizacion();
                if (nivel != null) {
                    nivelFindelizacionDTO = new GetCompaniesFidelizacionDTO.NivelFindelizacionDTO(
                            nivel.getIdNivel(),
                            nivel.getCodigoNivel(),
                            nivel.getNombreNivel(),
                            nivel.getDescripcion(),
                            nivel.getEntregasMinimas(),
                            nivel.getEntregasMaximas(),
                            nivel.getDescuentoPorcentaje(),
                            nivel.getCancelacionesGratuitasMes(),
                            nivel.getPorcentajePenalizacion(),
                            nivel.getActivo(),
                            nivel.getFechaInicioVigencia().toString(),
                            nivel.getFechaFinVigencia() != null ? nivel.getFechaFinVigencia().toString() : null
                    );
                }
            }
            GetCompaniesFidelizacionDTO.EmpresaFideDTO empresaFideDTO = new GetCompaniesFidelizacionDTO.EmpresaFideDTO(
                    empresa.getIdEmpresa(),
                    empresa.getTipoEmpresa().toString(),
                    empresa.getNombreComercial(),
                    empresa.getRazonSocial(),
                    empresa.getNit(),
                    empresa.getRegistroMercantil(),
                    empresa.getFechaConstitucion().toString(),
                    empresa.getFechaAfiliacion().toString(),
                    empresa.getFechaVencimientoAfiliacion().toString(),
                    empresa.getEstado().toString(),
                    empresaFidelizacionDTO,
                    nivelFindelizacionDTO
            );
            GetCompaniesFidelizacionDTO dto = new GetCompaniesFidelizacionDTO(empresaFideDTO);
            result.add(dto);
        }
        return result;
    }


    @Transactional
    public EmpresaFidelizacion createLoyaltyProgram(CreateLoyaltyProgramDTO dto) {
        //log.info("Creating loyalty program for company ID: {}", dto.getIdEmpresa());

        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa not found with ID: " + dto.getIdEmpresa()));

        //Buscamos en EmpresaFidelizacion si ya tiene un programa activo
        if (empresaFidelizacionRepository.existsByEmpresaIdEmpresa(dto.getIdEmpresa())) {
            throw new RuntimeException("La empresa ya tiene un programa de fidelizacion activo");
        }

        //Obtenemos el nivel de fidelizacion en base al codigo
        NivelFindelizacion nivelGuardado = nivelFidelizacionRepository.findByCodigoNivel(dto.getCodigoNivel());
        if (nivelGuardado == null) {
            //Si no existe el nivel, lo creamos
            nivelGuardado = createNewLevel(dto.getCodigoNivel());
            //Guardamos el nuevo nivel
            nivelFidelizacionRepository.save(nivelGuardado);
        }

        //CREAMOS EMPRESA FIDELIZACION
        EmpresaFidelizacion nuevoPrograma = new EmpresaFidelizacion();
        nuevoPrograma.setEmpresa(empresa);
        nuevoPrograma.setNivelFidelizacion(nivelGuardado);
        nuevoPrograma.setMes(LocalDate.now().getMonthValue());
        nuevoPrograma.setAnio(LocalDate.now().getYear());
        nuevoPrograma.setTotalEntregas(0);
        nuevoPrograma.setMontoTotalEntregas(0.0);
        nuevoPrograma.setTotalCancelaciones(0);
        nuevoPrograma.setDescuentoAplicado(0.0);
        nuevoPrograma.setPenalizacionesAplicadas(0.0);
        return empresaFidelizacionRepository.save(nuevoPrograma);
    }

    //Cambiar el nivel de fidelizacion de una empresa
    @Transactional
    public EmpresaFidelizacion changeLoyaltyLevel(CreateLoyaltyProgramDTO dto) {
        //Obtenemos la empresa
        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa not found with ID: " + dto.getIdEmpresa()));

        //Buscamos en EmpresaFidelizacion si ya tiene un programa activo
        if (!empresaFidelizacionRepository.existsByEmpresaIdEmpresa(dto.getIdEmpresa())) {
            throw new RuntimeException("La empresa no tiene un programa de fidelizacion activo");
        }
        EmpresaFidelizacion empresaFidelizacion = empresaFidelizacionRepository.findByEmpresaIdEmpresa(dto.getIdEmpresa());

        //Obtenemos el nivel de fidelizacion en base al codigo
        NivelFindelizacion nuevoNivel = nivelFidelizacionRepository.findByCodigoNivel(dto.getCodigoNivel());
        if (nuevoNivel == null) {
            //Si no existe el nivel, lo creamos
            nuevoNivel = createNewLevel(dto.getCodigoNivel());
            //Guardamos el nuevo nivel
            nivelFidelizacionRepository.save(nuevoNivel);

        }
        //Cambiamos el nivel de fidelizacion
        empresaFidelizacion.setNivelFidelizacion(nuevoNivel);
        return empresaFidelizacionRepository.save(empresaFidelizacion);

    }

    /*
    //Cambiar los detalles del nivel de fidelizacion
    @Transactional
    public NivelFindelizacion updateLoyaltyLevelDetails(ChangeLoyaltyLevelDTO dto){
        //Obtenemos el nivel de fidelizacion
        NivelFindelizacion nivel = nivelFidelizacionRepository.findById(dto.getIdNivelFidelizacion())
                .orElseThrow(() -> new RuntimeException("Nivel de fidelizacion not found with ID: " + dto.getIdNivelFidelizacion()));

        //Actualizamos los detalles del nivel
        nivel.setEntregasMinimas(dto.getEntregasMinimas());
        nivel.setEntregasMaximas(dto.getEntregasMaximas());
        nivel.setDescuentoPorcentaje(dto.getDescuentoPorcentaje());
        nivel.setCancelacionesGratuitasMes(dto.getCancelacionesGratuitasMes());
        nivel.setPorcentajePenalizacion(dto.getPorcentajePenalizacion());

        return nivelFidelizacionRepository.save(nivel);
    }*/


    private NivelFindelizacion createNewLevel(String codigoNivel){

        if(codigoNivel.equals("PLATA")){
            NivelFindelizacion nuevoNivel = new NivelFindelizacion();
            nuevoNivel.setCodigoNivel("PLATA");
            nuevoNivel.setNombreNivel("Nivel Plata");
            nuevoNivel.setDescripcion("Nivel de fidelizacion Plata");
            nuevoNivel.setEntregasMinimas(0);
            nuevoNivel.setEntregasMaximas(99);
            nuevoNivel.setDescuentoPorcentaje(5.0); //5%
            nuevoNivel.setCancelacionesGratuitasMes(0);
            nuevoNivel.setPorcentajePenalizacion(100.0); //100%
            nuevoNivel.setActivo(true);
            nuevoNivel.setFechaInicioVigencia(LocalDate.now());
            return nivelFidelizacionRepository.save(nuevoNivel);
        }else if (codigoNivel.equals("ORO")){
            NivelFindelizacion nuevoNivel = new NivelFindelizacion();
            nuevoNivel.setCodigoNivel("ORO");
            nuevoNivel.setNombreNivel("Nivel Oro");
            nuevoNivel.setDescripcion("Nivel de fidelizacion Oro");
            nuevoNivel.setEntregasMinimas(100);
            nuevoNivel.setEntregasMaximas(299);
            nuevoNivel.setDescuentoPorcentaje(8.0); //10%
            nuevoNivel.setCancelacionesGratuitasMes(0);
            nuevoNivel.setPorcentajePenalizacion(50.0); //50%
            nuevoNivel.setActivo(true);
            nuevoNivel.setFechaInicioVigencia(LocalDate.now());
            return nivelFidelizacionRepository.save(nuevoNivel);
        }else{
            NivelFindelizacion nuevoNivel = new NivelFindelizacion();
            nuevoNivel.setCodigoNivel("DIAMANTE");
            nuevoNivel.setNombreNivel("Nivel Diamante");
            nuevoNivel.setDescripcion("Nivel de fidelizacion Diamante");
            nuevoNivel.setEntregasMinimas(300);
            nuevoNivel.setEntregasMaximas(null); //Sin maximo
            nuevoNivel.setDescuentoPorcentaje(12.0); //15%
            nuevoNivel.setCancelacionesGratuitasMes(5);
            nuevoNivel.setPorcentajePenalizacion(50.0); //25%
            nuevoNivel.setActivo(true);
            nuevoNivel.setFechaInicioVigencia(LocalDate.now());
            return nivelFidelizacionRepository.save(nuevoNivel);
        }
    }


}
