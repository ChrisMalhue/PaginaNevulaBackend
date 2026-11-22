package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.assemblers.UsuarioModelAssembler;
import cl.duoc.nevulaback.tablas.dto.UsuarioDTO;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioModelAssembler assembler;

    public UsuarioController(UsuarioService service, UsuarioModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios",
        description = "Lista los usuarios. Con ?nombreUsuario=cris filtra por nombre y con ?rolId=1 por rol")
    public CollectionModel<EntityModel<Usuario>> obtenerTodos(
        @RequestParam(required = false) String nombreUsuario,
        @RequestParam(required = false) Long rolId) {
        List<EntityModel<Usuario>> usuarios = service.obtenerTodos(nombreUsuario, rolId).stream()
        .map(assembler::toModel)
        .toList();
        return CollectionModel.of(usuarios,
        linkTo(methodOn(UsuarioController.class).obtenerTodos(null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por su ID", description = "Responde 404 si no existe")
    public EntityModel<Usuario> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un usuario",
        description = "Crea un usuario y responde 201. Si no se envía rolId queda con rol USUARIO. "
        + "Responde 409 si el nombre de usuario o el correo ya existen")
    public ResponseEntity<EntityModel<Usuario>> crear(@Valid @RequestBody UsuarioDTO datos) {
        Usuario guardado = service.guardar(datos);
        return ResponseEntity
        .created(linkTo(methodOn(UsuarioController.class).buscarPorId(guardado.getId())).toUri())
        .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario completo",
        description = "Reemplaza nombre de usuario, correo y contraseña. Si no se envía rolId conserva el rol actual")
    public EntityModel<Usuario> actualizar(@PathVariable Long id,
        @Valid @RequestBody UsuarioDTO datos) {
        return assembler.toModel(service.actualizar(id, datos));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un usuario",
        description = "Cambia solo los campos enviados")
    public EntityModel<Usuario> actualizarParcial(@PathVariable Long id,
        @RequestBody UsuarioDTO datos) {
        return assembler.toModel(service.actualizarParcial(id, datos));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}