package es.cic.curso.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.cic.curso.back.exception.ViaNotFoundException;
import es.cic.curso.back.model.Via;
import es.cic.curso.back.repository.ViaRepository;

@Service
public class ViaService {

    @Autowired
    private ViaRepository viaRepository;

    public List<Via> getAllVias() {
        return viaRepository.findAll();
    }

    public Via getViaById(Long id) {
        return viaRepository.findById(id)
                .orElseThrow(() -> new ViaNotFoundException("Vía no encontrada con id: " + id));
    }

    public Via createVia(Via via) {
        return viaRepository.save(via);
    }

    public Via updateVia(Long id, Via viaDetails) {
        Via via = viaRepository.findById(id)
                .orElseThrow(() -> new ViaNotFoundException("Vía no encontrada con id: " + id));
        via.setNombre(viaDetails.getNombre());
        via.setLongitud(viaDetails.getLongitud());
        via.setAncho(viaDetails.getAncho());
        via.setCapacidad(viaDetails.getCapacidad());
        via.setEstado(viaDetails.getEstado());
        via.setCongestion(viaDetails.getCongestion());
        via.setCreacion(viaDetails.getCreacion());
        via.setModificacion(viaDetails.getModificacion());
        via.setTipo(viaDetails.getTipo());
        return viaRepository.save(via);
    }

    public void deleteVia(Long id) {
        Via via = viaRepository.findById(id)
                .orElseThrow(() -> new ViaNotFoundException("Vía no encontrada con id: " + id));
        viaRepository.delete(via);
    }
}
