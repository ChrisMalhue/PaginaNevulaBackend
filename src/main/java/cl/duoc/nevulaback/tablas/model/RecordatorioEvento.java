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
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

// Esta es la tabla intermedia entre recordatorio y evento, no confundirse!!!
@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "recordatoriosEvento", itemRelation = "recordatorioEvento")
@JsonPropertyOrder({"id", "recordatorio", "evento", "fechaCreacion"})
@Entity
@Table(name = "recordatorios",
  uniqueConstraints = @UniqueConstraint(columnNames = {"recordatorio_id", "evento_id"}))
public class RecordatorioEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El recordatorio es obligatorio")
    @ManyToOne
    @JoinColumn(name = "recordatorio_id", nullable = false)
    private Recordatorio recordatorio;

    @NotNull(message = "El evento es obligatorio")
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public RecordatorioEvento(Recordatorio recordatorio, Evento evento) {
        this.recordatorio = recordatorio;
        this.evento = evento;
        this.fechaCreacion = LocalDateTime.now(ZoneId.of("America/Santiago"));
    }
}