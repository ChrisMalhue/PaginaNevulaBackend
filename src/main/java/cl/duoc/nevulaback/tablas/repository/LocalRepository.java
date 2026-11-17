package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Local;

public interface LocalRepository extends JpaRepository<Local, Long> {

    List<Local> findByNombreContainingIgnoreCase(String nombre);

    List<Local> findByComunaId(Long comunaId);
}