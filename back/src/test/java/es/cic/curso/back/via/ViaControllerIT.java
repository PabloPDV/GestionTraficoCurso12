package es.cic.curso.back.via;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso.back.enums.NivelCongestion;
import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.model.Via;
import es.cic.curso.back.repository.TipoViaRepository;
import es.cic.curso.back.repository.ViaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ViaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ViaRepository viaRepository;

    @Autowired
    private TipoViaRepository tipoViaRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testGetAllVias() throws Exception {

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

        // Then
        mockMvc.perform(get("/api/vias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testGetViaById() throws Exception {

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

        // Then
        mockMvc.perform(get("/api/vias/{id}", via.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testCreateVia() throws Exception {

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

        // Then
        mockMvc.perform(post("/api/vias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(via)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testUpdateVia() throws Exception {

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

        // Then
        mockMvc.perform(put("/api/vias/{id}", via.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(viaDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calle Verdadera 456"));
    }

    @Test
    public void testDeleteVia() throws Exception {

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

        // Then
        mockMvc.perform(delete("/api/vias/{id}", via.getId()))
                .andExpect(status().isOk());
    }
}