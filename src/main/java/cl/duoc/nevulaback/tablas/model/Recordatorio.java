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
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "recordatorios", itemRelation = "recordatorio")
@JsonPropertyOrder({"id", "usuario", "fechaCreacion"})
@Entity
@Table(name = "recordatorio")
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Recordatorio(Usuario usuario) {
        this.usuario = usuario;
        this.fechaCreacion = LocalDateTime.now(ZoneId.of("America/Santiago"));
    }
}