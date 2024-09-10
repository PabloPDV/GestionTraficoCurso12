package es.cic.curso.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso.back.exception.ViaNotFoundException;
import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.repository.TipoViaRepository;

@Service
@Transactional
public class TipoViaService {

    @Autowired
    private TipoViaRepository tipoViaRepository;

    @Transactional(readOnly = true)
    public List<TipoVia> findAll() {
        return tipoViaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoVia findById(Long id) {
        return tipoViaRepository.findById(id)
                .orElseThrow(() -> new ViaNotFoundException("Tipo de vía no encontrado con id: " + id));
    }

    public TipoVia save(TipoVia tipoVia) {
        return tipoViaRepository.save(tipoVia);
    }

    public void deleteById(Long id) {
        tipoViaRepository.deleteById(id);
    }

}
