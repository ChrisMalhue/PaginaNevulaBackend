package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Rol;
import cl.duoc.nevulaback.tablas.repository.RolRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class RolService {

    private final RolRepository repository;
    private final Validacion validacion;

    public RolService(RolRepository repository, Validacion validacion) {
        this.repository = repository;
        this.validacion = validacion;
    }

    public List<Rol> obtenerTodos(String nombre) {
        log.info("Obteniendo roles (nombre: {})", nombre);

        if (nombre != null && !nombre.isBlank()) {
        return repository.findByNombreContainingIgnoreCase(nombre);
        }

        return repository.findAll();
    }

    public Rol buscarPorId(Long id) {
        log.info("Buscando rol por ID: {}", id);
        return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Rol " + id + " no existe"));
    }

    public Rol guardar(Rol rol) {
        log.info("Registrando nuevo rol");
        rol.setId(null);
        return repository.saveAndFlush(rol);
    }

    public Rol actualizar(Long id, Rol datos) {
        log.info("Actualizando el rol con ID: {}", id);
        Rol rol = buscarPorId(id);
        rol.setNombre(datos.getNombre());
        return repository.saveAndFlush(rol);
    }

    public Rol actualizarParcial(Long id, Rol datos) {
        log.info("Actualizando parcialmente el rol con ID: {}", id);
        Rol rol = buscarPorId(id);

        if (datos.getNombre() != null) {
        rol.setNombre(datos.getNombre());
        }

        validacion.validar(rol);
        return repository.saveAndFlush(rol);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el rol con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    public Rol buscarPorNombre(String nombre) {
        return repository.findByNombreIgnoreCase(nombre)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Rol " + nombre + " no existe"));
    }
}