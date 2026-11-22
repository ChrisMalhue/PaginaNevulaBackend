package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.tablas.model.Recordatorio;
import cl.duoc.nevulaback.tablas.repository.RecordatorioRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class RecordatorioService {

    private final RecordatorioRepository repository;
    private final UsuarioService usuarioService;

    public RecordatorioService(RecordatorioRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<Recordatorio> obtenerTodos(Long usuarioId) {
        log.info("Obteniendo recordatorios (usuarioId: {})", usuarioId);

        if (usuarioId != null) {
        return repository.findByUsuarioId(usuarioId);
        }

        return repository.findAll();
    }

    public Recordatorio buscarPorId(Long id) {
        log.info("Buscando recordatorio por ID: {}", id);
        return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Recordatorio " + id + " no existe"));
    }

    public Recordatorio guardar(Recordatorio datos) {
        log.info("Registrando nuevo recordatorio");

        if (datos.getUsuario() == null || datos.getUsuario().getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar el id del usuario");
        }

        // Se crea uno nuevo para que id y fechaCreacion no puedan venir desde el cliente.
        Recordatorio recordatorio = new Recordatorio(usuarioService.buscarPorId(datos.getUsuario().getId()));
        return repository.saveAndFlush(recordatorio);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el recordatorio con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}