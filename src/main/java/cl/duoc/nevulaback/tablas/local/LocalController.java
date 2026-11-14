package cl.duoc.nevulaback.tablas.local;

import cl.duoc.nevulaback.tablas.comuna.Comuna;
import cl.duoc.nevulaback.tablas.comuna.ComunaRepository;
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
import cl.duoc.nevulaback.config.Validacion;

/** Rutas del recurso "locales". Todas empiezan con /api/locales. */
@RestController
@RequestMapping("/api/locales")
public class LocalController {

    private final LocalRepository repository;
    private final ComunaRepository comunaRepository;
    private final Validacion validacion;

    public LocalController(LocalRepository repository, ComunaRepository comunaRepository, Validacion validacion) {
        this.repository = repository;
        this.comunaRepository = comunaRepository;
        this.validacion = validacion;
    }

    /** GET /api/locales, ?nombre=secret o ?comunaId=3 */
    @GetMapping
    public List<Local> listar(@RequestParam(required = false) String nombre,
                                @RequestParam(required = false) Long comunaId) {
        if (comunaId != null) {
        return repository.findByComunaId(comunaId);
        }
        if (nombre != null && !nombre.isBlank()) {
        return repository.findByNombreContainingIgnoreCase(nombre);
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Local obtener(@PathVariable Long id) {
        return buscar(id);
    }

    /** POST /api/locales -> 201 Created. La comuna debe existir (si no, 404). */
    @PostMapping
    public ResponseEntity<Local> crear(@Valid @RequestBody Local local) {
        local.setId(null);
        local.setComuna(buscarComuna(local.getComuna().getId()));
        Local guardado = repository.save(local);
        return ResponseEntity.created(URI.create("/api/locales/" + guardado.getId())).body(guardado);
    }

    /** PUT /api/locales/5 -> reemplaza el local COMPLETO. */
    @PutMapping("/{id}")
    public Local actualizar(@PathVariable Long id, @Valid @RequestBody Local datos) {
        Local local = buscar(id);
        local.setNombre(datos.getNombre());
        local.setDireccion(datos.getDireccion());
        local.setMapaUrl(datos.getMapaUrl());
        local.setComuna(buscarComuna(datos.getComuna().getId()));
        return repository.save(local);
    }

    /** PATCH /api/locales/5 -> cambia solo los campos enviados. */
    @PatchMapping("/{id}")
    public Local modificar(@PathVariable Long id, @RequestBody Local datos) {
        Local local = buscar(id);
        if (datos.getNombre() != null) local.setNombre(datos.getNombre());
        if (datos.getDireccion() != null) local.setDireccion(datos.getDireccion());
        if (datos.getMapaUrl() != null) local.setMapaUrl(datos.getMapaUrl());
        if (datos.getComuna() != null) local.setComuna(buscarComuna(datos.getComuna().getId()));
        validacion.validar(local);
        return repository.save(local);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    // Duda
    private Local buscar(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local " + id + " no existe"));
    }

    private Comuna buscarComuna(Long id) {
        if (id == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la comuna");
        }
        return comunaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna " + id + " no existe"));
    }
}