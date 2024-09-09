package es.cic.curso.back.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoVia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo debe tener entre 3 y 50 caracteres")
    private String tipo;

    @NotBlank(message = "El código no puede estar vacío")
    @Size(min = 1, max = 10, message = "El código debe tener entre 1 y 10 caracteres")
    private String codigo;

    @Size(max = 10, message = "La abreviatura no puede tener más de 10 caracteres")
    private String abreviatura;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    @OneToMany(mappedBy = "tipo")
    private List<Via> vias;
}
