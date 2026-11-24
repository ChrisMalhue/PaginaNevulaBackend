package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.assemblers.CotizacionModelAssembler;
import cl.duoc.nevulaback.tablas.model.Cotizacion;
import cl.duoc.nevulaback.tablas.service.CotizacionService;
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
@RequestMapping("/api/cotizaciones")
@Tag(name = "Cotizaciones", description = "Solicitudes de cotización de los usuarios")
public class CotizacionController {

    private final CotizacionService service;
    private final CotizacionModelAssembler assembler;

    public CotizacionController(CotizacionService service, CotizacionModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cotizaciones",
        description = "Lista las cotizaciones, de la más reciente a la más antigua. "
        + "Con ?usuarioId=2 entrega solo las de ese usuario")
    public CollectionModel<EntityModel<Cotizacion>> obtenerTodos(
        @RequestParam(required = false) Long usuarioId) {
        List<EntityModel<Cotizacion>> cotizaciones = service.obtenerTodos(usuarioId).stream()
        .map(assembler::toModel)
        .toList();
        return CollectionModel.of(cotizaciones,
        linkTo(methodOn(CotizacionController.class).obtenerTodos(null)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una cotización por su ID", description = "Responde 404 si no existe")
    public EntityModel<Cotizacion> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Enviar una cotización",
        description = "Envía nombre, telefono (+56912345678), fechaEvento, descripcion y usuario.id. "
        + "Responde 201, 400 si algún dato es inválido, 403 si el usuario no tiene rol USUARIO "
        + "y 404 si el usuario no existe")
    public ResponseEntity<EntityModel<Cotizacion>> crear(@Valid @RequestBody Cotizacion cotizacion) {
        Cotizacion guardada = service.guardar(cotizacion);
        return ResponseEntity
        .created(linkTo(methodOn(CotizacionController.class).buscarPorId(guardada.getId())).toUri())
        .body(assembler.toModel(guardada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cotización completa",
        description = "Reemplaza nombre, telefono, fechaEvento y descripcion. El usuario no se puede cambiar")
    public EntityModel<Cotizacion> actualizar(@PathVariable Long id,
        @Valid @RequestBody Cotizacion cotizacion) {
        return assembler.toModel(service.actualizar(id, cotizacion));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parte de una cotización",
        description = "Cambia solo los campos enviados")
    public EntityModel<Cotizacion> actualizarParcial(@PathVariable Long id,
        @RequestBody Cotizacion cotizacion) {
        return assembler.toModel(service.actualizarParcial(id, cotizacion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cotización", description = "Responde 204 si se elimina correctamente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}