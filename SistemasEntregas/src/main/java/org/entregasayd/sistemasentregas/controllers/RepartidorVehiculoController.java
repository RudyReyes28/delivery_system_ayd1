package org.entregasayd.sistemasentregas.controllers;

import org.entregasayd.sistemasentregas.dto.RepartidorVehiculoDTO;
import org.entregasayd.sistemasentregas.services.RepartidorVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/repartidor-vehiculo")
public class RepartidorVehiculoController {
    @Autowired
    private RepartidorVehiculoService repartidorVehiculoService;

    @PostMapping("/crear-repartidor-vehiculo")
    public RepartidorVehiculoDTO crearRepartidorVehiculo(@RequestBody RepartidorVehiculoDTO repartidorVehiculoDTO) {
        return repartidorVehiculoService.save(repartidorVehiculoDTO);
    }

    @PutMapping("/actualizar-repartidor-vehiculo")
    public RepartidorVehiculoDTO actualizarRepartidorVehiculo(@RequestBody RepartidorVehiculoDTO repartidorVehiculoDTO) {
        return repartidorVehiculoService.update(repartidorVehiculoDTO);
    }

    @GetMapping("/get-vehiculos-asignados/{idRepartidor}")
    public List<RepartidorVehiculoDTO> vehiculosAsignados(@PathVariable Long idRepartidor) {
        return repartidorVehiculoService.vehiculosAsignados(idRepartidor);
    }

    @GetMapping("/get-all")
    public List<RepartidorVehiculoDTO> getAll() {
        return repartidorVehiculoService.getAll();
    }
}