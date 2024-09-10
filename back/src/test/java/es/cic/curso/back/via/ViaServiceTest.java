package es.cic.curso.back.via;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.cic.curso.back.enums.NivelCongestion;
import es.cic.curso.back.exception.ViaNotFoundException;
import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.model.Via;
import es.cic.curso.back.repository.ViaRepository;
import es.cic.curso.back.service.ViaService;

public class ViaServiceTest {

    @Mock
    private ViaRepository viaRepository;

    @InjectMocks
    private ViaService viaService;

    private Via via;
    private TipoVia tipoVia;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tipoVia = TipoVia.builder()
                .tipo("Calle")
                .codigo("CL")
                .abreviatura("C.")
                .descripcion("Una calle común")
                .build();
        via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .build();
    }

    @Test
    public void testGetAllVias() {
        when(viaRepository.findAll()).thenReturn(Arrays.asList(via));

        List<Via> vias = viaService.getAllVias();
        assertThat(vias).hasSize(1);
        assertThat(vias.get(0).getNombre()).isEqualTo("Calle Falsa 123");
    }

    @Test
    public void testGetViaById() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.of(via));

        Via foundVia = viaService.getViaById(via.getId());
        assertThat(foundVia).isNotNull();
        assertThat(foundVia.getNombre()).isEqualTo("Calle Falsa 123");
    }

    @Test
    public void testGetViaByIdNotFound() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.empty());

        assertThrows(ViaNotFoundException.class, () -> {
            viaService.getViaById(via.getId());
        });
    }

    @Test
    public void testCreateVia() {
        when(viaRepository.save(any(Via.class))).thenReturn(via);

        Via createdVia = viaService.createVia(via);
        assertThat(createdVia).isNotNull();
        assertThat(createdVia.getNombre()).isEqualTo("Calle Falsa 123");
    }

    @Test
    public void testUpdateVia() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.of(via));
        when(viaRepository.save(any(Via.class))).thenReturn(via);

        Via viaDetails = Via.builder()
                .nombre("Calle Verdadera 456")
                .longitud(2.0)
                .ancho(6.0)
                .capacidad(200)
                .estado("En mantenimiento")
                .congestion(NivelCongestion.MEDIO)
                .tipo(tipoVia)
                .build();

        Via updatedVia = viaService.updateVia(via.getId(), viaDetails);
        assertThat(updatedVia).isNotNull();
        assertThat(updatedVia.getNombre()).isEqualTo("Calle Verdadera 456");
    }

    @Test
    public void testUpdateViaNotFound() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.empty());

        Via viaDetails = Via.builder()
                .nombre("Calle Verdadera 456")
                .longitud(2.0)
                .ancho(6.0)
                .capacidad(200)
                .estado("En mantenimiento")
                .congestion(NivelCongestion.MEDIO)
                .tipo(tipoVia)
                .build();

        assertThrows(ViaNotFoundException.class, () -> {
            viaService.updateVia(via.getId(), viaDetails);
        });
    }

    @Test
    public void testDeleteVia() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.of(via));
        doNothing().when(viaRepository).delete(via);

        viaService.deleteVia(via.getId());
        verify(viaRepository, times(1)).delete(via);
    }

    @Test
    public void testDeleteViaNotFound() {
        when(viaRepository.findById(via.getId())).thenReturn(Optional.empty());

        assertThrows(ViaNotFoundException.class, () -> {
            viaService.deleteVia(via.getId());
        });
    }
}