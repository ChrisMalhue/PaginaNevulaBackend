package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.assemblers.RolModelAssembler;
import cl.duoc.nevulaback.tablas.model.Rol;
import cl.duoc.nevulaback.tablas.service.RolService;
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
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Operaciones relacionadas con los roles (USUARIO, ADMIN)")
public class RolController {

    private final RolService service;
    private final RolModelAssembler assembler;

    public RolController(RolService service, RolModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los roles",
        description = "Lista los roles. Con ?nombre=admin filtra por nombre")
    public CollectionModel<EntityModel<Rol>> obtenerTodos(
        @RequestParam(required = false) String nombre) {
        List<EntityModel<Rol>> roles = service.obtenerTodos(nombre).stream()
        .map(assembler::toModel)
        .toList();
        return CollectionModel.of(roles,
        linkTo(methodOn(RolController.class).obtenerTodos(null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por su ID", description = "Responde 404 si no existe")
    public EntityModel<Rol> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un rol",
        description = "Crea un rol y responde 201. Responde 409 si el nombre ya existe")
    public ResponseEntity<EntityModel<Rol>> crear(@Valid @RequestBody Rol rol) {
        Rol guardado = service.guardar(rol);
        return ResponseEntity
        .created(linkTo(methodOn(RolController.class).buscarPorId(guardado.getId())).toUri())
        .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rol completo",
        description = "Reemplaza todos los datos del rol")
    public EntityModel<Rol> actualizar(@PathVariable Long id, @Valid @RequestBody Rol rol) {
        return assembler.toModel(service.actualizar(id, rol));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un rol",
        description = "Cambia solo los campos enviados")
    public EntityModel<Rol> actualizarParcial(@PathVariable Long id, @RequestBody Rol rol) {
        return assembler.toModel(service.actualizarParcial(id, rol));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol", description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}