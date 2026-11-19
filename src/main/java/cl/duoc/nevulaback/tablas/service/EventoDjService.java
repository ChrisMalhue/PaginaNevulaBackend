package cl.duoc.nevulaback.tablas.service;


import cl.duoc.nevulaback.config.Validacion;

import cl.duoc.nevulaback.tablas.model.Dj;

import cl.duoc.nevulaback.tablas.model.Evento;

import cl.duoc.nevulaback.tablas.model.EventoDj;

import cl.duoc.nevulaback.tablas.repository.EventoDjRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;


@Service

@Transactional

@Slf4j

public class EventoDjService {

    private final EventoDjRepository repository;
    private final EventoService eventoService;
    private final DjService djService;
    private final Validacion validacion;

    public EventoDjService(EventoDjRepository repository, EventoService eventoService, DjService djService, Validacion validacion) {
        this.repository = repository;
        this.eventoService = eventoService;
        this.djService = djService;
        this.validacion = validacion;
    }


    public List<EventoDj> obtenerTodos(Long eventoId, Long djId) {
        log.info("Obteniendo djs por evento (eventoId: {}, djId: {})", eventoId, djId);
        if (eventoId != null) {
            List<EventoDj> lineUp = repository.findByEventoIdOrderByOrdenAsc(eventoId);
            if (djId == null) {
                return lineUp;
            }
            return lineUp.stream().filter(ed -> ed.getDj().getId().equals(djId)).toList();
        }

        if (djId != null) {
            return repository.findByDjIdOrderByEventoFechaDesc(djId);
        }

        return repository.findAllByOrderByEventoIdAscOrdenAsc();
    }

    public EventoDj buscarPorId(Long id) {
        log.info("Buscando dj de evento por ID: {}", id);
        return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Dj de evento " + id + " no existe"));
    }

    public EventoDj guardar(EventoDj datos) {
        log.info("Registrando nuevo dj en un evento");
        datos.setId(null);
        datos.setEvento(resolverEvento(datos.getEvento()));
        datos.setDj(resolverDj(datos.getDj()));
        return repository.saveAndFlush(datos);
    }

    public EventoDj actualizar(Long id, EventoDj datos) {
        log.info("Actualizando el dj de evento con ID: {}", id);
        EventoDj existente = buscarPorId(id);
        existente.setEvento(resolverEvento(datos.getEvento()));
        existente.setDj(resolverDj(datos.getDj()));
        existente.setOrden(datos.getOrden());
        return repository.saveAndFlush(existente);
    }

    public EventoDj actualizarParcial(Long id, EventoDj datos) {
        log.info("Actualizando parcialmente el dj de evento con ID: {}", id);
        EventoDj existente = buscarPorId(id);

        if (datos.getEvento() != null) {
            existente.setEvento(resolverEvento(datos.getEvento()));
        }
        if (datos.getDj() != null) {
            existente.setDj(resolverDj(datos.getDj()));
        }
        if (datos.getOrden() != null) {
            existente.setOrden(datos.getOrden());
        }

        validacion.validar(existente);
        return repository.saveAndFlush(existente);
    }


    public void eliminar(Long id) {
        log.info("Eliminando el dj de evento con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    // Busca el evento real a partir del id enviado ({"evento": {"id": 6}})
    private Evento resolverEvento(Evento evento) {
        if (evento == null || evento.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del evento");
        }
        return eventoService.buscarPorId(evento.getId());
    }


    private Dj resolverDj(Dj dj) {
        if (dj == null || dj.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del dj");
        }
        return djService.buscarPorId(dj.getId());
    }
}