package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.duoc.nevulaback.tablas.controller.RegionController;
import cl.duoc.nevulaback.tablas.model.Region;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<Region, EntityModel<Region>> {

    @Override
    public EntityModel<Region> toModel(Region region) {
        return EntityModel.of(region,
            linkTo(methodOn(RegionController.class).buscarPorId(region.getId())).withSelfRel(),
            linkTo(methodOn(RegionController.class).obtenerTodos(null)).withRel("regiones"),
            linkTo(methodOn(RegionController.class).actualizar(region.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(RegionController.class).actualizarParcial(region.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(RegionController.class).eliminar(region.getId())).withRel("eliminar"));
    }
}