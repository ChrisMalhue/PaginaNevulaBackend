package cl.duoc.nevulaback.tablas.repository;

import cl.duoc.nevulaback.tablas.model.Recordatorio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {

    List<Recordatorio> findByUsuarioId(Long usuarioId);
}