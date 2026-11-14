package cl.duoc.nevulaback.tablas.local;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {

    List<Local> findByNombreContainingIgnoreCase(String nombre);

    List<Local> findByComunaId(Long comunaId);
}