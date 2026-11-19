package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import cl.duoc.nevulaback.tablas.assemblers.EventoDjModelAssembler;
import cl.duoc.nevulaback.tablas.model.EventoDj;
import cl.duoc.nevulaback.tablas.service.EventoDjService;
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
@RequestMapping("/api/djs-evento")
@Tag(name = "Djs por evento", description = "Line-up: qué djs tocan en cada evento y en qué orden")
public class EventoDjController {

    private final EventoDjService service;
    private final EventoDjModelAssembler assembler;

    public EventoDjController(EventoDjService service, EventoDjModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener los djs de los eventos", description = "Lista todo. Con ?eventoId=6 entrega el line-up ordenado de ese evento. " + "Con ?djId=3 entrega los eventos donde toca ese dj")
    public CollectionModel<EntityModel<EventoDj>> obtenerTodos(
        @RequestParam(required = false) Long eventoId,
        @RequestParam(required = false) Long djId) {
        List<EntityModel<EventoDj>> lista = service.obtenerTodos(eventoId, djId).stream()
        .map(assembler::toModel)
        .toList();
        return CollectionModel.of(lista,
        linkTo(methodOn(EventoDjController.class).obtenerTodos(null, null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un dj de evento por su ID", description = "Responde 404 si no existe")
    public EntityModel<EventoDj> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Agregar un dj a un evento", description = "Envía evento.id, dj.id y orden. Responde 201, 404 si el evento o dj no existe, " + "y 409 si el dj ya está en ese evento")
    public ResponseEntity<EntityModel<EventoDj>> crear(@Valid @RequestBody EventoDj eventoDj) {
        EventoDj guardado = service.guardar(eventoDj);
        return ResponseEntity
        .created(linkTo(methodOn(EventoDjController.class).buscarPorId(guardado.getId())).toUri())
        .body(assembler.toModel(guardado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un dj de evento completo", description = "Reemplaza evento, dj y orden")
    public EntityModel<EventoDj> actualizar(@PathVariable Long id, @Valid @RequestBody EventoDj eventoDj) {
        return assembler.toModel(service.actualizar(id, eventoDj));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de un dj de evento", description = "Cambia solo los campos enviados (por ejemplo solo el orden)")
    public EntityModel<EventoDj> actualizarParcial(@PathVariable Long id, @RequestBody EventoDj eventoDj) {
        return assembler.toModel(service.actualizarParcial(id, eventoDj));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Quitar un dj de un evento", description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}