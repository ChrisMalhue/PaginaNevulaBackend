package cl.duoc.nevulaback.tablas.estado;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

  List<Estado> findByNombreContainingIgnoreCase(String nombre);
}