package cl.duoc.nevulaback.tablas.repository;

import cl.duoc.nevulaback.tablas.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);

    List<Usuario> findByRolId(Long rolId);

    List<Usuario> findByNombreUsuarioContainingIgnoreCaseAndRolId(String nombreUsuario, Long rolId);
}