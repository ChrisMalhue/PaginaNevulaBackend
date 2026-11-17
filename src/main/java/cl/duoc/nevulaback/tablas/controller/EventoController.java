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

import cl.duoc.nevulaback.tablas.assemblers.EventoModelAssembler;
import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos", description = "Operaciones relacionadas con los eventos")
public class EventoController {

    private final EventoService service;
    private final EventoModelAssembler assembler;

    public EventoController(EventoService service, EventoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los eventos",
        description = "Lista los eventos, del más reciente al más antiguo. "
            + "Con ?estadoId=1 filtra por estado, con ?localId=2 por local")
    public CollectionModel<EntityModel<Evento>> obtenerTodos(
        @RequestParam(required = false) Long estadoId,
        @RequestParam(required = false) Long localId) {
        List<EntityModel<Evento>> eventos = service.obtenerTodos(estadoId, localId).stream()
            .map(assembler::toModel)
            .toList();
        return CollectionModel.of(eventos,
            linkTo(methodOn(EventoController.class).obtenerTodos(null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un evento por su ID", description = "Responde 404 si no existe")
    public EntityModel<Evento> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un evento",
        description = "Crea un evento y responde 201. El local, el estado y la imagen deben existir (si no, 404). "
            + "Si el estado es Programado, la fecha no puede ser anterior a hoy (400)")
    public ResponseEntity<EntityModel<Evento>> crear(@Valid @RequestBody Evento evento) {
        Evento guardado = service.guardar(evento);
        return ResponseEntity
            .created(linkTo(methodOn(EventoController.class).buscarPorId(guardado.getId())).toUri())
            .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el evento completo", description = "Reemplaza todos los datos del evento")
    public EntityModel<Evento> actualizar(@PathVariable Long id, @Valid @RequestBody Evento evento) {
        return assembler.toModel(service.actualizar(id, evento));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un evento",
        description = "Cambia solo los campos enviados (por ejemplo, el estado)")
    public EntityModel<Evento> actualizarParcial(@PathVariable Long id, @RequestBody Evento evento) {
        return assembler.toModel(service.actualizarParcial(id, evento));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un evento",
        description = "Responde 204. Si tiene DJs o recordatorios asignados, responde 409")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}