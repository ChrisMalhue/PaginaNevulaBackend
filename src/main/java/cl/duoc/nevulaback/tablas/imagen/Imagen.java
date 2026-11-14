package cl.duoc.nevulaback.tablas.imagen;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"id", "url", "descripcion"})
@Entity
@Table(name = "imagenes")
public class Imagen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Link público de la imagen (el que devuelve ImgBB, ya convertida a WebP desde React). */
  @NotBlank(message = "La URL es obligatoria")
  @Size(max = 500, message = "La URL puede tener como máximo 500 caracteres")
  @Pattern(regexp = "^https://.*", message = "La URL debe empezar con https://")
  @Column(nullable = false, unique = true, length = 500)
  private String url;

  /** Opcional: texto alternativo para la imagen. */
  @Size(max = 200, message = "La descripción puede tener como máximo 200 caracteres")
  @Column(length = 200)
  private String descripcion;

  public Imagen() {
  }

  public Imagen(String url, String descripcion) {
    this.url = url;
    this.descripcion = descripcion;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }

  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}