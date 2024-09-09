package es.cic.curso.back.model;

import es.cic.curso.back.enums.NivelCongestion;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Via {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Min(value = 0, message = "La longitud debe ser un valor positivo")
    private double longitud;

    @Min(value = 0, message = "El ancho debe ser un valor positivo")
    private double ancho;

    @Min(value = 0, message = "La capacidad debe ser un valor positivo")
    private int capacidad;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "El nivel de congestión no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private NivelCongestion nivelCongestion;

    @ManyToOne
    @NotNull(message = "El tipo de vía no puede ser nulo")
    private TipoVia tipo;
}