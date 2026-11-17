package cl.duoc.nevulaback.tablas.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "estados", itemRelation = "estado")
@JsonPropertyOrder({"id", "nombre"})
@Entity
@Table(name = "estados")
public class Estado {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 50, message = "El nombre puede tener como máximo 50 caracteres")
  @Column(nullable = false, unique = true, length = 50)
  private String nombre;

  public Estado(String nombre) {
    this.nombre = nombre;
  }
}