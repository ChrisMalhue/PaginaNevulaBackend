package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.nevulaback.tablas.controller.ImagenController;
import cl.duoc.nevulaback.tablas.model.Imagen;

@Component
public class ImagenModelAssembler implements RepresentationModelAssembler<Imagen, EntityModel<Imagen>> {

    @Override
    public EntityModel<Imagen> toModel(Imagen imagen) {
        return EntityModel.of(imagen,
            linkTo(methodOn(ImagenController.class).buscarPorId(imagen.getId())).withSelfRel(),
            linkTo(methodOn(ImagenController.class).obtenerTodos()).withRel("imagenes"),
            Link.of(imagen.getUrl(), "archivo"),
            linkTo(methodOn(ImagenController.class).actualizar(imagen.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(ImagenController.class).actualizarParcial(imagen.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(ImagenController.class).eliminar(imagen.getId())).withRel("eliminar"));
    }
}