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
class ImagenServiceTest {

    @Mock
    private ImagenRepository repository;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private ImagenService service;

    @Test
    void testObtenerTodos() {
        Imagen existente = new Imagen();
        existente.setId(1L);
        existente.setUrl("https://example.com/demo");
        existente.setDescripcion("Ejemplo");
        when(repository.findAll()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos());
    }

    @Test
    void testBuscarPorId() {
        Imagen existente = new Imagen();
        existente.setId(1L);
        existente.setUrl("https://example.com/demo");
        existente.setDescripcion("Ejemplo");
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
        Imagen existente = new Imagen();
        existente.setId(1L);
        existente.setUrl("https://example.com/demo");
        existente.setDescripcion("Ejemplo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testGuardar() {
        Imagen datos = new Imagen();
        datos.setId(1L);
        datos.setUrl("https://example.com/nuevo");
        datos.setDescripcion("Nuevo");
        when(repository.saveAndFlush(any(Imagen.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(repository).saveAndFlush(same(resultado));
    }

    @Test
    void testActualizar() {
        Imagen existente = new Imagen();
        existente.setId(1L);
        existente.setUrl("https://example.com/demo");
        existente.setDescripcion("Ejemplo");
        Imagen datos = new Imagen();
        datos.setId(1L);
        datos.setUrl("https://example.com/nuevo");
        datos.setDescripcion("Nuevo");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(Imagen.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getUrl(), resultado.getUrl());
    }

    @Test
    void testActualizarParcial() {
        Imagen existente = new Imagen();
        existente.setId(1L);
        existente.setUrl("https://example.com/demo");
        existente.setDescripcion("Ejemplo");
        Imagen datos = new Imagen();
        datos.setUrl("https://example.com/nuevo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(Imagen.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getUrl(), resultado.getUrl());
        verify(validacion).validar(existente);
    }
}
