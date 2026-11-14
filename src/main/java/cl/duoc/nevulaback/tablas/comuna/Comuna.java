package cl.duoc.nevulaback.tablas.comuna;

import cl.duoc.nevulaback.tablas.region.Region;
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
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({"id", "nombre", "region"})
@Entity
@Table(name = "comunas")
public class Comuna {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 100, message = "El nombre puede tener como máximo 100 caracteres")
  @Column(nullable = false, length = 100)
  private String nombre;

  @NotNull(message = "La región es obligatoria")
  @ManyToOne
  @JoinColumn(name = "region_id", nullable = false)
  private Region region;

  public Comuna() {
  }

  public Comuna(String nombre, Region region) {
    this.nombre = nombre;
    this.region = region;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public Region getRegion() { return region; }
  public void setRegion(Region region) { this.region = region; }
}