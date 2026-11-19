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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "djsEvento", itemRelation = "djEvento")
@JsonPropertyOrder({"id", "evento", "dj", "orden"})
@Entity
@Table(name = "djs",
uniqueConstraints = @UniqueConstraint(columnNames = {"evento_id", "dj_id"}))
public class EventoDj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El evento es obligatorio")
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @NotNull(message = "El dj es obligatorio")
    @ManyToOne
    @JoinColumn(name = "dj_id", nullable = false)
    private Dj dj;

    @NotNull(message = "El orden es obligatorio")
    @Min(value = 1, message = "El orden debe ser 1 o mayor")
    @Column(nullable = false)
    private Integer orden;

    public EventoDj(Evento evento, Dj dj, Integer orden) {
        this.evento = evento;
        this.dj = dj;
        this.orden = orden;
    }
}