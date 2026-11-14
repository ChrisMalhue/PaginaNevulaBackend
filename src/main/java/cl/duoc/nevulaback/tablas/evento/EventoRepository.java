package cl.duoc.nevulaback.tablas.evento;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByOrderByFechaDesc();

    List<Evento> findByEstadoIdOrderByFechaDesc(Long estadoId);

    List<Evento> findByLocalIdOrderByFechaDesc(Long localId);
}