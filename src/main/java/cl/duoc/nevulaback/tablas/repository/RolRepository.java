package cl.duoc.nevulaback.tablas.repository;

import cl.duoc.nevulaback.tablas.model.Rol;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {

    List<Rol> findByNombreContainingIgnoreCase(String nombre);

    Optional<Rol> findByNombreIgnoreCase(String nombre);
}