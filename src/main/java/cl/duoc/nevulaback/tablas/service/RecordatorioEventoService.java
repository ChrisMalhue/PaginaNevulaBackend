package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.model.Recordatorio;
import cl.duoc.nevulaback.tablas.model.RecordatorioEvento;
import cl.duoc.nevulaback.tablas.repository.RecordatorioEventoRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class RecordatorioEventoService {

    private final RecordatorioEventoRepository repository;
    private final RecordatorioService recordatorioService;
    private final EventoService eventoService;

    public RecordatorioEventoService(RecordatorioEventoRepository repository,
        RecordatorioService recordatorioService, EventoService eventoService) {
        this.repository = repository;
        this.recordatorioService = recordatorioService;
        this.eventoService = eventoService;
    }

    public List<RecordatorioEvento> obtenerTodos(Long recordatorioId, Long usuarioId, Long eventoId) {
        log.info("Obteniendo eventos de recordatorios (recordatorioId: {}, usuarioId: {}, eventoId: {})",
        recordatorioId, usuarioId, eventoId);

        List<RecordatorioEvento> lista;
        if (recordatorioId != null) {
        lista = repository.findByRecordatorioIdOrderByFechaCreacionDesc(recordatorioId);
        } else if (usuarioId != null) {
        lista = repository.findByRecordatorioUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        } else {
        lista = repository.findAllByOrderByFechaCreacionDesc();
        }

        return lista.stream()
        .filter(re -> usuarioId == null || re.getRecordatorio().getUsuario().getId().equals(usuarioId))
        .filter(re -> eventoId == null || re.getEvento().getId().equals(eventoId))
        .toList();
    }

    public RecordatorioEvento buscarPorId(Long id) {
        log.info("Buscando evento de recordatorio por ID: {}", id);
        return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Evento de recordatorio " + id + " no existe"));
    }

    public RecordatorioEvento guardar(RecordatorioEvento datos) {
        log.info("Agregando un evento a un recordatorio");

        if (datos.getRecordatorio() == null || datos.getRecordatorio().getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del recordatorio");
        }
        if (datos.getEvento() == null || datos.getEvento().getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del evento");
        }

        Recordatorio recordatorio = recordatorioService.buscarPorId(datos.getRecordatorio().getId());
        Evento evento = eventoService.buscarPorId(datos.getEvento().getId());

        // Se crea uno nuevo para que id y fechaCreacion no puedan venir desde el cliente.
        return repository.saveAndFlush(new RecordatorioEvento(recordatorio, evento));
    }

    public void eliminar(Long id) {
        log.info("Quitando el evento de recordatorio con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}