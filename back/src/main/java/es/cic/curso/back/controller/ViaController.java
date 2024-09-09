package es.cic.curso.back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso.back.model.Via;
import es.cic.curso.back.service.ViaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vias")
@Validated
public class ViaController {

    @Autowired
    private ViaService viaService;

    @GetMapping
    public List<Via> getAllVias() {
        return viaService.getAllVias();
    }

    @GetMapping("/{id}")
    public Via getViaById(@PathVariable Long id) {
        Via via = viaService.getViaById(id);
        return via;
    }

    @PostMapping
    public Via createVia(@Valid @RequestBody Via via) {
        Via nuevaVia = viaService.createVia(via);
        return nuevaVia;
    }

    @PutMapping("/{id}")
    public Via updateVia(@PathVariable Long id, @Valid @RequestBody Via viaDetails) {
        Via viaActualizada = viaService.updateVia(id, viaDetails);
        return viaActualizada;
    }

    @DeleteMapping("/{id}")
    public void deleteVia(@PathVariable Long id) {
        viaService.deleteVia(id);
    }
}