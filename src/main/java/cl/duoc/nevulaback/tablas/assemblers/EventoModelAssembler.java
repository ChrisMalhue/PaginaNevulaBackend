package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.EventoController;
import cl.duoc.nevulaback.tablas.controller.EstadoController;
import cl.duoc.nevulaback.tablas.controller.ImagenController;
import cl.duoc.nevulaback.tablas.controller.LocalController;
import cl.duoc.nevulaback.tablas.model.Evento;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventoModelAssembler implements RepresentationModelAssembler<Evento, EntityModel<Evento>> {

    @Override
    public EntityModel<Evento> toModel(Evento evento) {
        EntityModel<Evento> modelo = EntityModel.of(evento,
            linkTo(methodOn(EventoController.class).buscarPorId(evento.getId())).withSelfRel(),
            linkTo(methodOn(EventoController.class).obtenerTodos(null, null)).withRel("eventos"),
            linkTo(methodOn(LocalController.class).buscarPorId(evento.getLocal().getId())).withRel("local"),
            linkTo(methodOn(EstadoController.class).buscarPorId(evento.getEstado().getId())).withRel("estado"),
            linkTo(methodOn(ImagenController.class).buscarPorId(evento.getImagen().getId())).withRel("imagen"),
            linkTo(methodOn(EventoController.class).actualizar(evento.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(EventoController.class).actualizarParcial(evento.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(EventoController.class).eliminar(evento.getId())).withRel("eliminar"));

        // El link de entradas solo existe si el evento está Programado
        boolean programado = "Programado".equalsIgnoreCase(evento.getEstado().getNombre());
        if (programado && evento.getLinkEntradas() != null) {
        modelo.add(Link.of(evento.getLinkEntradas(), "entradas"));
        }
        return modelo;
    }
}