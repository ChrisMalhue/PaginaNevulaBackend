package cl.duoc.nevulaback.tablas.comuna;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.region.Region;
import cl.duoc.nevulaback.tablas.region.RegionService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class ComunaService {

    private final ComunaRepository repository;
    private final RegionService regionService;
    private final Validacion validacion;

    public ComunaService(ComunaRepository repository, RegionService regionService,
                        Validacion validacion) {
        this.repository = repository;
        this.regionService = regionService;
        this.validacion = validacion;
    }

    public List<Comuna> obtenerTodos(String nombre, Long regionId) {
        log.info("Obteniendo comunas (nombre: {}, regionId: {})", nombre, regionId);
        if (regionId != null) {
        return repository.findByRegionId(regionId);
        }
        if (nombre != null && !nombre.isBlank()) {
        return repository.findByNombreContainingIgnoreCase(nombre);
        }
        return repository.findAll();
    }

    public Comuna buscarPorId(Long id) {
        log.info("Buscando comuna por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna " + id + " no existe"));
    }

    public Comuna guardar(Comuna comuna) {
        log.info("Registrando nueva comuna");
        comuna.setId(null);
        comuna.setRegion(resolverRegion(comuna.getRegion()));
        return repository.save(comuna);
    }

    public Comuna actualizar(Long id, Comuna datos) {
        log.info("Actualizando la comuna con ID: {}", id);
        Comuna comuna = buscarPorId(id);
        comuna.setNombre(datos.getNombre());
        comuna.setRegion(resolverRegion(datos.getRegion()));
        return repository.save(comuna);
    }

    public Comuna actualizarParcial(Long id, Comuna datos) {
        log.info("Actualizando parcialmente la comuna con ID: {}", id);
        Comuna comuna = buscarPorId(id);
        if (datos.getNombre() != null) comuna.setNombre(datos.getNombre());
        if (datos.getRegion() != null) comuna.setRegion(resolverRegion(datos.getRegion()));
        validacion.validar(comuna);
        return repository.save(comuna);
    }

    public void eliminar(Long id) {
        log.info("Eliminando la comuna con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si hay locales que dependen de esta comuna (409)
    }

    /** La región debe venir con su id y existir en la base. */
    private Region resolverRegion(Region region) {
        if (region == null || region.getId() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el id de la región");
        }
        return regionService.buscarPorId(region.getId());
    }
}