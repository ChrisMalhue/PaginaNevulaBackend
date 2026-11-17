package cl.duoc.nevulaback.tablas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.nevulaback.tablas.assemblers.ImagenModelAssembler;
import cl.duoc.nevulaback.tablas.model.Imagen;
import cl.duoc.nevulaback.tablas.service.ImagenService;

@RestController
@RequestMapping("/api/imagenes")
@Tag(name = "Imágenes", description = "Flyers de los eventos. Solo se guarda el link (ImgBB), no el archivo")
public class ImagenController {

  private final ImagenService service;
  private final ImagenModelAssembler assembler;

  public ImagenController(ImagenService service, ImagenModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @GetMapping
  @Operation(summary = "Obtener todas las imágenes", description = "Lista todas las imágenes guardadas")
  public CollectionModel<EntityModel<Imagen>> obtenerTodos() {
    List<EntityModel<Imagen>> imagenes = service.obtenerTodos().stream()
        .map(assembler::toModel)
        .toList();
    return CollectionModel.of(imagenes,
        linkTo(methodOn(ImagenController.class).obtenerTodos()).withSelfRel());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener una imagen por su ID", description = "Responde 404 si no existe")
  public EntityModel<Imagen> buscarPorId(@PathVariable Long id) {
    return assembler.toModel(service.buscarPorId(id));
  }

  @PostMapping
  @Operation(summary = "Agregar una imagen",
      description = "Guarda el link de la imagen (ya subida a ImgBB) y responde 201. "
          + "La URL debe empezar con https:// y no puede repetirse (409)")
  public ResponseEntity<EntityModel<Imagen>> crear(@Valid @RequestBody Imagen imagen) {
    Imagen guardada = service.guardar(imagen);
    return ResponseEntity
        .created(linkTo(methodOn(ImagenController.class).buscarPorId(guardada.getId())).toUri())
        .body(assembler.toModel(guardada));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar la imagen completa", description = "Reemplaza todos los datos de la imagen")
  public EntityModel<Imagen> actualizar(@PathVariable Long id, @Valid @RequestBody Imagen imagen) {
    return assembler.toModel(service.actualizar(id, imagen));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar parte de una imagen", description = "Cambia solo los campos enviados")
  public EntityModel<Imagen> actualizarParcial(@PathVariable Long id, @RequestBody Imagen imagen) {
    return assembler.toModel(service.actualizarParcial(id, imagen));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar una imagen",
      description = "Responde 204. Si algún evento usa esta imagen, responde 409")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}