package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.RolController;
import cl.duoc.nevulaback.tablas.model.Rol;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {

    @Override
    public EntityModel<Rol> toModel(Rol rol) {
        return EntityModel.of(rol,
        linkTo(methodOn(RolController.class).buscarPorId(rol.getId())).withSelfRel(),
        linkTo(methodOn(RolController.class).obtenerTodos(null)).withRel("roles"),
        linkTo(methodOn(RolController.class).actualizar(rol.getId(), null)).withRel("actualizar"),
        linkTo(methodOn(RolController.class).actualizarParcial(rol.getId(), null)).withRel("actualizar-parcial"),
        linkTo(methodOn(RolController.class).eliminar(rol.getId())).withRel("eliminar"));
    }
}