package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.nevulaback.tablas.model.EventoDj;

public interface EventoDjRepository extends JpaRepository<EventoDj, Long> {

    List<EventoDj> findByEventoIdOrderByOrdenAsc(Long eventoId);

    List<EventoDj> findByDjIdOrderByEventoFechaDesc(Long djId);

    List<EventoDj> findAllByOrderByEventoIdAscOrdenAsc();
}