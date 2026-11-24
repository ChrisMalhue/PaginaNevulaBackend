package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.CotizacionController;
import cl.duoc.nevulaback.tablas.controller.UsuarioController;
import cl.duoc.nevulaback.tablas.model.Cotizacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CotizacionModelAssembler implements RepresentationModelAssembler<Cotizacion, EntityModel<Cotizacion>> {

    @Override
    public EntityModel<Cotizacion> toModel(Cotizacion cotizacion) {
        return EntityModel.of(cotizacion,
        linkTo(methodOn(CotizacionController.class).buscarPorId(cotizacion.getId())).withSelfRel(),
        linkTo(methodOn(CotizacionController.class).obtenerTodos(null)).withRel("cotizaciones"),
        linkTo(methodOn(UsuarioController.class).buscarPorId(cotizacion.getUsuario().getId())).withRel("usuario"),
        linkTo(methodOn(CotizacionController.class).actualizar(cotizacion.getId(), null)).withRel("actualizar"),
        linkTo(methodOn(CotizacionController.class).actualizarParcial(cotizacion.getId(), null)).withRel("actualizar-parcial"),
        linkTo(methodOn(CotizacionController.class).eliminar(cotizacion.getId())).withRel("eliminar"));
    }
}