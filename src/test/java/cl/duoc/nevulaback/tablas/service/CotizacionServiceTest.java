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
class CotizacionServiceTest {

    @Mock
    private CotizacionRepository repository;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private CotizacionService service;

    @Test
    void testObtenerTodos() {
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        when(repository.findAllByOrderByFechaCreacionDesc()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null));
    }

    @Test
    void testBuscarPorId() {
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        when(repository.findByUsuarioIdOrderByFechaCreacionDesc(1L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(99L));
        verify(repository).findByUsuarioIdOrderByFechaCreacionDesc(1L);
    }

    @Test
    void testGuardar() {
        Cotizacion datos = new Cotizacion();
        datos.setId(1L);
        datos.setNombre("Nuevo");
        datos.setTelefono("+56912345678");
        datos.setFechaEvento(LocalDate.now().plusDays(6));
        datos.setDescripcion("Nuevo");
        Usuario datosUsuario = new Usuario();
        datosUsuario.setId(1L);
        datosUsuario.setNombreUsuario("Ejemplo");
        datosUsuario.setCorreo("demo@example.com");
        datosUsuario.setContrasena("clave1234");
        Rol datosUsuarioRol = new Rol();
        datosUsuarioRol.setId(1L);
        datosUsuarioRol.setNombre("USUARIO");
        datosUsuario.setRol(datosUsuarioRol);
        datos.setUsuario(datosUsuario);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(datosUsuario);
        when(repository.saveAndFlush(any(Cotizacion.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(repository).saveAndFlush(same(resultado));
    }

    @Test
    void testActualizar() {
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        Cotizacion datos = new Cotizacion();
        datos.setId(1L);
        datos.setNombre("Nuevo");
        datos.setTelefono("+56912345678");
        datos.setFechaEvento(LocalDate.now().plusDays(6));
        datos.setDescripcion("Nuevo");
        Usuario datosUsuario = new Usuario();
        datosUsuario.setId(1L);
        datosUsuario.setNombreUsuario("Ejemplo");
        datosUsuario.setCorreo("demo@example.com");
        datosUsuario.setContrasena("clave1234");
        Rol datosUsuarioRol = new Rol();
        datosUsuarioRol.setId(1L);
        datosUsuarioRol.setNombre("USUARIO");
        datosUsuario.setRol(datosUsuarioRol);
        datos.setUsuario(datosUsuario);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(Cotizacion.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getNombre(), resultado.getNombre());
    }

    @Test
    void testActualizarParcial() {
        Cotizacion existente = new Cotizacion();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setTelefono("+56912345678");
        existente.setFechaEvento(LocalDate.now().plusDays(5));
        existente.setDescripcion("Ejemplo");
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
        Cotizacion datos = new Cotizacion();
        datos.setNombre("Nuevo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(Cotizacion.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getNombre(), resultado.getNombre());
        verify(validacion).validar(existente);
    }
}
