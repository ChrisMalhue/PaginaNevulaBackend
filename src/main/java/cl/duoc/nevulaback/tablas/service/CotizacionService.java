package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.UsuarioAutenticadoService;
import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Cotizacion;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.CotizacionRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class CotizacionService {

    private static final String ROL_COTIZANTE = "USUARIO";

    private final CotizacionRepository repository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final Validacion validacion;

    public CotizacionService(
            CotizacionRepository repository,
            UsuarioAutenticadoService usuarioAutenticadoService,
            Validacion validacion) {
        this.repository = repository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.validacion = validacion;
    }

    public List<Cotizacion> obtenerTodos(Long usuarioId) {
        Usuario actual = usuarioAutenticadoService.obtenerUsuarioActual();
        log.info("Obteniendo cotizaciones (solicitante: {}, filtro usuarioId: {})", actual.getId(), usuarioId);

        // Si no es ADMIN, solo puede ver sus propias cotizaciones
        if (!usuarioAutenticadoService.esAdmin(actual)) {
            return repository.findByUsuarioIdOrderByFechaCreacionDesc(actual.getId());
        }

        if (usuarioId != null) {
            return repository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        }

        return repository.findAllByOrderByFechaCreacionDesc();
    }

    public Cotizacion buscarPorId(Long id) {
        log.info("Buscando cotización por ID: {}", id);
        Cotizacion cotizacion = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Cotización " + id + " no existe"));

        usuarioAutenticadoService.validarPropietarioOAdmin(cotizacion.getUsuario());
        return cotizacion;
    }

    public Cotizacion guardar(Cotizacion datos) {
        log.info("Registrando nueva cotización");
        Usuario usuario = usuarioAutenticadoService.obtenerUsuarioActual();

        if (!ROL_COTIZANTE.equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Solo los usuarios con rol " + ROL_COTIZANTE + " pueden cotizar");
        }

        validacion.fechaNoPasada(datos.getFechaEvento());

        Cotizacion cotizacion = new Cotizacion(
            datos.getNombre().trim(),
            datos.getTelefono(),
            datos.getFechaEvento(),
            datos.getDescripcion().trim(),
            usuario);

        return repository.saveAndFlush(cotizacion);
    }

    public Cotizacion actualizar(Long id, Cotizacion datos) {
        log.info("Actualizando la cotización con ID: {}", id);
        Cotizacion cotizacion = buscarPorId(id);

        if (!datos.getFechaEvento().equals(cotizacion.getFechaEvento())) {
            validacion.fechaNoPasada(datos.getFechaEvento());
        }

        cotizacion.setNombre(datos.getNombre().trim());
        cotizacion.setTelefono(datos.getTelefono());
        cotizacion.setFechaEvento(datos.getFechaEvento());
        cotizacion.setDescripcion(datos.getDescripcion().trim());

        return repository.saveAndFlush(cotizacion);
    }

    public Cotizacion actualizarParcial(Long id, Cotizacion datos) {
        log.info("Actualizando parcialmente la cotización con ID: {}", id);
        Cotizacion cotizacion = buscarPorId(id);

        if (datos.getNombre() != null) {
            cotizacion.setNombre(datos.getNombre().trim());
        }
        if (datos.getTelefono() != null) {
            cotizacion.setTelefono(datos.getTelefono());
        }
        if (datos.getFechaEvento() != null) {
            if (!datos.getFechaEvento().equals(cotizacion.getFechaEvento())) {
                validacion.fechaNoPasada(datos.getFechaEvento());
            }
            cotizacion.setFechaEvento(datos.getFechaEvento());
        }
        if (datos.getDescripcion() != null) {
            cotizacion.setDescripcion(datos.getDescripcion().trim());
        }

        validacion.validar(cotizacion);
        return repository.saveAndFlush(cotizacion);
    }

    public void eliminar(Long id) {
        log.info("Eliminando la cotización con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}