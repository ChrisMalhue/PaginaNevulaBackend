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
class EventoServiceTest {

    @Mock
    private EventoRepository repository;

    @Mock
    private LocalService localService;

    @Mock
    private EstadoService estadoService;

    @Mock
    private ImagenService imagenService;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private EventoService service;

    @Test
    void testObtenerTodos() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        when(repository.findAllByOrderByFechaDesc()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, null));
    }

    @Test
    void testBuscarPorId() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
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
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testFiltro1() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        when(repository.findByEstadoIdOrderByFechaDesc(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(2L, null));
    }

    @Test
    void testFiltro2() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        when(repository.findByLocalIdOrderByFechaDesc(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, 2L));
    }

    @Test
    void testGuardar() {
        Evento datos = new Evento();
        datos.setId(1L);
        datos.setFecha(LocalDate.now().plusDays(6));
        datos.setHoraInicio(LocalTime.of(21, 0));
        datos.setHoraFin(LocalTime.of(21, 0));
        Local datosLocal = new Local();
        datosLocal.setId(1L);
        datosLocal.setNombre("Ejemplo");
        datosLocal.setDireccion("Ejemplo");
        datosLocal.setMapaUrl("https://example.com/demo");
        Comuna datosLocalComuna = new Comuna();
        datosLocalComuna.setId(1L);
        datosLocalComuna.setNombre("Ejemplo");
        Region datosLocalComunaRegion = new Region();
        datosLocalComunaRegion.setId(1L);
        datosLocalComunaRegion.setNombre("Ejemplo");
        datosLocalComuna.setRegion(datosLocalComunaRegion);
        datosLocal.setComuna(datosLocalComuna);
        datos.setLocal(datosLocal);
        Estado datosEstado = new Estado();
        datosEstado.setId(1L);
        datosEstado.setNombre("Programado");
        datos.setEstado(datosEstado);
        Imagen datosImagen = new Imagen();
        datosImagen.setId(1L);
        datosImagen.setUrl("https://example.com/demo");
        datosImagen.setDescripcion("Ejemplo");
        datos.setImagen(datosImagen);
        datos.setLinkEntradas("https://example.com/nuevo");
        datos.setGenero("Nuevo");
        when(localService.buscarPorId(datos.getLocal().getId())).thenReturn(datos.getLocal());
        when(estadoService.buscarPorId(datos.getEstado().getId())).thenReturn(datos.getEstado());
        when(imagenService.buscarPorId(datos.getImagen().getId())).thenReturn(datos.getImagen());
        when(repository.save(any(Evento.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(repository).save(same(resultado));
    }

    @Test
    void testActualizar() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        Evento datos = new Evento();
        datos.setId(1L);
        datos.setFecha(LocalDate.now().plusDays(6));
        datos.setHoraInicio(LocalTime.of(21, 0));
        datos.setHoraFin(LocalTime.of(21, 0));
        Local datosLocal = new Local();
        datosLocal.setId(1L);
        datosLocal.setNombre("Ejemplo");
        datosLocal.setDireccion("Ejemplo");
        datosLocal.setMapaUrl("https://example.com/demo");
        Comuna datosLocalComuna = new Comuna();
        datosLocalComuna.setId(1L);
        datosLocalComuna.setNombre("Ejemplo");
        Region datosLocalComunaRegion = new Region();
        datosLocalComunaRegion.setId(1L);
        datosLocalComunaRegion.setNombre("Ejemplo");
        datosLocalComuna.setRegion(datosLocalComunaRegion);
        datosLocal.setComuna(datosLocalComuna);
        datos.setLocal(datosLocal);
        Estado datosEstado = new Estado();
        datosEstado.setId(1L);
        datosEstado.setNombre("Programado");
        datos.setEstado(datosEstado);
        Imagen datosImagen = new Imagen();
        datosImagen.setId(1L);
        datosImagen.setUrl("https://example.com/demo");
        datosImagen.setDescripcion("Ejemplo");
        datos.setImagen(datosImagen);
        datos.setLinkEntradas("https://example.com/nuevo");
        datos.setGenero("Nuevo");
        when(localService.buscarPorId(datos.getLocal().getId())).thenReturn(datos.getLocal());
        when(estadoService.buscarPorId(datos.getEstado().getId())).thenReturn(datos.getEstado());
        when(imagenService.buscarPorId(datos.getImagen().getId())).thenReturn(datos.getImagen());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Evento.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getGenero(), resultado.getGenero());
    }

    @Test
    void testActualizarParcial() {
        Evento existente = new Evento();
        existente.setId(1L);
        existente.setFecha(LocalDate.now().plusDays(5));
        existente.setHoraInicio(LocalTime.of(20, 0));
        existente.setHoraFin(LocalTime.of(20, 0));
        Local existenteLocal = new Local();
        existenteLocal.setId(1L);
        existenteLocal.setNombre("Ejemplo");
        existenteLocal.setDireccion("Ejemplo");
        existenteLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteLocalComuna = new Comuna();
        existenteLocalComuna.setId(1L);
        existenteLocalComuna.setNombre("Ejemplo");
        Region existenteLocalComunaRegion = new Region();
        existenteLocalComunaRegion.setId(1L);
        existenteLocalComunaRegion.setNombre("Ejemplo");
        existenteLocalComuna.setRegion(existenteLocalComunaRegion);
        existenteLocal.setComuna(existenteLocalComuna);
        existente.setLocal(existenteLocal);
        Estado existenteEstado = new Estado();
        existenteEstado.setId(1L);
        existenteEstado.setNombre("Programado");
        existente.setEstado(existenteEstado);
        Imagen existenteImagen = new Imagen();
        existenteImagen.setId(1L);
        existenteImagen.setUrl("https://example.com/demo");
        existenteImagen.setDescripcion("Ejemplo");
        existente.setImagen(existenteImagen);
        existente.setLinkEntradas("https://example.com/demo");
        existente.setGenero("Ejemplo");
        Evento datos = new Evento();
        datos.setGenero("Nuevo");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Evento.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getGenero(), resultado.getGenero());
        verify(validacion).validar(existente);
    }

    @Test
    void testGuardarSinRelacion() {
        var error = assertThrows(ResponseStatusException.class, () -> service.guardar(new Evento()));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repository, never()).save(any());
    }
}
