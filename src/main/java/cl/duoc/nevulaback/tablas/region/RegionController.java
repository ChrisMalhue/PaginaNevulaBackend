package cl.duoc.nevulaback.tablas.region;

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
@RequestMapping("/api/regiones")
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones")
public class RegionController {

    private final RegionService service;
    private final RegionModelAssembler assembler;

    public RegionController(RegionService service, RegionModelAssembler assembler) {
      this.service = service;
      this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las regiones",
        description = "Lista las regiones. Con ?nombre=met filtra por nombre")
    public CollectionModel<EntityModel<Region>> obtenerTodos(@RequestParam(required = false) String nombre) {
      List<EntityModel<Region>> regiones = service.obtenerTodos(nombre).stream()
          .map(assembler::toModel)
          .toList();
      return CollectionModel.of(regiones,
          linkTo(methodOn(RegionController.class).obtenerTodos(null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una región por su ID", description = "Responde 404 si no existe")
    public EntityModel<Region> buscarPorId(@PathVariable Long id) {
      return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar una región", description = "Crea una región y responde 201")
    public ResponseEntity<EntityModel<Region>> crear(@Valid @RequestBody Region region) {
      Region guardada = service.guardar(region);
      return ResponseEntity
          .created(linkTo(methodOn(RegionController.class).buscarPorId(guardada.getId())).toUri())
          .body(assembler.toModel(guardada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar la región completa", description = "Reemplaza todos los datos de la región")
    public EntityModel<Region> actualizar(@PathVariable Long id, @Valid @RequestBody Region region) {
      return assembler.toModel(service.actualizar(id, region));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de una región", description = "Cambia solo los campos enviados")
    public EntityModel<Region> actualizarParcial(@PathVariable Long id, @RequestBody Region region) {
      return assembler.toModel(service.actualizarParcial(id, region));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una región",
        description = "Responde 204. Si tiene comunas asociadas, responde 409")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
      service.eliminar(id);
      return ResponseEntity.noContent().build();
    }
}