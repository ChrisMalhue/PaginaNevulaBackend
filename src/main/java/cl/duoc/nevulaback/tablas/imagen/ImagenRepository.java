package cl.duoc.nevulaback.tablas.imagen;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    Optional<Imagen> findByUrl(String url);
}