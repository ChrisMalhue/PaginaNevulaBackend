package cl.duoc.nevulaback.tablas.local;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.comuna.Comuna;
import cl.duoc.nevulaback.tablas.comuna.ComunaService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class LocalService {

    private final LocalRepository repository;
    private final ComunaService comunaService;
    private final Validacion validacion;

    public LocalService(LocalRepository repository, ComunaService comunaService,
                        Validacion validacion) {
        this.repository = repository;
        this.comunaService = comunaService;
        this.validacion = validacion;
    }

    public List<Local> obtenerTodos(String nombre, Long comunaId) {
        log.info("Obteniendo locales (nombre: {}, comunaId: {})", nombre, comunaId);
        if (comunaId != null) {
        return repository.findByComunaId(comunaId);
        }
        if (nombre != null && !nombre.isBlank()) {
        return repository.findByNombreContainingIgnoreCase(nombre);
        }
        return repository.findAll();
    }

    public Local buscarPorId(Long id) {
        log.info("Buscando local por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local " + id + " no existe"));
    }

    public Local guardar(Local local) {
        log.info("Registrando nuevo local");
        local.setId(null);
        local.setComuna(resolverComuna(local.getComuna()));
        return repository.save(local);
    }

    public Local actualizar(Long id, Local datos) {
        log.info("Actualizando el local con ID: {}", id);
        Local local = buscarPorId(id);
        local.setNombre(datos.getNombre());
        local.setDireccion(datos.getDireccion());
        local.setMapaUrl(datos.getMapaUrl());
        local.setComuna(resolverComuna(datos.getComuna()));
        return repository.save(local);
    }

    public Local actualizarParcial(Long id, Local datos) {
        log.info("Actualizando parcialmente el local con ID: {}", id);
        Local local = buscarPorId(id);
        if (datos.getNombre() != null) local.setNombre(datos.getNombre());
        if (datos.getDireccion() != null) local.setDireccion(datos.getDireccion());
        if (datos.getMapaUrl() != null) local.setMapaUrl(datos.getMapaUrl());
        if (datos.getComuna() != null) local.setComuna(resolverComuna(datos.getComuna()));
        validacion.validar(local);
        return repository.save(local);
    }

    public void eliminar(Long id) {
        log.info("Eliminando el local con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si hay eventos que dependen de este local (409)
    }

    /** La comuna debe venir con su id y existir en la base. */
    private Comuna resolverComuna(Comuna comuna) {
        if (comuna == null || comuna.getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la comuna");
        }
        return comunaService.buscarPorId(comuna.getId());
    }
}