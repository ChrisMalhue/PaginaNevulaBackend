package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.DjController;
import cl.duoc.nevulaback.tablas.model.Dj;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class DjModelAssembler implements RepresentationModelAssembler<Dj, EntityModel<Dj>> {

    @Override
    public EntityModel<Dj> toModel(Dj dj) {
        return EntityModel.of(dj,
            linkTo(methodOn(DjController.class).buscarPorId(dj.getId())).withSelfRel(),
            linkTo(methodOn(DjController.class).obtenerTodos(null)).withRel("djs"),
            linkTo(methodOn(DjController.class).actualizar(dj.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(DjController.class).actualizarParcial(dj.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(DjController.class).eliminar(dj.getId())).withRel("eliminar"));
    }
}