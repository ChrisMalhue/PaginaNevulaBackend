package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.RecordatorioController;
import cl.duoc.nevulaback.tablas.controller.RecordatorioEventoController;
import cl.duoc.nevulaback.tablas.controller.UsuarioController;
import cl.duoc.nevulaback.tablas.model.Recordatorio;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class RecordatorioModelAssembler
    implements RepresentationModelAssembler<Recordatorio, EntityModel<Recordatorio>> {

    @Override
    public EntityModel<Recordatorio> toModel(Recordatorio recordatorio) {
        return EntityModel.of(recordatorio,
        linkTo(methodOn(RecordatorioController.class).buscarPorId(recordatorio.getId())).withSelfRel(),
        linkTo(methodOn(RecordatorioController.class).obtenerTodos(null)).withRel("recordatorios"),
        linkTo(methodOn(UsuarioController.class).buscarPorId(recordatorio.getUsuario().getId())).withRel("usuario"),
        linkTo(methodOn(RecordatorioEventoController.class).obtenerTodos(recordatorio.getId(), null, null)).withRel("eventos"),
        linkTo(methodOn(RecordatorioController.class).eliminar(recordatorio.getId())).withRel("eliminar"));
    }
}