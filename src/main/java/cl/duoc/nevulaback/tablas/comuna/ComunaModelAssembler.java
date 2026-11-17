package cl.duoc.nevulaback.tablas.comuna;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.region.RegionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<Comuna, EntityModel<Comuna>> {

    @Override
    public EntityModel<Comuna> toModel(Comuna comuna) {
        return EntityModel.of(comuna,
            linkTo(methodOn(ComunaController.class).buscarPorId(comuna.getId())).withSelfRel(),
            linkTo(methodOn(ComunaController.class).obtenerTodos(null, null)).withRel("comunas"),
            linkTo(methodOn(RegionController.class).buscarPorId(comuna.getRegion().getId())).withRel("region"),
            linkTo(methodOn(ComunaController.class).actualizar(comuna.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(ComunaController.class).actualizarParcial(comuna.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(ComunaController.class).eliminar(comuna.getId())).withRel("eliminar"));
  }
}