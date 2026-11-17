package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

  List<Estado> findByNombreContainingIgnoreCase(String nombre);
}