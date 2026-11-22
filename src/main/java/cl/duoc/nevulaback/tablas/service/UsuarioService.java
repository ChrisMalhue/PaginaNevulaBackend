package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.dto.UsuarioDTO;
import cl.duoc.nevulaback.tablas.model.Rol;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.UsuarioRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class UsuarioService {

    private static final String ROL_POR_DEFECTO = "USUARIO";

    private final UsuarioRepository repository;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;
    private final Validacion validacion;

    public UsuarioService(UsuarioRepository repository, RolService rolService,
        PasswordEncoder passwordEncoder, Validacion validacion) {
        this.repository = repository;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
        this.validacion = validacion;
    }

    public List<Usuario> obtenerTodos(String nombreUsuario, Long rolId) {
        log.info("Obteniendo usuarios (nombreUsuario: {}, rolId: {})", nombreUsuario, rolId);

        boolean hayNombre = nombreUsuario != null && !nombreUsuario.isBlank();

        if (hayNombre && rolId != null) {
        return repository.findByNombreUsuarioContainingIgnoreCaseAndRolId(nombreUsuario, rolId);
        }
        if (hayNombre) {
        return repository.findByNombreUsuarioContainingIgnoreCase(nombreUsuario);
        }
        if (rolId != null) {
        return repository.findByRolId(rolId);
        }
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("Buscando usuario por ID: {}", id);
        return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Usuario " + id + " no existe"));
    }

    public Usuario guardar(UsuarioDTO datos) {
        log.info("Registrando nuevo usuario");

        Rol rol = datos.getRolId() != null
        ? rolService.buscarPorId(datos.getRolId())
        : rolService.buscarPorNombre(ROL_POR_DEFECTO);

        Usuario usuario = new Usuario(
        datos.getNombreUsuario().trim(),
        normalizarCorreo(datos.getCorreo()),
        passwordEncoder.encode(datos.getContrasena()),
        rol);

        return repository.saveAndFlush(usuario);
    }

    public Usuario actualizar(Long id, UsuarioDTO datos) {
        log.info("Actualizando el usuario con ID: {}", id);
        Usuario usuario = buscarPorId(id);

        usuario.setNombreUsuario(datos.getNombreUsuario().trim());
        usuario.setCorreo(normalizarCorreo(datos.getCorreo()));
        usuario.setContrasena(passwordEncoder.encode(datos.getContrasena()));

        // Si no se envía rolId, conserva el rol actual (así un PUT no baja de rango a un admin).
        if (datos.getRolId() != null) {
        usuario.setRol(rolService.buscarPorId(datos.getRolId()));
        }

        return repository.saveAndFlush(usuario);
    }

    public Usuario actualizarParcial(Long id, UsuarioDTO datos) {
        log.info("Actualizando parcialmente el usuario con ID: {}", id);
        Usuario usuario = buscarPorId(id);

        if (datos.getNombreUsuario() != null) {
        usuario.setNombreUsuario(datos.getNombreUsuario().trim());
        }
        if (datos.getCorreo() != null) {
        usuario.setCorreo(normalizarCorreo(datos.getCorreo()));
        }
        if (datos.getContrasena() != null) {
        validarContrasena(datos.getContrasena());
        usuario.setContrasena(passwordEncoder.encode(datos.getContrasena()));
        }
        if (datos.getRolId() != null) {
        usuario.setRol(rolService.buscarPorId(datos.getRolId()));
        }

        validacion.validar(usuario);
        return repository.saveAndFlush(usuario);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el usuario con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }

    // En el PATCH no corre @Valid, por eso la regla de la contraseña se revisa aquí.
    private void validarContrasena(String contrasena) {
        if (contrasena.length() < 8 || contrasena.length() > 72) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "La contraseña debe tener entre 8 y 72 caracteres");
        }
    }
}