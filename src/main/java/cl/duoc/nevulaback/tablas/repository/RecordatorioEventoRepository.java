package cl.duoc.nevulaback.tablas.repository;

import cl.duoc.nevulaback.tablas.model.RecordatorioEvento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordatorioEventoRepository extends JpaRepository<RecordatorioEvento, Long> {

    List<RecordatorioEvento> findByRecordatorioIdOrderByFechaCreacionDesc(Long recordatorioId);

    List<RecordatorioEvento> findByRecordatorioUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<RecordatorioEvento> findAllByOrderByFechaCreacionDesc();
}