package cl.duoc.nevulaback.tablas.imagen;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Rutas del recurso "imagenes". Todas empiezan con /api/imagenes. */
@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

  private final ImagenRepository repository;
  private final Validacion validacion;

  public ImagenController(ImagenRepository repository, Validacion validacion) {
    this.repository = repository;
    this.validacion = validacion;
  }

  @GetMapping
  public List<Imagen> listar() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public Imagen obtener(@PathVariable Long id) {
    return buscar(id);
  }

  /** POST /api/imagenes -> 201 Created, con el header Location. */
  @PostMapping
  public ResponseEntity<Imagen> crear(@Valid @RequestBody Imagen imagen) {
    imagen.setId(null);
    Imagen guardada = repository.save(imagen);
    return ResponseEntity.created(URI.create("/api/imagenes/" + guardada.getId())).body(guardada);
  }

  /** PUT /api/imagenes/5 -> reemplaza la imagen COMPLETA. */
  @PutMapping("/{id}")
  public Imagen actualizar(@PathVariable Long id, @Valid @RequestBody Imagen datos) {
    Imagen imagen = buscar(id);
    imagen.setUrl(datos.getUrl());
    imagen.setDescripcion(datos.getDescripcion());
    return repository.save(imagen);
  }

  /** PATCH /api/imagenes/5 -> cambia solo los campos enviados. */
  @PatchMapping("/{id}")
  public Imagen modificar(@PathVariable Long id, @RequestBody Imagen datos) {
    Imagen imagen = buscar(id);
    if (datos.getUrl() != null) imagen.setUrl(datos.getUrl());
    if (datos.getDescripcion() != null) imagen.setDescripcion(datos.getDescripcion());
    validacion.validar(imagen);
    return repository.save(imagen);
  }

  /** DELETE /api/imagenes/5 -> 204. Si algún evento la usa, responde 409. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    repository.delete(buscar(id));
    return ResponseEntity.noContent().build();
  }

  private Imagen buscar(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen " + id + " no existe"));
  }
}