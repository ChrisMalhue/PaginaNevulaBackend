package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.assemblers.RecordatorioModelAssembler;
import cl.duoc.nevulaback.tablas.model.Recordatorio;
import cl.duoc.nevulaback.tablas.service.RecordatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recordatorios")
@Tag(name = "Recordatorios", description = "Lista de recordatorios de cada usuario")
public class RecordatorioController {

    private final RecordatorioService service;
    private final RecordatorioModelAssembler assembler;

    public RecordatorioController(RecordatorioService service, RecordatorioModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los recordatorios",
        description = "Lista los recordatorios. Con ?usuarioId=2 entrega los de ese usuario")
    public CollectionModel<EntityModel<Recordatorio>> obtenerTodos(
        @RequestParam(required = false) Long usuarioId) {
        List<EntityModel<Recordatorio>> recordatorios = service.obtenerTodos(usuarioId).stream()
        .map(assembler::toModel)
        .toList();
        return CollectionModel.of(recordatorios,
        linkTo(methodOn(RecordatorioController.class).obtenerTodos(null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un recordatorio por su ID", description = "Responde 404 si no existe")
    public EntityModel<Recordatorio> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear la lista de recordatorios del usuario autenticado",
        description = "Toma el usuario desde el token JWT. Responde 201")
    public ResponseEntity<EntityModel<Recordatorio>> crear() {
        Recordatorio guardado = service.guardar();
        return ResponseEntity
            .created(linkTo(methodOn(RecordatorioController.class).buscarPorId(guardado.getId())).toUri())
            .body(assembler.toModel(guardado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista de recordatorios",
        description = "Responde 204. Responde 409 si la lista todavía tiene eventos")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}