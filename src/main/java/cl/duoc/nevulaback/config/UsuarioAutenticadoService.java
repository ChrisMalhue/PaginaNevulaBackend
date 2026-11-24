package cl.duoc.nevulaback.config;

import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión");
        }

        return usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));
    }

    public boolean esAdmin(Usuario usuario) {
        return usuario.getRol() != null && "ADMIN".equalsIgnoreCase(usuario.getRol().getNombre());
    }

    public void validarPropietarioOAdmin(Usuario dueno) {
        Usuario actual = obtenerUsuarioActual();
        if (!esAdmin(actual) && !actual.getId().equals(dueno.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "No tienes permisos sobre este recurso");
        }
    }
}