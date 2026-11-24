package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.UsuarioAutenticadoService;
import cl.duoc.nevulaback.tablas.model.Recordatorio;
import cl.duoc.nevulaback.tablas.model.Usuario;
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
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public RecordatorioService(
            RecordatorioRepository repository,
            UsuarioAutenticadoService usuarioAutenticadoService) {
        this.repository = repository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    public List<Recordatorio> obtenerTodos(Long usuarioId) {
        Usuario actual = usuarioAutenticadoService.obtenerUsuarioActual();
        log.info("Obteniendo recordatorios (solicitante: {}, filtro usuarioId: {})", actual.getId(), usuarioId);

        if (!usuarioAutenticadoService.esAdmin(actual)) {
            return repository.findByUsuarioId(actual.getId());
        }

        if (usuarioId != null) {
            return repository.findByUsuarioId(usuarioId);
        }

        return repository.findAll();
    }

    public Recordatorio buscarPorId(Long id) {
        log.info("Buscando recordatorio por ID: {}", id);
        Recordatorio recordatorio = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Recordatorio " + id + " no existe"));

        usuarioAutenticadoService.validarPropietarioOAdmin(recordatorio.getUsuario());
        return recordatorio;
    }

    public Recordatorio guardar() {
        Usuario usuario = usuarioAutenticadoService.obtenerUsuarioActual();
        log.info("Registrando nuevo recordatorio para usuario ID: {}", usuario.getId());

        Recordatorio recordatorio = new Recordatorio(usuario);
        return repository.saveAndFlush(recordatorio);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el recordatorio con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}