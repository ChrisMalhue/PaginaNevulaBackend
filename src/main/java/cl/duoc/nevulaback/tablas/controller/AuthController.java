package cl.duoc.nevulaback.tablas.controller;

import cl.duoc.nevulaback.config.JwtService;
import cl.duoc.nevulaback.tablas.dto.UsuarioDTO;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.UsuarioRepository;
import cl.duoc.nevulaback.tablas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, inicio de sesión y consulta de perfil autenticado")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        private String correo;

        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegistroRequest {

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
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String tipo;
        private Usuario usuario;
    }

    @PostMapping({"/registro", "/register"})
    @Operation(summary = "Registrar nuevo usuario",
        description = "Crea una cuenta con rol USUARIO por defecto, la guarda en BD y devuelve su token JWT")
    public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuario(request.getNombreUsuario());
        dto.setCorreo(request.getCorreo());
        dto.setContrasena(request.getContrasena());
        // rolId queda null -> UsuarioService asigna automáticamente ROL_POR_DEFECTO ("USUARIO")

        Usuario creado = usuarioService.guardar(dto);
        String token = jwtService.generarToken(creado);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new LoginResponse(token, "Bearer", creado));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida correo y contraseña y devuelve un token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo().trim().toLowerCase())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario);
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", usuario));
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener usuario autenticado", description = "Devuelve los datos del usuario dueño del token")
    public ResponseEntity<Usuario> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return ResponseEntity.ok(usuario);
    }
}
