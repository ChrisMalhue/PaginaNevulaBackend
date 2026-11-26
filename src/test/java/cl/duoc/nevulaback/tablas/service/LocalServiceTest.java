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
class LocalServiceTest {

    @Mock
    private LocalRepository repository;

    @Mock
    private ComunaService comunaService;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private LocalService service;

    @Test
    void testObtenerTodos() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        when(repository.findAll()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, null));
    }

    @Test
    void testBuscarPorId() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
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
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testFiltro1() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        when(repository.findByComunaId(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, 2L));
    }

    @Test
    void testFiltro2() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        when(repository.findByNombreContainingIgnoreCase("abc")).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos("abc", null));
    }

    @Test
    void testGuardar() {
        Local datos = new Local();
        datos.setId(1L);
        datos.setNombre("Nuevo");
        datos.setDireccion("Nuevo");
        datos.setMapaUrl("https://example.com/nuevo");
        Comuna datosComuna = new Comuna();
        datosComuna.setId(1L);
        datosComuna.setNombre("Ejemplo");
        Region datosComunaRegion = new Region();
        datosComunaRegion.setId(1L);
        datosComunaRegion.setNombre("Ejemplo");
        datosComuna.setRegion(datosComunaRegion);
        datos.setComuna(datosComuna);
        when(comunaService.buscarPorId(datos.getComuna().getId())).thenReturn(datos.getComuna());
        when(repository.save(any(Local.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(repository).save(same(resultado));
    }

    @Test
    void testActualizar() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        Local datos = new Local();
        datos.setId(1L);
        datos.setNombre("Nuevo");
        datos.setDireccion("Nuevo");
        datos.setMapaUrl("https://example.com/nuevo");
        Comuna datosComuna = new Comuna();
        datosComuna.setId(1L);
        datosComuna.setNombre("Ejemplo");
        Region datosComunaRegion = new Region();
        datosComunaRegion.setId(1L);
        datosComunaRegion.setNombre("Ejemplo");
        datosComuna.setRegion(datosComunaRegion);
        datos.setComuna(datosComuna);
        when(comunaService.buscarPorId(datos.getComuna().getId())).thenReturn(datos.getComuna());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Local.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getNombre(), resultado.getNombre());
    }

    @Test
    void testActualizarParcial() {
        Local existente = new Local();
        existente.setId(1L);
        existente.setNombre("Ejemplo");
        existente.setDireccion("Ejemplo");
        existente.setMapaUrl("https://example.com/demo");
        Comuna existenteComuna = new Comuna();
        existenteComuna.setId(1L);
        existenteComuna.setNombre("Ejemplo");
        Region existenteComunaRegion = new Region();
        existenteComunaRegion.setId(1L);
        existenteComunaRegion.setNombre("Ejemplo");
        existenteComuna.setRegion(existenteComunaRegion);
        existente.setComuna(existenteComuna);
        Local datos = new Local();
        datos.setNombre("Nuevo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Local.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getNombre(), resultado.getNombre());
        verify(validacion).validar(existente);
    }

    @Test
    void testGuardarSinRelacion() {
        var error = assertThrows(ResponseStatusException.class, () -> service.guardar(new Local()));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repository, never()).save(any());
    }
}
