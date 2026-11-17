package cl.duoc.nevulaback.tablas.local;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.comuna.ComunaController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class LocalModelAssembler implements RepresentationModelAssembler<Local, EntityModel<Local>> {

    @Override
    public EntityModel<Local> toModel(Local local) {
        EntityModel<Local> modelo = EntityModel.of(local,
            linkTo(methodOn(LocalController.class).buscarPorId(local.getId())).withSelfRel(),
            linkTo(methodOn(LocalController.class).obtenerTodos(null, null)).withRel("locales"),
            linkTo(methodOn(ComunaController.class).buscarPorId(local.getComuna().getId())).withRel("comuna"),
            linkTo(methodOn(LocalController.class).actualizar(local.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(LocalController.class).actualizarParcial(local.getId(), null)).withRel("actualizar-parcial"),
            linkTo(methodOn(LocalController.class).eliminar(local.getId())).withRel("eliminar"));

        // Los locales secretos no tienen link de mapa
        if (local.getMapaUrl() != null) {
        modelo.add(Link.of(local.getMapaUrl(), "mapa"));
        }
        return modelo;
    }
}