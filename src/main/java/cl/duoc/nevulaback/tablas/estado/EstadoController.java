package cl.duoc.nevulaback.tablas.estado;

import cl.duoc.nevulaback.config.Validacion;
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

/** Rutas del recurso "estados". Todas empiezan con /api/estados. */
@RestController
@RequestMapping("/api/estados")
public class EstadoController {

  private final EstadoRepository repository;
  private final Validacion validacion;

  public EstadoController(EstadoRepository repository, Validacion validacion) {
    this.repository = repository;
    this.validacion = validacion;
  }

  /** GET /api/estados o GET /api/estados?nombre=prog */
  @GetMapping
  public List<Estado> listar(@RequestParam(required = false) String nombre) {
    if (nombre == null || nombre.isBlank()) {
      return repository.findAll();
    }
    return repository.findByNombreContainingIgnoreCase(nombre);
  }

  @GetMapping("/{id}")
  public Estado obtener(@PathVariable Long id) {
    return buscar(id);
  }

  @PostMapping
  public ResponseEntity<Estado> crear(@Valid @RequestBody Estado estado) {
    estado.setId(null);
    Estado guardado = repository.save(estado);
    return ResponseEntity.created(URI.create("/api/estados/" + guardado.getId())).body(guardado);
  }

  @PutMapping("/{id}")
  public Estado actualizar(@PathVariable Long id, @Valid @RequestBody Estado datos) {
    Estado estado = buscar(id);
    estado.setNombre(datos.getNombre());
    return repository.save(estado);
  }

  @PatchMapping("/{id}")
  public Estado modificar(@PathVariable Long id, @RequestBody Estado datos) {
    Estado estado = buscar(id);
    if (datos.getNombre() != null) estado.setNombre(datos.getNombre());
    validacion.validar(estado);
    return repository.save(estado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    repository.delete(buscar(id));
    return ResponseEntity.noContent().build();
  }

  private Estado buscar(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado " + id + " no existe"));
  }
}