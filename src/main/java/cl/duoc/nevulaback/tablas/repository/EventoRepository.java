package cl.duoc.nevulaback.tablas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.nevulaback.tablas.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByOrderByFechaDesc();

    List<Evento> findByEstadoIdOrderByFechaDesc(Long estadoId);

    List<Evento> findByLocalIdOrderByFechaDesc(Long localId);
}