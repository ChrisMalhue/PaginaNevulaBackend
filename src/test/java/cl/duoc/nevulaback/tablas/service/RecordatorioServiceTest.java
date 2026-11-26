package cl.duoc.nevulaback.tablas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import cl.duoc.nevulaback.config.*;
import cl.duoc.nevulaback.tablas.model.*;
import cl.duoc.nevulaback.tablas.repository.*;
import cl.duoc.nevulaback.tablas.dto.UsuarioDTO;

@ExtendWith(MockitoExtension.class)
class RecordatorioServiceTest {

    @Mock
    private RecordatorioRepository repository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @InjectMocks
    private RecordatorioService service;

    @Test
    void testObtenerTodos() {
        Recordatorio existente = new Recordatorio();
        existente.setId(1L);
        Usuario existenteUsuario = new Usuario();
        existenteUsuario.setId(1L);
        existenteUsuario.setNombreUsuario("Ejemplo");
        existenteUsuario.setCorreo("demo@example.com");
        existenteUsuario.setContrasena("clave1234");
        Rol existenteUsuarioRol = new Rol();
        existenteUsuarioRol.setId(1L);
        existenteUsuarioRol.setNombre("USUARIO");
        existenteUsuario.setRol(existenteUsuarioRol);
        existente.setUsuario(existenteUsuario);
        Usuario actual = new Usuario();
        actual.setId(1L);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(actual);
        when(usuarioAutenticadoService.esAdmin(actual)).thenReturn(true);
        when(repository.findAll()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null));
    }

    @Test
    void testBuscarPorId() {
        Recordatorio existente = new Recordatorio();
        existente.setId(1L);
        Usuario existenteUsuario = new Usuario();
        existenteUsuario.setId(1L);
        existenteUsuario.setNombreUsuario("Ejemplo");
        existenteUsuario.setCorreo("demo@example.com");
        existenteUsuario.setContrasena("clave1234");
        Rol existenteUsuarioRol = new Rol();
        existenteUsuarioRol.setId(1L);
        existenteUsuarioRol.setNombre("USUARIO");
        existenteUsuario.setRol(existenteUsuarioRol);
        existente.setUsuario(existenteUsuario);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        assertSame(existente, service.buscarPorId(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testBuscarPorIdNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> service.buscarPorId(99L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void testEliminarNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> service.eliminar(99L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void testEliminar() {
        Recordatorio existente = new Recordatorio();
        existente.setId(1L);
        Usuario existenteUsuario = new Usuario();
        existenteUsuario.setId(1L);
        existenteUsuario.setNombreUsuario("Ejemplo");
        existenteUsuario.setCorreo("demo@example.com");
        existenteUsuario.setContrasena("clave1234");
        Rol existenteUsuarioRol = new Rol();
        existenteUsuarioRol.setId(1L);
        existenteUsuarioRol.setNombre("USUARIO");
        existenteUsuario.setRol(existenteUsuarioRol);
        existente.setUsuario(existenteUsuario);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testUsuarioSoloVeSusDatos() {
        Recordatorio existente = new Recordatorio();
        existente.setId(1L);
        Usuario existenteUsuario = new Usuario();
        existenteUsuario.setId(1L);
        existenteUsuario.setNombreUsuario("Ejemplo");
        existenteUsuario.setCorreo("demo@example.com");
        existenteUsuario.setContrasena("clave1234");
        Rol existenteUsuarioRol = new Rol();
        existenteUsuarioRol.setId(1L);
        existenteUsuarioRol.setNombre("USUARIO");
        existenteUsuario.setRol(existenteUsuarioRol);
        existente.setUsuario(existenteUsuario);
        Usuario actual = new Usuario();
        actual.setId(1L);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(actual);
        when(usuarioAutenticadoService.esAdmin(actual)).thenReturn(false);
        when(repository.findByUsuarioId(1L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(99L));
        verify(repository).findByUsuarioId(1L);
    }

    @Test
    void testGuardar() {
        Usuario actual = new Usuario();
        actual.setId(1L);
        actual.setNombreUsuario("Ejemplo");
        actual.setCorreo("demo@example.com");
        actual.setContrasena("clave1234");
        Rol actualRol = new Rol();
        actualRol.setId(1L);
        actualRol.setNombre("USUARIO");
        actual.setRol(actualRol);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(actual);
        when(repository.saveAndFlush(any(Recordatorio.class))).thenAnswer(inv -> inv.getArgument(0));
        assertSame(actual, service.guardar().getUsuario());
    }
}
