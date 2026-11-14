package cl.duoc.nevulaback.tablas.evento;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.estado.Estado;
import cl.duoc.nevulaback.tablas.estado.EstadoRepository;
import cl.duoc.nevulaback.tablas.imagen.Imagen;
import cl.duoc.nevulaback.tablas.imagen.ImagenRepository;
import cl.duoc.nevulaback.tablas.local.Local;
import cl.duoc.nevulaback.tablas.local.LocalRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rutas del recurso "eventos". Todas empiezan con /api/eventos.
 * Filtros: ?estadoId=1 o ?localId=2. Sin filtro, del más reciente al más antiguo.
 */
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoRepository repository;
    private final LocalRepository localRepository;
    private final EstadoRepository estadoRepository;
    private final ImagenRepository imagenRepository;
    private final Validacion validacion;

    public EventoController(EventoRepository repository, LocalRepository localRepository,
                            EstadoRepository estadoRepository, ImagenRepository imagenRepository,
                            Validacion validacion) {
        this.repository = repository;
        this.localRepository = localRepository;
        this.estadoRepository = estadoRepository;
        this.imagenRepository = imagenRepository;
        this.validacion = validacion;
    }

    /** GET /api/eventos, ?estadoId=1 o ?localId=2 */
    @GetMapping
    public List<Evento> listar(@RequestParam(required = false) Long estadoId,
                                @RequestParam(required = false) Long localId) {
        if (estadoId != null) {
        return repository.findByEstadoIdOrderByFechaDesc(estadoId);
        }
        if (localId != null) {
        return repository.findByLocalIdOrderByFechaDesc(localId);
        }
        return repository.findAllByOrderByFechaDesc();
    }

    /** GET /api/eventos/5 -> 200, o 404 si no existe. */
    @GetMapping("/{id}")
    public Evento obtener(@PathVariable Long id) {
        return buscar(id);
    }

    /** POST /api/eventos -> 201. La fecha no puede ser anterior a hoy. */
    @PostMapping
    public ResponseEntity<Evento> crear(@Valid @RequestBody Evento evento) {
        validacion.fechaNoPasada(evento.getFecha());
        evento.setId(null);
        evento.setLocal(buscarLocal(evento.getLocal().getId()));
        evento.setEstado(buscarEstado(evento.getEstado().getId()));
        evento.setImagen(buscarImagen(evento.getImagen().getId()));
        Evento guardado = repository.save(evento);
        return ResponseEntity.created(URI.create("/api/eventos/" + guardado.getId())).body(guardado);
    }

    /** PUT /api/eventos/5 -> reemplaza el evento COMPLETO. Solo revisa la fecha si cambió. */
    @PutMapping("/{id}")
    public Evento actualizar(@PathVariable Long id, @Valid @RequestBody Evento datos) {
        Evento evento = buscar(id);
        if (!datos.getFecha().equals(evento.getFecha())) {
        validacion.fechaNoPasada(datos.getFecha());
        }
        evento.setFecha(datos.getFecha());
        evento.setHoraInicio(datos.getHoraInicio());
        evento.setHoraFin(datos.getHoraFin());
        evento.setLocal(buscarLocal(datos.getLocal().getId()));
        evento.setEstado(buscarEstado(datos.getEstado().getId()));
        evento.setImagen(buscarImagen(datos.getImagen().getId()));
        return repository.save(evento);
    }

    /** PATCH /api/eventos/5 -> cambia solo lo enviado (por ejemplo, el estado). */
    @PatchMapping("/{id}")
    public Evento modificar(@PathVariable Long id, @RequestBody Evento datos) {
        Evento evento = buscar(id);
        if (datos.getFecha() != null) {
        if (!datos.getFecha().equals(evento.getFecha())) {
            validacion.fechaNoPasada(datos.getFecha());
        }
        evento.setFecha(datos.getFecha());
        }
        if (datos.getHoraInicio() != null) evento.setHoraInicio(datos.getHoraInicio());
        if (datos.getHoraFin() != null) evento.setHoraFin(datos.getHoraFin());
        if (datos.getLocal() != null) evento.setLocal(buscarLocal(datos.getLocal().getId()));
        if (datos.getEstado() != null) evento.setEstado(buscarEstado(datos.getEstado().getId()));
        if (datos.getImagen() != null) evento.setImagen(buscarImagen(datos.getImagen().getId()));
        validacion.validar(evento);
        return repository.save(evento);
    }

    /** DELETE /api/eventos/5 -> 204. Si tiene DJs asignados, responde 409. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Evento buscar(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " no existe"));
    }

    private Local buscarLocal(Long id) {
        if (id == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id del local");
        }
        return localRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local " + id + " no existe"));
    }

    private Estado buscarEstado(Long id) {
        if (id == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id del estado");
        }
        return estadoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado " + id + " no existe"));
    }

    private Imagen buscarImagen(Long id) {
        if (id == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la imagen");
        }
        return imagenRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen " + id + " no existe"));
    }
}