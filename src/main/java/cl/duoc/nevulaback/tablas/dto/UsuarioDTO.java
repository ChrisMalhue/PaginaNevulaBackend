package cl.duoc.nevulaback.tablas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(max = 50, message = "El nombre de usuario puede tener como máximo 50 caracteres")
  private String nombreUsuario;

  @NotBlank(message = "El correo es obligatorio")
  @Email(message = "El correo no tiene un formato válido")
  @Size(max = 100, message = "El correo puede tener como máximo 100 caracteres")
  private String correo;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
  private String contrasena;

  // Opcional: si no se envía, el usuario nuevo queda con rol USUARIO.
  private Long rolId;
}