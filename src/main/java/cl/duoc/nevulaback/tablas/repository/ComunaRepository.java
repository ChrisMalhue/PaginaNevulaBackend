package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Comuna;

/**
 * JpaRepository ya trae findAll, findById, save y delete.
 * findByNombreContainingIgnoreCase("san") -> comunas cuyo nombre contiene "san".
 * findByRegionId(7) -> SELECT ... WHERE region_id = 7 (las comunas de una región).
 */
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    List<Comuna> findByNombreContainingIgnoreCase(String nombre);

    List<Comuna> findByRegionId(Long regionId);
}