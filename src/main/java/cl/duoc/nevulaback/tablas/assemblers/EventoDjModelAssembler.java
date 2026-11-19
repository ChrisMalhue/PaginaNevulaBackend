package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import cl.duoc.nevulaback.tablas.controller.DjController;
import cl.duoc.nevulaback.tablas.controller.EventoController;
import cl.duoc.nevulaback.tablas.controller.EventoDjController;
import cl.duoc.nevulaback.tablas.model.EventoDj;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventoDjModelAssembler implements RepresentationModelAssembler<EventoDj, EntityModel<EventoDj>> {
    @Override
    public EntityModel<EventoDj> toModel(EventoDj ed) {
        return EntityModel.of(ed,
        linkTo(methodOn(EventoDjController.class).buscarPorId(ed.getId())).withSelfRel(),
        linkTo(methodOn(EventoDjController.class).obtenerTodos(null, null)).withRel("djs-evento"),
        linkTo(methodOn(EventoController.class).buscarPorId(ed.getEvento().getId())).withRel("evento"),
        linkTo(methodOn(DjController.class).buscarPorId(ed.getDj().getId())).withRel("dj"),
        linkTo(methodOn(EventoDjController.class).actualizar(ed.getId(), null)).withRel("actualizar"),
        linkTo(methodOn(EventoDjController.class).actualizarParcial(ed.getId(), null)).withRel("actualizar-parcial"),
        linkTo(methodOn(EventoDjController.class).eliminar(ed.getId())).withRel("eliminar"));
    }
}