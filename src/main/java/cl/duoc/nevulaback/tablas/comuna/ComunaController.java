package cl.duoc.nevulaback.tablas.comuna;

import cl.duoc.nevulaback.tablas.region.Region;
import cl.duoc.nevulaback.tablas.region.RegionRepository;
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

/**
 * Rutas del recurso "comunas". Todas empiezan con /api/comunas.
 * GET /api/comunas?regionId=7 devuelve solo las comunas de esa región.
 */
@RestController
@RequestMapping("/api/comunas")
public class ComunaController {

  private final ComunaRepository repository;
  private final RegionRepository regionRepository;
  private final Validacion validacion;

  public ComunaController(ComunaRepository repository, RegionRepository regionRepository, Validacion validacion) {
    this.repository = repository;
    this.regionRepository = regionRepository;
    this.validacion = validacion;
  }

  /** GET /api/comunas, ?nombre=san o ?regionId=7 */
  @GetMapping
  public List<Comuna> listar(@RequestParam(required = false) String nombre,
                             @RequestParam(required = false) Long regionId) {
    if (regionId != null) {
      return repository.findByRegionId(regionId);
    }
    if (nombre != null && !nombre.isBlank()) {
      return repository.findByNombreContainingIgnoreCase(nombre);
    }
    return repository.findAll();
  }

  /** GET /api/comunas/5 -> 200, o 404 si no existe. */
  @GetMapping("/{id}")
  public Comuna obtener(@PathVariable Long id) {
    return buscar(id);
  }

  /** POST /api/comunas -> 201 Created. La región debe existir (si no, 404). */
  @PostMapping
  public ResponseEntity<Comuna> crear(@Valid @RequestBody Comuna comuna) {
    comuna.setId(null);
    comuna.setRegion(buscarRegion(comuna.getRegion().getId()));
    Comuna guardada = repository.save(comuna);
    return ResponseEntity.created(URI.create("/api/comunas/" + guardada.getId())).body(guardada);
  }

  /** PUT /api/comunas/5 -> reemplaza la comuna COMPLETA. */
  @PutMapping("/{id}")
  public Comuna actualizar(@PathVariable Long id, @Valid @RequestBody Comuna datos) {
    Comuna comuna = buscar(id);
    comuna.setNombre(datos.getNombre());
    comuna.setRegion(buscarRegion(datos.getRegion().getId()));
    return repository.save(comuna);
  }

    /** PATCH /api/comunas/5 -> cambia solo los campos enviados. */
  @PatchMapping("/{id}")
  public Comuna modificar(@PathVariable Long id, @RequestBody Comuna datos) {
    Comuna comuna = buscar(id);
    if (datos.getNombre() != null) comuna.setNombre(datos.getNombre());
    if (datos.getRegion() != null) comuna.setRegion(buscarRegion(datos.getRegion().getId()));
    validacion.validar(comuna);
    return repository.save(comuna);
  }

  /** DELETE /api/comunas/5 -> 204 sin cuerpo. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    repository.delete(buscar(id));
    return ResponseEntity.noContent().build();
  }

  private Comuna buscar(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna " + id + " no existe"));
  }

  private Region buscarRegion(Long id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la región");
    }
    return regionRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región " + id + " no existe"));
  }
}