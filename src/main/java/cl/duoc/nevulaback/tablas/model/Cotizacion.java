package cl.duoc.nevulaback.tablas.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "cotizaciones", itemRelation = "cotizacion")
@JsonPropertyOrder({"id", "nombre", "telefono", "fechaEvento", "descripcion", "fechaCreacion", "usuario"})
@Entity
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre puede tener como máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+569\\d{8}$", message = "El teléfono debe tener el formato +56912345678")
    @Column(nullable = false, length = 12)
    private String telefono;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaEvento;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción puede tener como máximo 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    public Cotizacion(String nombre, String telefono, LocalDate fechaEvento,
        String descripcion, Usuario usuario) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaEvento = fechaEvento;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.fechaCreacion = LocalDateTime.now(ZoneId.of("America/Santiago"));
    }
}