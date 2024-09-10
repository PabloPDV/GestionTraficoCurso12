package es.cic.curso.back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.service.TipoViaService;

@RestController
@RequestMapping("/api/tipovia")
public class TipoViaController {

    @Autowired
    private TipoViaService tipoViaService;

    @GetMapping
    public List<TipoVia> getAllTipoVias() {
        return tipoViaService.findAll();
    }

    @GetMapping("/{id}")
    public TipoVia getTipoViaById(@PathVariable Long id) {
        return tipoViaService.findById(id);
    }

    @PostMapping
    public TipoVia createTipoVia(@RequestBody TipoVia tipoVia) {
        return tipoViaService.save(tipoVia);
    }

    @PutMapping("/{id}")
    public TipoVia updateTipoVia(@PathVariable Long id, @RequestBody TipoVia tipoViaDetails) {

        TipoVia tipoVia = tipoViaService.findById(id);

        TipoVia updatedTipoVia = tipoVia;
        updatedTipoVia.setTipo(tipoViaDetails.getTipo());
        updatedTipoVia.setCodigo(tipoViaDetails.getCodigo());
        updatedTipoVia.setAbreviatura(tipoViaDetails.getAbreviatura());
        updatedTipoVia.setDescripcion(tipoViaDetails.getDescripcion());
        tipoViaService.save(updatedTipoVia);

        return updatedTipoVia;

    }

    @DeleteMapping("/{id}")
    public void deleteTipoVia(@PathVariable Long id) {
        tipoViaService.deleteById(id);
    }
}