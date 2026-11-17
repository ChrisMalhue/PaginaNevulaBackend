package cl.duoc.nevulaback.tablas.comuna;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

@RestController
@RequestMapping("/api/comunas")
@Tag(name = "Comunas", description = "Operaciones relacionadas con las comunas")
public class ComunaController {

  private final ComunaService service;
  private final ComunaModelAssembler assembler;

  public ComunaController(ComunaService service, ComunaModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @GetMapping
  @Operation(summary = "Obtener todas las comunas",
      description = "Lista las comunas. Con ?nombre=san filtra por nombre, con ?regionId=1 por región")
  public CollectionModel<EntityModel<Comuna>> obtenerTodos(
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false) Long regionId) {
    List<EntityModel<Comuna>> comunas = service.obtenerTodos(nombre, regionId).stream()
        .map(assembler::toModel)
        .toList();
    return CollectionModel.of(comunas,
        linkTo(methodOn(ComunaController.class).obtenerTodos(null, null)).withSelfRel());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener una comuna por su ID", description = "Responde 404 si no existe")
  public EntityModel<Comuna> buscarPorId(@PathVariable Long id) {
    return assembler.toModel(service.buscarPorId(id));
  }

  @PostMapping
  @Operation(summary = "Agregar una comuna",
      description = "Crea una comuna y responde 201. La región debe existir (si no, 404)")
  public ResponseEntity<EntityModel<Comuna>> crear(@Valid @RequestBody Comuna comuna) {
    Comuna guardada = service.guardar(comuna);
    return ResponseEntity
        .created(linkTo(methodOn(ComunaController.class).buscarPorId(guardada.getId())).toUri())
        .body(assembler.toModel(guardada));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar la comuna completa", description = "Reemplaza todos los datos de la comuna")
  public EntityModel<Comuna> actualizar(@PathVariable Long id, @Valid @RequestBody Comuna comuna) {
    return assembler.toModel(service.actualizar(id, comuna));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar parte de una comuna", description = "Cambia solo los campos enviados")
  public EntityModel<Comuna> actualizarParcial(@PathVariable Long id, @RequestBody Comuna comuna) {
    return assembler.toModel(service.actualizarParcial(id, comuna));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar una comuna",
      description = "Responde 204. Si tiene locales asociados, responde 409")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}