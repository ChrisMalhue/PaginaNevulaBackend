package cl.duoc.nevulaback.tablas.region;

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

/** Rutas del recurso "regiones". Todas empiezan con /api/regiones. */
@RestController
@RequestMapping("/api/regiones")
public class RegionController {

  private final RegionRepository repository;
  private final Validacion validacion;


  public RegionController(RegionRepository repository, Validacion validacion) {
    this.repository = repository;
    this.validacion = validacion;
  }

  /** GET /api/regiones o GET /api/regiones?nombre=met */
  @GetMapping
  public List<Region> listar(@RequestParam(required = false) String nombre) {
    if (nombre == null || nombre.isBlank()) {
      return repository.findAll();
    }
    return repository.findByNombreContainingIgnoreCase(nombre);
  }

  /** GET /api/regiones/5 -> 200, o 404 si no existe. */
  @GetMapping("/{id}")
  public Region obtener(@PathVariable Long id) {
    return buscar(id);
  }

  /** POST /api/regiones -> 201 Created, con el header Location. */
  @PostMapping
  public ResponseEntity<Region> crear(@Valid @RequestBody Region region) {
    region.setId(null);
    Region guardada = repository.save(region);
    return ResponseEntity.created(URI.create("/api/regiones/" + guardada.getId())).body(guardada);
  }

  /** PUT /api/regiones/5 -> reemplaza la región COMPLETA. */
  @PutMapping("/{id}")
  public Region actualizar(@PathVariable Long id, @Valid @RequestBody Region datos) {
    Region region = buscar(id);
    region.setNombre(datos.getNombre());
    return repository.save(region);
  }

    /** PATCH /api/regiones/5 -> cambia solo los campos enviados. */
  @PatchMapping("/{id}")
  public Region modificar(@PathVariable Long id, @RequestBody Region datos) {
    Region region = buscar(id);
    if (datos.getNombre() != null) region.setNombre(datos.getNombre());
    validacion.validar(region);
    return repository.save(region);
  }

  /** DELETE /api/regiones/5 -> 204 sin cuerpo. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    repository.delete(buscar(id));
    return ResponseEntity.noContent().build();
  }

  private Region buscar(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región " + id + " no existe"));
  }
}