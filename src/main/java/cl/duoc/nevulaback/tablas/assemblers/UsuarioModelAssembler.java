package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.RolController;
import cl.duoc.nevulaback.tablas.controller.UsuarioController;
import cl.duoc.nevulaback.tablas.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UsuarioModelAssembler
    implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
        linkTo(methodOn(UsuarioController.class).buscarPorId(usuario.getId())).withSelfRel(),
        linkTo(methodOn(UsuarioController.class).obtenerTodos(null, null)).withRel("usuarios"),
        linkTo(methodOn(RolController.class).buscarPorId(usuario.getRol().getId())).withRel("rol"),
        linkTo(methodOn(UsuarioController.class).actualizar(usuario.getId(), null)).withRel("actualizar"),
        linkTo(methodOn(UsuarioController.class).actualizarParcial(usuario.getId(), null)).withRel("actualizar-parcial"),
        linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId())).withRel("eliminar"));
    }
}