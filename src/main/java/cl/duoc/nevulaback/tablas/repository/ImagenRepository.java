package cl.duoc.nevulaback.tablas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Imagen;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
}