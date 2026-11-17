package cl.duoc.nevulaback.tablas.estado;

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
@RequestMapping("/api/estados")
@Tag(name = "Estados", description = "Estados de un evento: Programado, Hoy, Finalizado y Cancelado")
public class EstadoController {

  private final EstadoService service;
  private final EstadoModelAssembler assembler;

  public EstadoController(EstadoService service, EstadoModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @GetMapping
  @Operation(summary = "Obtener todos los estados",
      description = "Lista los estados. Con ?nombre=prog filtra por nombre")
  public CollectionModel<EntityModel<Estado>> obtenerTodos(@RequestParam(required = false) String nombre) {
    List<EntityModel<Estado>> estados = service.obtenerTodos(nombre).stream()
        .map(assembler::toModel)
        .toList();
    return CollectionModel.of(estados,
        linkTo(methodOn(EstadoController.class).obtenerTodos(null)).withSelfRel());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener un estado por su ID", description = "Responde 404 si no existe")
  public EntityModel<Estado> buscarPorId(@PathVariable Long id) {
    return assembler.toModel(service.buscarPorId(id));
  }

  @PostMapping
  @Operation(summary = "Agregar un estado",
      description = "Crea un estado y responde 201. Si el nombre ya existe, responde 409")
  public ResponseEntity<EntityModel<Estado>> crear(@Valid @RequestBody Estado estado) {
    Estado guardado = service.guardar(estado);
    return ResponseEntity
        .created(linkTo(methodOn(EstadoController.class).buscarPorId(guardado.getId())).toUri())
        .body(assembler.toModel(guardado));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar el estado completo", description = "Reemplaza todos los datos del estado")
  public EntityModel<Estado> actualizar(@PathVariable Long id, @Valid @RequestBody Estado estado) {
    return assembler.toModel(service.actualizar(id, estado));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar parte de un estado", description = "Cambia solo los campos enviados")
  public EntityModel<Estado> actualizarParcial(@PathVariable Long id, @RequestBody Estado estado) {
    return assembler.toModel(service.actualizarParcial(id, estado));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar un estado",
      description = "Responde 204. Si algún evento usa este estado, responde 409")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}