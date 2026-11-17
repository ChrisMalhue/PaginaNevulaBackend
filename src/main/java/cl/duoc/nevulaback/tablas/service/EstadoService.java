package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Estado;
import cl.duoc.nevulaback.tablas.repository.EstadoRepository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class EstadoService {

    private final EstadoRepository repository;
    private final Validacion validacion;

    public EstadoService(EstadoRepository repository, Validacion validacion) {
        this.repository = repository;
        this.validacion = validacion;
    }

    public List<Estado> obtenerTodos(String nombre) {
        log.info("Obteniendo estados (filtro nombre: {})", nombre);
        if (nombre == null || nombre.isBlank()) {
        return repository.findAll();
        }
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Estado buscarPorId(Long id) {
        log.info("Buscando estado por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado " + id + " no existe"));
    }

    public Estado guardar(Estado estado) {
        log.info("Registrando nuevo estado");
        estado.setId(null);
        return repository.saveAndFlush(estado); // si el nombre ya existe, responde 409
    }

    public Estado actualizar(Long id, Estado datos) {
        log.info("Actualizando el estado con ID: {}", id);
        Estado estado = buscarPorId(id);
        estado.setNombre(datos.getNombre());
        return repository.saveAndFlush(estado);
    }

    public Estado actualizarParcial(Long id, Estado datos) {
        log.info("Actualizando parcialmente el estado con ID: {}", id);
        Estado estado = buscarPorId(id);
        if (datos.getNombre() != null) estado.setNombre(datos.getNombre());
        validacion.validar(estado);
        return repository.saveAndFlush(estado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el estado con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si hay eventos que usan este estado (409)
    }
}