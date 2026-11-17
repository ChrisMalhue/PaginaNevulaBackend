package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Dj;

/**
 * JpaRepository ya trae findAll, findById, save y delete.
 * findByNombreContainingIgnoreCase("snake") -> djs cuyo nombre contiene "snake".
 */
public interface DjRepository extends JpaRepository<Dj, Long> {

    List<Dj> findByNombreContainingIgnoreCase(String nombre);
}