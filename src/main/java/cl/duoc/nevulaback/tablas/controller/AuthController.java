package cl.duoc.nevulaback.tablas.controller;

import cl.duoc.nevulaback.config.JwtService;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;
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
@Tag(name = "Autenticación", description = "Inicio de sesión y consulta de perfil autenticado")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena
    ) {}

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida correo y contraseña y devuelve un token JWT")
    public ResponseEntity> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.correo().trim())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.contrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("token", token);
        respuesta.put("tipo", "Bearer");
        respuesta.put("usuario", usuario);

        return ResponseEntity.ok(respuesta);
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