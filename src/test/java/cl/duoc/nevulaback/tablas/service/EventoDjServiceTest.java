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
class EventoDjServiceTest {

    @Mock
    private EventoDjRepository repository;

    @Mock
    private EventoService eventoService;

    @Mock
    private DjService djService;

    @Mock
    private Validacion validacion;

    @InjectMocks
    private EventoDjService service;

    @Test
    void testObtenerTodos() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        when(repository.findAllByOrderByEventoIdAscOrdenAsc()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, null));
    }

    @Test
    void testBuscarPorId() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
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
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testFiltro1() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        when(repository.findByEventoIdOrderByOrdenAsc(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(2L, null));
    }

    @Test
    void testFiltro2() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        when(repository.findByDjIdOrderByEventoFechaDesc(2L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, 2L));
    }

    @Test
    void testGuardar() {
        EventoDj datos = new EventoDj();
        datos.setId(1L);
        Evento datosEvento = new Evento();
        datosEvento.setId(1L);
        datosEvento.setFecha(LocalDate.now().plusDays(5));
        datosEvento.setHoraInicio(LocalTime.of(20, 0));
        datosEvento.setHoraFin(LocalTime.of(20, 0));
        Local datosEventoLocal = new Local();
        datosEventoLocal.setId(1L);
        datosEventoLocal.setNombre("Ejemplo");
        datosEventoLocal.setDireccion("Ejemplo");
        datosEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna datosEventoLocalComuna = new Comuna();
        datosEventoLocalComuna.setId(1L);
        datosEventoLocalComuna.setNombre("Ejemplo");
        Region datosEventoLocalComunaRegion = new Region();
        datosEventoLocalComunaRegion.setId(1L);
        datosEventoLocalComunaRegion.setNombre("Ejemplo");
        datosEventoLocalComuna.setRegion(datosEventoLocalComunaRegion);
        datosEventoLocal.setComuna(datosEventoLocalComuna);
        datosEvento.setLocal(datosEventoLocal);
        Estado datosEventoEstado = new Estado();
        datosEventoEstado.setId(1L);
        datosEventoEstado.setNombre("Programado");
        datosEvento.setEstado(datosEventoEstado);
        Imagen datosEventoImagen = new Imagen();
        datosEventoImagen.setId(1L);
        datosEventoImagen.setUrl("https://example.com/demo");
        datosEventoImagen.setDescripcion("Ejemplo");
        datosEvento.setImagen(datosEventoImagen);
        datosEvento.setLinkEntradas("https://example.com/demo");
        datosEvento.setGenero("Ejemplo");
        datos.setEvento(datosEvento);
        Dj datosDj = new Dj();
        datosDj.setId(1L);
        datosDj.setNombre("Ejemplo");
        datos.setDj(datosDj);
        datos.setOrden(2);
        when(eventoService.buscarPorId(datos.getEvento().getId())).thenReturn(datos.getEvento());
        when(djService.buscarPorId(datos.getDj().getId())).thenReturn(datos.getDj());
        when(repository.saveAndFlush(any(EventoDj.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertNotNull(resultado);
        assertNull(resultado.getId());
        verify(repository).saveAndFlush(same(resultado));
    }

    @Test
    void testActualizar() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        EventoDj datos = new EventoDj();
        datos.setId(1L);
        Evento datosEvento = new Evento();
        datosEvento.setId(1L);
        datosEvento.setFecha(LocalDate.now().plusDays(5));
        datosEvento.setHoraInicio(LocalTime.of(20, 0));
        datosEvento.setHoraFin(LocalTime.of(20, 0));
        Local datosEventoLocal = new Local();
        datosEventoLocal.setId(1L);
        datosEventoLocal.setNombre("Ejemplo");
        datosEventoLocal.setDireccion("Ejemplo");
        datosEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna datosEventoLocalComuna = new Comuna();
        datosEventoLocalComuna.setId(1L);
        datosEventoLocalComuna.setNombre("Ejemplo");
        Region datosEventoLocalComunaRegion = new Region();
        datosEventoLocalComunaRegion.setId(1L);
        datosEventoLocalComunaRegion.setNombre("Ejemplo");
        datosEventoLocalComuna.setRegion(datosEventoLocalComunaRegion);
        datosEventoLocal.setComuna(datosEventoLocalComuna);
        datosEvento.setLocal(datosEventoLocal);
        Estado datosEventoEstado = new Estado();
        datosEventoEstado.setId(1L);
        datosEventoEstado.setNombre("Programado");
        datosEvento.setEstado(datosEventoEstado);
        Imagen datosEventoImagen = new Imagen();
        datosEventoImagen.setId(1L);
        datosEventoImagen.setUrl("https://example.com/demo");
        datosEventoImagen.setDescripcion("Ejemplo");
        datosEvento.setImagen(datosEventoImagen);
        datosEvento.setLinkEntradas("https://example.com/demo");
        datosEvento.setGenero("Ejemplo");
        datos.setEvento(datosEvento);
        Dj datosDj = new Dj();
        datosDj.setId(1L);
        datosDj.setNombre("Ejemplo");
        datos.setDj(datosDj);
        datos.setOrden(2);
        when(eventoService.buscarPorId(datos.getEvento().getId())).thenReturn(datos.getEvento());
        when(djService.buscarPorId(datos.getDj().getId())).thenReturn(datos.getDj());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(EventoDj.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizar(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getOrden(), resultado.getOrden());
    }

    @Test
    void testActualizarParcial() {
        EventoDj existente = new EventoDj();
        existente.setId(1L);
        Evento existenteEvento = new Evento();
        existenteEvento.setId(1L);
        existenteEvento.setFecha(LocalDate.now().plusDays(5));
        existenteEvento.setHoraInicio(LocalTime.of(20, 0));
        existenteEvento.setHoraFin(LocalTime.of(20, 0));
        Local existenteEventoLocal = new Local();
        existenteEventoLocal.setId(1L);
        existenteEventoLocal.setNombre("Ejemplo");
        existenteEventoLocal.setDireccion("Ejemplo");
        existenteEventoLocal.setMapaUrl("https://example.com/demo");
        Comuna existenteEventoLocalComuna = new Comuna();
        existenteEventoLocalComuna.setId(1L);
        existenteEventoLocalComuna.setNombre("Ejemplo");
        Region existenteEventoLocalComunaRegion = new Region();
        existenteEventoLocalComunaRegion.setId(1L);
        existenteEventoLocalComunaRegion.setNombre("Ejemplo");
        existenteEventoLocalComuna.setRegion(existenteEventoLocalComunaRegion);
        existenteEventoLocal.setComuna(existenteEventoLocalComuna);
        existenteEvento.setLocal(existenteEventoLocal);
        Estado existenteEventoEstado = new Estado();
        existenteEventoEstado.setId(1L);
        existenteEventoEstado.setNombre("Programado");
        existenteEvento.setEstado(existenteEventoEstado);
        Imagen existenteEventoImagen = new Imagen();
        existenteEventoImagen.setId(1L);
        existenteEventoImagen.setUrl("https://example.com/demo");
        existenteEventoImagen.setDescripcion("Ejemplo");
        existenteEvento.setImagen(existenteEventoImagen);
        existenteEvento.setLinkEntradas("https://example.com/demo");
        existenteEvento.setGenero("Ejemplo");
        existente.setEvento(existenteEvento);
        Dj existenteDj = new Dj();
        existenteDj.setId(1L);
        existenteDj.setNombre("Ejemplo");
        existente.setDj(existenteDj);
        existente.setOrden(1);
        EventoDj datos = new EventoDj();
        datos.setOrden(2);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.saveAndFlush(any(EventoDj.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.actualizarParcial(1L, datos);
        assertSame(existente, resultado);
        assertEquals(datos.getOrden(), resultado.getOrden());
        verify(validacion).validar(existente);
    }

    @Test
    void testGuardarSinRelacion() {
        var error = assertThrows(ResponseStatusException.class, () -> service.guardar(new EventoDj()));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repository, never()).saveAndFlush(any());
    }
}
