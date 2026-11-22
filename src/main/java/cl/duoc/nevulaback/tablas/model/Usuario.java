package cl.duoc.nevulaback.tablas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Relation(collectionRelation = "usuarios", itemRelation = "usuario")
@JsonPropertyOrder({"id", "nombreUsuario", "correo", "rol"})
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario puede tener como máximo 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String nombreUsuario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo puede tener como máximo 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    // Guarda el hash BCrypt, nunca la contraseña original. No sale en ninguna respuesta.
    @JsonIgnore
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 100)
    private String contrasena;

    @NotNull(message = "El rol es obligatorio")
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Usuario(String nombreUsuario, String correo, String contrasena, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
  }
}