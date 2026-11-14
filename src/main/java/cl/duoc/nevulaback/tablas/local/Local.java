package cl.duoc.nevulaback.tablas.local;

import cl.duoc.nevulaback.tablas.comuna.Comuna;
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

@JsonPropertyOrder({"id", "nombre", "direccion", "mapaUrl", "comuna"})
@Entity
@Table(name = "locales")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre puede tener como máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Opcional: los locales secretos no publican su dirección. */
    @Size(max = 200, message = "La dirección puede tener como máximo 200 caracteres")
    @Column(length = 200)
    private String direccion;

    /** Opcional: link de Google Maps. Vacío en los locales secretos. */
    @Size(max = 500, message = "El link del mapa puede tener como máximo 500 caracteres")
    @Pattern(regexp = "^https://.*", message = "El link del mapa debe empezar con https://")
    @Column(length = 500)
    private String mapaUrl;

    @NotNull(message = "La comuna es obligatoria")
    @ManyToOne
    @JoinColumn(name = "comuna_id", nullable = false)
    private Comuna comuna;

    public Local() {
    }

    public Local(String nombre, String direccion, String mapaUrl, Comuna comuna) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.mapaUrl = mapaUrl;
        this.comuna = comuna;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMapaUrl() { return mapaUrl; }
    public void setMapaUrl(String mapaUrl) { this.mapaUrl = mapaUrl; }

    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }
}