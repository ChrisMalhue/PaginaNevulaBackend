package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Dj;
import cl.duoc.nevulaback.tablas.repository.DjRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class DjService {

    private final DjRepository repository;
    private final Validacion validacion;

    public DjService(DjRepository repository, Validacion validacion) {
        this.repository = repository;
        this.validacion = validacion;
    }

    public List<Dj> obtenerTodos(String nombre) {
        log.info("Obteniendo djs (nombre: {})", nombre);

        if (nombre != null && !nombre.isBlank()) {
            return repository.findByNombreContainingIgnoreCase(nombre);
        }

        return repository.findAll();
    }

    public Dj buscarPorId(Long id) {
        log.info("Buscando dj por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Dj " + id + " no existe"));
    }

    public Dj guardar(Dj dj) {
        log.info("Registrando nuevo dj");
        dj.setId(null);
        return repository.saveAndFlush(dj);
    }

    public Dj actualizar(Long id, Dj datos) {
        log.info("Actualizando el dj con ID: {}", id);
        Dj dj = buscarPorId(id);
        dj.setNombre(datos.getNombre());
        return repository.saveAndFlush(dj);
    }

    public Dj actualizarParcial(Long id, Dj datos) {
        log.info("Actualizando parcialmente el dj con ID: {}", id);
        Dj dj = buscarPorId(id);

        if (datos.getNombre() != null) {
            dj.setNombre(datos.getNombre());
        }

        validacion.validar(dj);
        return repository.saveAndFlush(dj);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el dj con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }
}