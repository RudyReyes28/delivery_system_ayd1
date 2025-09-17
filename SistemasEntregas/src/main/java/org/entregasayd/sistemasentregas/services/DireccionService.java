package org.entregasayd.sistemasentregas.services;

import org.entregasayd.sistemasentregas.models.Direccion;
import org.entregasayd.sistemasentregas.repositories.DireccionRepository;
import org.entregasayd.sistemasentregas.utils.ErrorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionService {
    @Autowired
    private DireccionRepository direccionRepository;

    public Direccion create(Direccion direccion) {
        System.out.println("hola qeu tal "+direccion);
        direccion.setIdDireccion(null);
        return direccionRepository.save(direccion);
    }

    /**
     * Actualiza una direccion
     * @param direccion  direccion a actualizar
     * @return direccion actualizada
     */
    public Direccion update(Direccion direccion) {
        Direccion direccionUpdate = direccionRepository.findById(direccion.getIdDireccion()).orElse(null);
        if(direccionUpdate == null){
            throw new ErrorApi(404,"La direccion no existe");
        }
        direccionUpdate.setTipoDireccion(direccion.getTipoDireccion());
        direccionUpdate.setCodigoPostal(direccion.getCodigoPostal());
        direccionUpdate.setDepartamento(direccion.getDepartamento());
        direccionUpdate.setMunicipio(direccion.getMunicipio());
        return direccionRepository.save(direccionUpdate);
    }

    public Direccion findById(Long id){
        return direccionRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene todas las direcciones
     * @return lista de direcciones
     */
    public List<Direccion> findAll() {
        return direccionRepository.findAll();
    }

    /**
     * Valida el tipo de direccion
     * @param tipoDireccion
     * @return
     */
    private boolean valideType(Direccion.TipoDireccion tipoDireccion) {
        switch (tipoDireccion){
            case ENTREGA, FISCAL, RESIDENCIA,TRABAJO  -> {
                return true;
            }
        }
        return false;
    }
}
