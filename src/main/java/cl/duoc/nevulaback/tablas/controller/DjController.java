package cl.duoc.nevulaback.tablas.controller;

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

import cl.duoc.nevulaback.tablas.assemblers.DjModelAssembler;
import cl.duoc.nevulaback.tablas.model.Dj;
import cl.duoc.nevulaback.tablas.service.DjService;

@RestController
@RequestMapping("/api/djs")
@Tag(name = "Djs", description = "Operaciones relacionadas con los djs")
public class DjController {

    private final DjService service;
    private final DjModelAssembler assembler;

    public DjController(DjService service, DjModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los djs",
        description = "Lista los djs. Con ?nombre=snake filtra por nombre")
    public CollectionModel<EntityModel<Dj>> obtenerTodos(
        @RequestParam(required = false) String nombre) {
        List<EntityModel<Dj>> djs = service.obtenerTodos(nombre).stream()
            .map(assembler::toModel)
            .toList();
        return CollectionModel.of(djs,
            linkTo(methodOn(DjController.class).obtenerTodos(null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un dj por su ID", description = "Responde 404 si no existe")
    public EntityModel<Dj> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un dj", description = "Crea un dj y responde 201")
    public ResponseEntity<EntityModel<Dj>> crear(@Valid @RequestBody Dj dj) {
        Dj guardado = service.guardar(dj);
        return ResponseEntity
            .created(linkTo(methodOn(DjController.class).buscarPorId(guardado.getId())).toUri())
            .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un dj completo",
        description = "Reemplaza todos los datos del dj")
    public EntityModel<Dj> actualizar(@PathVariable Long id, @Valid @RequestBody Dj dj) {
        return assembler.toModel(service.actualizar(id, dj));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un dj",
        description = "Cambia solo los campos enviados")
    public EntityModel<Dj> actualizarParcial(@PathVariable Long id, @RequestBody Dj dj) {
        return assembler.toModel(service.actualizarParcial(id, dj));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dj", description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}