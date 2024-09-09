package es.cic.curso.back.via;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.cic.curso.back.enums.NivelCongestion;
import es.cic.curso.back.exception.ViaNotFoundException;
import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.model.Via;
import es.cic.curso.back.repository.TipoViaRepository;
import es.cic.curso.back.repository.ViaRepository;
import es.cic.curso.back.service.ViaService;

@SpringBootTest
public class ViaServiceIT {

    @Autowired
    private ViaService viaService;

    @Autowired
    private ViaRepository viaRepository;

    @Autowired
    private TipoViaRepository tipoViaRepository;

    private TipoVia tipoVia;

    @BeforeEach
    public void setUp() {
        viaRepository.deleteAll();
        tipoViaRepository.deleteAll();

        tipoVia = TipoVia.builder()
                .tipo("Calle")
                .codigo("CL")
                .abreviatura("C.")
                .descripcion("Una calle común")
                .build();

        tipoVia = tipoViaRepository.save(tipoVia);
    }

    @Test
    public void testGetAllVias() {

        // Given
        Via via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();

        viaRepository.save(via);

        // When
        List<Via> vias = viaService.getAllVias();

        // Then
        assertThat(vias).hasSize(1);
    }

    @Test
    public void testGetViaById() {

        // Given
        Via via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();

        via = viaRepository.save(via);

        // When
        Via foundVia = viaService.getViaById(via.getId());

        // Then
        assertThat(foundVia).isNotNull();
        assertThat(foundVia.getNombre()).isEqualTo(via.getNombre());
    }

    @Test
    public void testCreateVia() {

        // Given
        Via via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();

        // When
        Via createdVia = viaService.createVia(via);

        // Then
        assertThat(createdVia).isNotNull();
        assertThat(createdVia.getId()).isNotNull();
    }

    @Test
    public void testUpdateVia() {

        // Given
        Via via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();

        via = viaRepository.save(via);

        Via viaDetails = Via.builder()
                .nombre("Calle Verdadera 456")
                .longitud(2.0)
                .ancho(6.0)
                .capacidad(200)
                .estado("En mantenimiento")
                .congestion(NivelCongestion.MEDIO)
                .tipo(tipoVia)
                .build();

        // When
        Via updatedVia = viaService.updateVia(via.getId(), viaDetails);

        // Then
        assertThat(updatedVia).isNotNull();
        assertThat(updatedVia.getNombre()).isEqualTo(viaDetails.getNombre());
    }

    @Test
    public void testDeleteVia() {

        // Given
        Via via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();

        via = viaRepository.save(via);

        // When
        viaService.deleteVia(via.getId());

        // Then
        Optional<Via> deletedVia = viaRepository.findById(via.getId());
        assertThat(deletedVia).isEmpty();
    }

    @Test
    public void testGetViaByIdNotFound() {
        assertThrows(ViaNotFoundException.class, () -> {
            viaService.getViaById(999L);
        });
    }

    @Test
    public void testUpdateViaNotFound() {

        // Given
        Via viaDetails = Via.builder()
                .nombre("Calle Verdadera 456")
                .longitud(2.0)
                .ancho(6.0)
                .capacidad(200)
                .estado("En mantenimiento")
                .congestion(NivelCongestion.MEDIO)
                .tipo(tipoVia)
                .build();

        // Then
        assertThrows(ViaNotFoundException.class, () -> {
            viaService.updateVia(999L, viaDetails);
        });
    }

    @Test
    public void testDeleteViaNotFound() {
        assertThrows(ViaNotFoundException.class, () -> {
            viaService.deleteVia(999L);
        });
    }
}