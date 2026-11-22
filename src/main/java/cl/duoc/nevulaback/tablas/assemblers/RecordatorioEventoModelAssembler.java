package cl.duoc.nevulaback.tablas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import cl.duoc.nevulaback.tablas.controller.EventoController;
import cl.duoc.nevulaback.tablas.controller.RecordatorioController;
import cl.duoc.nevulaback.tablas.controller.RecordatorioEventoController;
import cl.duoc.nevulaback.tablas.model.RecordatorioEvento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class RecordatorioEventoModelAssembler
    implements RepresentationModelAssembler<RecordatorioEvento, EntityModel<RecordatorioEvento>> {

    @Override
    public EntityModel<RecordatorioEvento> toModel(RecordatorioEvento re) {
        return EntityModel.of(re,
        linkTo(methodOn(RecordatorioEventoController.class).buscarPorId(re.getId())).withSelfRel(),
        linkTo(methodOn(RecordatorioEventoController.class).obtenerTodos(null, null, null)).withRel("recordatorios-evento"),
        linkTo(methodOn(RecordatorioController.class).buscarPorId(re.getRecordatorio().getId())).withRel("recordatorio"),
        linkTo(methodOn(EventoController.class).buscarPorId(re.getEvento().getId())).withRel("evento"),
        linkTo(methodOn(RecordatorioEventoController.class).eliminar(re.getId())).withRel("eliminar"));
    }
}