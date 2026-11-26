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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private RolService rolService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private UsuarioService service;

    @Test
    void testObtenerTodos() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findAll()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, null));
    }

    @Test
    void testBuscarPorId() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
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
    void testActualizarNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> service.actualizar(99L, null));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void testActualizarParcialNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> service.actualizarParcial(99L, null));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void testEliminar() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testFiltro1() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findByNombreUsuarioContainingIgnoreCaseAndRolId("abc", 2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos("abc", 2L));
    }

    @Test
    void testFiltro2() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findByNombreUsuarioContainingIgnoreCase("abc")).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos("abc", null));
    }

    @Test
    void testFiltro3() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findByRolId(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, 2L));
    }

    @Test
    void testGuardar() {
        UsuarioDTO datos = new UsuarioDTO();
        datos.setNombreUsuario("  Nuevo  ");
        datos.setCorreo("  NUEVO@EXAMPLE.COM  ");
        datos.setContrasena("clave1234");
        Rol rol = new Rol("USUARIO");
        when(rolService.buscarPorNombre("USUARIO")).thenReturn(rol);
        when(passwordEncoder.encode("clave1234")).thenReturn("hash");
        when(repository.saveAndFlush(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertEquals("Nuevo", resultado.getNombreUsuario());
        assertEquals("nuevo@example.com", resultado.getCorreo());
        assertEquals("hash", resultado.getContrasena());
        assertSame(rol, resultado.getRol());
    }

    @Test
    void testActualizar() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        UsuarioDTO datos = new UsuarioDTO();
        datos.setNombreUsuario("  Nuevo  ");
        datos.setCorreo("  NUEVO@EXAMPLE.COM  ");
        datos.setContrasena("clave1234");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("clave1234")).thenReturn("hash");
        when(repository.saveAndFlush(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals("Nuevo", resultado.getNombreUsuario());
        assertEquals("hash", resultado.getContrasena());
        assertSame(existenteRol, resultado.getRol());
    }

    @Test
    void testActualizarParcial() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        UsuarioDTO datos = new UsuarioDTO();
        datos.setNombreUsuario("  Nuevo  ");
        datos.setCorreo("  NUEVO@EXAMPLE.COM  ");
        datos.setContrasena("clave1234");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("clave1234")).thenReturn("hash");
        when(repository.saveAndFlush(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals("Nuevo", resultado.getNombreUsuario());
        assertEquals("hash", resultado.getContrasena());
        assertSame(existenteRol, resultado.getRol());
    }

    @Test
    void testContrasenaCorta() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombreUsuario("Ejemplo");
        existente.setCorreo("demo@example.com");
        existente.setContrasena("clave1234");
        Rol existenteRol = new Rol();
        existenteRol.setId(1L);
        existenteRol.setNombre("USUARIO");
        existente.setRol(existenteRol);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        UsuarioDTO datos = new UsuarioDTO();
        datos.setContrasena("123");
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class, () -> service.actualizarParcial(1L, datos)).getStatusCode());
        verify(repository, never()).saveAndFlush(any());
    }
}
