package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Region;
import cl.duoc.nevulaback.tablas.repository.RegionRepository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class RegionService {

    private final RegionRepository repository;
    private final Validacion validacion;

    public RegionService(RegionRepository repository, Validacion validacion) {
        this.repository = repository;
        this.validacion = validacion;
    }

    public List<Region> obtenerTodos(String nombre) {
        log.info("Obteniendo regiones (filtro nombre: {})", nombre);
        if (nombre == null || nombre.isBlank()) {
        return repository.findAll();
        }
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Region buscarPorId(Long id) {
        log.info("Buscando región por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región " + id + " no existe"));
    }

    public Region guardar(Region region) {
        log.info("Registrando nueva región");
        region.setId(null); // el id lo genera la base de datos
        return repository.save(region);
    }

    public Region actualizar(Long id, Region datos) {
        log.info("Actualizando la región con ID: {}", id);
        Region region = buscarPorId(id);
        region.setNombre(datos.getNombre());
        return repository.save(region);
    }

    public Region actualizarParcial(Long id, Region datos) {
        log.info("Actualizando parcialmente la región con ID: {}", id);
        Region region = buscarPorId(id);
        if (datos.getNombre() != null) region.setNombre(datos.getNombre());
        validacion.validar(region);
        return repository.save(region);
    }

    public void eliminar(Long id) {
        log.info("Eliminando la región con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si hay comunas que dependen de esta región (409)
    }
}