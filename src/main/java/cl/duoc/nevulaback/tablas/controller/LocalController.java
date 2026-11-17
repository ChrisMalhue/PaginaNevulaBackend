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

import cl.duoc.nevulaback.tablas.assemblers.LocalModelAssembler;
import cl.duoc.nevulaback.tablas.model.Local;
import cl.duoc.nevulaback.tablas.service.LocalService;

@RestController
@RequestMapping("/api/locales")
@Tag(name = "Locales", description = "Operaciones relacionadas con los locales donde se hacen los eventos")
public class LocalController {

    private final LocalService service;
    private final LocalModelAssembler assembler;

    public LocalController(LocalService service, LocalModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los locales",
        description = "Lista los locales. Con ?nombre=secret filtra por nombre, con ?comunaId=3 por comuna")
    public CollectionModel<EntityModel<Local>> obtenerTodos(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Long comunaId) {
        List<EntityModel<Local>> locales = service.obtenerTodos(nombre, comunaId).stream()
            .map(assembler::toModel)
            .toList();
        return CollectionModel.of(locales,
            linkTo(methodOn(LocalController.class).obtenerTodos(null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un local por su ID", description = "Responde 404 si no existe")
    public EntityModel<Local> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un local",
        description = "Crea un local y responde 201. La comuna debe existir (si no, 404). "
            + "La dirección y el link del mapa son opcionales (los locales secretos no los llevan)")
    public ResponseEntity<EntityModel<Local>> crear(@Valid @RequestBody Local local) {
        Local guardado = service.guardar(local);
        return ResponseEntity
            .created(linkTo(methodOn(LocalController.class).buscarPorId(guardado.getId())).toUri())
            .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el local completo", description = "Reemplaza todos los datos del local")
    public EntityModel<Local> actualizar(@PathVariable Long id, @Valid @RequestBody Local local) {
        return assembler.toModel(service.actualizar(id, local));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un local", description = "Cambia solo los campos enviados")
    public EntityModel<Local> actualizarParcial(@PathVariable Long id, @RequestBody Local local) {
        return assembler.toModel(service.actualizarParcial(id, local));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un local",
        description = "Responde 204. Si tiene eventos asociados, responde 409")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}