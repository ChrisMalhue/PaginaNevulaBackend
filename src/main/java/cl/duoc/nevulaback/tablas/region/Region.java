package cl.duoc.nevulaback.tablas.region;

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
@Relation(collectionRelation = "regiones", itemRelation = "region")
@JsonPropertyOrder({"id", "nombre"})
@Entity
@Table(name = "regiones")
public class Region {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 100, message = "El nombre puede tener como máximo 100 caracteres")
  @Column(nullable = false, length = 100)
  private String nombre;

  public Region(String nombre) {
    this.nombre = nombre;
  }
}