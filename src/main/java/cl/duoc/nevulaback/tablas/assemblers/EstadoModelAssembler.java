package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.nevulaback.tablas.controller.EstadoController;
import cl.duoc.nevulaback.tablas.model.Estado;

@Component
public class EstadoModelAssembler implements RepresentationModelAssembler<Estado, EntityModel<Estado>> {

    @Override
    public EntityModel<Estado> toModel(Estado estado) {
        return EntityModel.of(estado,
            linkTo(methodOn(EstadoController.class).buscarPorId(estado.getId())).withSelfRel(),
            linkTo(methodOn(EstadoController.class).obtenerTodos(null)).withRel("estados"),
            linkTo(methodOn(EstadoController.class).actualizar(estado.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(EstadoController.class).actualizarParcial(estado.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(EstadoController.class).eliminar(estado.getId())).withRel("eliminar"));
    }
}