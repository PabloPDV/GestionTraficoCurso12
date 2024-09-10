package es.cic.curso.back.via;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso.back.controller.ViaController;
import es.cic.curso.back.enums.NivelCongestion;
import es.cic.curso.back.model.TipoVia;
import es.cic.curso.back.model.Via;
import es.cic.curso.back.service.ViaService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ViaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ViaService viaService;

    @InjectMocks
    private ViaController viaController;

    @Autowired
    private ObjectMapper objectMapper;

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
                .id(1L) // Simulate that TipoVia is already persisted
                .build();

        via = Via.builder()
                .nombre("Calle Falsa 123")
                .longitud(1.2)
                .ancho(5.0)
                .capacidad(100)
                .estado("Abierta")
                .congestion(NivelCongestion.BAJO)
                .tipo(tipoVia)
                .id(1L) // Simulate that Via is already persisted
                .build();
    }

    @Test
    public void testGetViaById() throws Exception {
        when(viaService.getViaById(1L)).thenReturn(via);

        mockMvc.perform(get("/api/vias/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testCreateVia() throws Exception {
        when(viaService.createVia(any(Via.class))).thenAnswer(invocation -> {
            Via viaToSave = invocation.getArgument(0);
            viaToSave.setId(1L); // Simulate persisting Via
            return viaToSave;
        });

        mockMvc.perform(post("/api/vias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(via)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testUpdateVia() throws Exception {
        when(viaService.updateVia(anyLong(), any(Via.class))).thenReturn(via);

        mockMvc.perform(put("/api/vias/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(via)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calle Falsa 123"));
    }

    @Test
    public void testDeleteVia() throws Exception {
        mockMvc.perform(delete("/api/vias/{id}", 1L))
                .andExpect(status().isOk());
    }
}