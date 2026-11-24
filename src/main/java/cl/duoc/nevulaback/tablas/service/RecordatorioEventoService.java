package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.UsuarioAutenticadoService;
import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.model.Recordatorio;
import cl.duoc.nevulaback.tablas.model.RecordatorioEvento;
import cl.duoc.nevulaback.tablas.model.Usuario;
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
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public RecordatorioEventoService(
            RecordatorioEventoRepository repository,
            RecordatorioService recordatorioService,
            EventoService eventoService,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this.repository = repository;
        this.recordatorioService = recordatorioService;
        this.eventoService = eventoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    public List<RecordatorioEvento> obtenerTodos(Long recordatorioId, Long usuarioId, Long eventoId) {
        Usuario actual = usuarioAutenticadoService.obtenerUsuarioActual();
        Long usuarioFiltro = usuarioAutenticadoService.esAdmin(actual) ? usuarioId : actual.getId();

        log.info("Obteniendo eventos de recordatorios (recordatorioId: {}, usuarioId: {}, eventoId: {})",
            recordatorioId, usuarioFiltro, eventoId);

        List<RecordatorioEvento> lista;
        if (recordatorioId != null) {
            lista = repository.findByRecordatorioIdOrderByFechaCreacionDesc(recordatorioId);
        } else if (usuarioFiltro != null) {
            lista = repository.findByRecordatorioUsuarioIdOrderByFechaCreacionDesc(usuarioFiltro);
        } else {
            lista = repository.findAllByOrderByFechaCreacionDesc();
        }

        return lista.stream()
            .filter(re -> usuarioFiltro == null || re.getRecordatorio().getUsuario().getId().equals(usuarioFiltro))
            .filter(re -> eventoId == null || re.getEvento().getId().equals(eventoId))
            .toList();
    }

    public RecordatorioEvento buscarPorId(Long id) {
        log.info("Buscando evento de recordatorio por ID: {}", id);
        RecordatorioEvento re = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento de recordatorio " + id + " no existe"));

        usuarioAutenticadoService.validarPropietarioOAdmin(re.getRecordatorio().getUsuario());
        return re;
    }

    public RecordatorioEvento guardar(RecordatorioEvento datos) {
        log.info("Agregando un evento a un recordatorio");

        if (datos.getRecordatorio() == null || datos.getRecordatorio().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del recordatorio");
        }
        if (datos.getEvento() == null || datos.getEvento().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del evento");
        }

        // Valida automáticamente que el recordatorio exista y pertenezca al usuario autenticado
        Recordatorio recordatorio = recordatorioService.buscarPorId(datos.getRecordatorio().getId());
        Evento evento = eventoService.buscarPorId(datos.getEvento().getId());

        return repository.saveAndFlush(new RecordatorioEvento(recordatorio, evento));
    }

    public void eliminar(Long id) {
        log.info("Quitando el evento de recordatorio con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}