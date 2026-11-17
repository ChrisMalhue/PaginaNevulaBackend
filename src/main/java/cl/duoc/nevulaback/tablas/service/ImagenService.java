package cl.duoc.nevulaback.tablas.service;

import cl.duoc.nevulaback.config.Validacion;
import cl.duoc.nevulaback.tablas.model.Imagen;
import cl.duoc.nevulaback.tablas.repository.ImagenRepository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class ImagenService {

    private final ImagenRepository repository;
    private final Validacion validacion;

    public ImagenService(ImagenRepository repository, Validacion validacion) {
        this.repository = repository;
        this.validacion = validacion;
    }

    public List<Imagen> obtenerTodos() {
        log.info("Obteniendo todas las imágenes");
        return repository.findAll();
    }

    public Imagen buscarPorId(Long id) {
        log.info("Buscando imagen por ID: {}", id);
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen " + id + " no existe"));
    }

    public Imagen guardar(Imagen imagen) {
        log.info("Registrando nueva imagen");
        imagen.setId(null);
        return repository.saveAndFlush(imagen); // si la URL ya existe, responde 409
    }

    public Imagen actualizar(Long id, Imagen datos) {
        log.info("Actualizando la imagen con ID: {}", id);
        Imagen imagen = buscarPorId(id);
        imagen.setUrl(datos.getUrl());
        imagen.setDescripcion(datos.getDescripcion());
        return repository.saveAndFlush(imagen);
    }

    public Imagen actualizarParcial(Long id, Imagen datos) {
        log.info("Actualizando parcialmente la imagen con ID: {}", id);
        Imagen imagen = buscarPorId(id);
        if (datos.getUrl() != null) imagen.setUrl(datos.getUrl());
        if (datos.getDescripcion() != null) imagen.setDescripcion(datos.getDescripcion());
        validacion.validar(imagen);
        return repository.saveAndFlush(imagen);
    }

    public void eliminar(Long id) {
        log.info("Eliminando la imagen con ID: {}", id);
        repository.delete(buscarPorId(id));
        repository.flush(); // fuerza el error aquí si hay eventos que usan esta imagen (409)
    }
}