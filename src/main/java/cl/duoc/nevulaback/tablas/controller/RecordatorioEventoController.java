package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.assemblers.RecordatorioEventoModelAssembler;
import cl.duoc.nevulaback.tablas.model.RecordatorioEvento;
import cl.duoc.nevulaback.tablas.service.RecordatorioEventoService;
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
@RequestMapping("/api/recordatorios-evento")
@Tag(name = "Eventos de recordatorios", description = "Eventos guardados en la lista de recordatorios (la campanita)")
public class RecordatorioEventoController {

    private final RecordatorioEventoService service;
    private final RecordatorioEventoModelAssembler assembler;

    public RecordatorioEventoController(RecordatorioEventoService service,
        RecordatorioEventoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener los eventos guardados en recordatorios",
        description = "Con ?usuarioId=2 entrega los eventos que guardó ese usuario (Mis recordatorios). "
        + "También se puede filtrar con ?recordatorioId= y ?eventoId=. Del más reciente al más antiguo")
    public CollectionModel<EntityModel<RecordatorioEvento>> obtenerTodos(
        @RequestParam(required = false) Long recordatorioId,
        @RequestParam(required = false) Long usuarioId,
        @RequestParam(required = false) Long eventoId) {
        List<EntityModel<RecordatorioEvento>> lista =
        service.obtenerTodos(recordatorioId, usuarioId, eventoId).stream()
            .map(assembler::toModel)
            .toList();
        return CollectionModel.of(lista,
        linkTo(methodOn(RecordatorioEventoController.class).obtenerTodos(null, null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un evento de recordatorio por su ID", description = "Responde 404 si no existe")
    public EntityModel<RecordatorioEvento> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Guardar un evento en un recordatorio (activar la campanita)",
        description = "Envía recordatorio.id y evento.id. Responde 201, 404 si no existen y 409 si ya estaba guardado")
    public ResponseEntity<EntityModel<RecordatorioEvento>> crear(
        @Valid @RequestBody RecordatorioEvento recordatorioEvento) {
        RecordatorioEvento guardado = service.guardar(recordatorioEvento);
        return ResponseEntity
        .created(linkTo(methodOn(RecordatorioEventoController.class).buscarPorId(guardado.getId())).toUri())
        .body(assembler.toModel(guardado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Quitar un evento del recordatorio (desactivar la campanita)",
        description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}