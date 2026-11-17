package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {

  List<Region> findByNombreContainingIgnoreCase(String nombre);
}