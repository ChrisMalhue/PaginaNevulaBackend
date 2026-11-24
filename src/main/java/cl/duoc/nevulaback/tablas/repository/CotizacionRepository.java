package cl.duoc.nevulaback.tablas.repository;

import cl.duoc.nevulaback.tablas.model.Cotizacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Cotizacion> findAllByOrderByFechaCreacionDesc();
}