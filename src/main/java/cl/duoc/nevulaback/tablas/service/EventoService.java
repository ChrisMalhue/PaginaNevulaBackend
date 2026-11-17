package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Estado;
import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.model.Imagen;
import cl.duoc.nevulaback.tablas.model.Local;
import cl.duoc.nevulaback.tablas.repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class EventoService {

    private final EventoRepository repository;
    private final LocalService localService;
    private final EstadoService estadoService;
    private final ImagenService imagenService;
    private final Validacion validacion;

    public EventoService(EventoRepository repository, LocalService localService,
                        EstadoService estadoService, ImagenService imagenService,
                        Validacion validacion) {
        this.repository = repository;
        this.localService = localService;
        this.estadoService = estadoService;
        this.imagenService = imagenService;
        this.validacion = validacion;
    }

    public List<Evento> obtenerTodos(Long estadoId, Long localId) {
        log.info("Obteniendo eventos (estadoId: {}, localId: {})", estadoId, localId);
        if (estadoId != null) {
        return repository.findByEstadoIdOrderByFechaDesc(estadoId);
        }
        if (localId != null) {
        return repository.findByLocalIdOrderByFechaDesc(localId);
        }
        return repository.findAllByOrderByFechaDesc();
    }

    public Evento buscarPorId(Long id) {
        log.info("Buscando evento por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " no existe"));
    }

    /** La fecha no puede ser anterior a hoy, pero solo si el evento queda Programado. */
    public Evento guardar(Evento evento) {
        log.info("Registrando nuevo evento");
        evento.setId(null);
        evento.setLocal(resolverLocal(evento.getLocal()));
        evento.setEstado(resolverEstado(evento.getEstado()));
        evento.setImagen(resolverImagen(evento.getImagen()));
        if (esProgramado(evento.getEstado())) {
        validacion.fechaNoPasada(evento.getFecha());
        }
        return repository.save(evento);
    }

    /** La fecha se revisa solo si el evento queda Programado y cambió la fecha o el estado. */
    public Evento actualizar(Long id, Evento datos) {
        log.info("Actualizando el evento con ID: {}", id);
        Evento evento = buscarPorId(id);
        Estado estado = resolverEstado(datos.getEstado());

        boolean cambioFecha = !datos.getFecha().equals(evento.getFecha());
        boolean cambioEstado = !estado.getId().equals(evento.getEstado().getId());
        if (esProgramado(estado) && (cambioFecha || cambioEstado)) {
        validacion.fechaNoPasada(datos.getFecha());
        }

        evento.setFecha(datos.getFecha());
        evento.setHoraInicio(datos.getHoraInicio());
        evento.setHoraFin(datos.getHoraFin());
        evento.setLocal(resolverLocal(datos.getLocal()));
        evento.setEstado(estado);
        evento.setImagen(resolverImagen(datos.getImagen()));
        evento.setLinkEntradas(datos.getLinkEntradas());
        evento.setGenero(datos.getGenero());
        return repository.save(evento);
    }

    public Evento actualizarParcial(Long id, Evento datos) {
        log.info("Actualizando parcialmente el evento con ID: {}", id);
        Evento evento = buscarPorId(id);

        LocalDate fechaNueva = datos.getFecha() != null ? datos.getFecha() : evento.getFecha();
        Estado estadoNuevo = datos.getEstado() != null
            ? resolverEstado(datos.getEstado())
            : evento.getEstado();

        boolean cambioFecha = !fechaNueva.equals(evento.getFecha());
        boolean cambioEstado = !estadoNuevo.getId().equals(evento.getEstado().getId());
        if (esProgramado(estadoNuevo) && (cambioFecha || cambioEstado)) {
        validacion.fechaNoPasada(fechaNueva);
        }

        evento.setFecha(fechaNueva);
        evento.setEstado(estadoNuevo);
        if (datos.getHoraInicio() != null) evento.setHoraInicio(datos.getHoraInicio());
        if (datos.getHoraFin() != null) evento.setHoraFin(datos.getHoraFin());
        if (datos.getLocal() != null) evento.setLocal(resolverLocal(datos.getLocal()));
        if (datos.getImagen() != null) evento.setImagen(resolverImagen(datos.getImagen()));
        if (datos.getLinkEntradas() != null) evento.setLinkEntradas(datos.getLinkEntradas());
        if (datos.getGenero() != null) evento.setGenero(datos.getGenero());
        validacion.validar(evento);
        return repository.save(evento);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el evento con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si el evento tiene DJs o recordatorios asignados (409)
    }

    private boolean esProgramado(Estado estado) {
        return "Programado".equalsIgnoreCase(estado.getNombre());
    }

    private Local resolverLocal(Local local) {
        if (local == null || local.getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id del local");
        }
        return localService.buscarPorId(local.getId());
    }

    private Estado resolverEstado(Estado estado) {
        if (estado == null || estado.getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id del estado");
        }
        return estadoService.buscarPorId(estado.getId());
    }

    private Imagen resolverImagen(Imagen imagen) {
        if (imagen == null || imagen.getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la imagen");
        }
        return imagenService.buscarPorId(imagen.getId());
    }
}