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
class RecordatorioEventoServiceTest {

    @Mock
    private RecordatorioEventoRepository repository;

    @Mock
    private RecordatorioService recordatorioService;

    @Mock
    private EventoService eventoService;

    @Mock
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @InjectMocks
    private RecordatorioEventoService service;

    @Test
    void testObtenerTodos() {
        RecordatorioEvento existente = new RecordatorioEvento();
        existente.setId(1L);
        Recordatorio existenteRecordatorio = new Recordatorio();
        existenteRecordatorio.setId(1L);
        Usuario existenteRecordatorioUsuario = new Usuario();
        existenteRecordatorioUsuario.setId(1L);
        existenteRecordatorioUsuario.setNombreUsuario("Ejemplo");
        existenteRecordatorioUsuario.setCorreo("demo@example.com");
        existenteRecordatorioUsuario.setContrasena("clave1234");
        Rol existenteRecordatorioUsuarioRol = new Rol();
        existenteRecordatorioUsuarioRol.setId(1L);
        existenteRecordatorioUsuarioRol.setNombre("USUARIO");
        existenteRecordatorioUsuario.setRol(existenteRecordatorioUsuarioRol);
        existenteRecordatorio.setUsuario(existenteRecordatorioUsuario);
        existente.setRecordatorio(existenteRecordatorio);
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
        Usuario actual = new Usuario();
        actual.setId(1L);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(actual);
        when(usuarioAutenticadoService.esAdmin(actual)).thenReturn(true);
        when(repository.findAllByOrderByFechaCreacionDesc()).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, null, null));
    }

    @Test
    void testBuscarPorId() {
        RecordatorioEvento existente = new RecordatorioEvento();
        existente.setId(1L);
        Recordatorio existenteRecordatorio = new Recordatorio();
        existenteRecordatorio.setId(1L);
        Usuario existenteRecordatorioUsuario = new Usuario();
        existenteRecordatorioUsuario.setId(1L);
        existenteRecordatorioUsuario.setNombreUsuario("Ejemplo");
        existenteRecordatorioUsuario.setCorreo("demo@example.com");
        existenteRecordatorioUsuario.setContrasena("clave1234");
        Rol existenteRecordatorioUsuarioRol = new Rol();
        existenteRecordatorioUsuarioRol.setId(1L);
        existenteRecordatorioUsuarioRol.setNombre("USUARIO");
        existenteRecordatorioUsuario.setRol(existenteRecordatorioUsuarioRol);
        existenteRecordatorio.setUsuario(existenteRecordatorioUsuario);
        existente.setRecordatorio(existenteRecordatorio);
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
        RecordatorioEvento existente = new RecordatorioEvento();
        existente.setId(1L);
        Recordatorio existenteRecordatorio = new Recordatorio();
        existenteRecordatorio.setId(1L);
        Usuario existenteRecordatorioUsuario = new Usuario();
        existenteRecordatorioUsuario.setId(1L);
        existenteRecordatorioUsuario.setNombreUsuario("Ejemplo");
        existenteRecordatorioUsuario.setCorreo("demo@example.com");
        existenteRecordatorioUsuario.setContrasena("clave1234");
        Rol existenteRecordatorioUsuarioRol = new Rol();
        existenteRecordatorioUsuarioRol.setId(1L);
        existenteRecordatorioUsuarioRol.setNombre("USUARIO");
        existenteRecordatorioUsuario.setRol(existenteRecordatorioUsuarioRol);
        existenteRecordatorio.setUsuario(existenteRecordatorioUsuario);
        existente.setRecordatorio(existenteRecordatorio);
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
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        service.eliminar(1L);
        verify(repository).delete(existente);
        verify(repository).flush();
    }

    @Test
    void testUsuarioSoloVeSusDatos() {
        RecordatorioEvento existente = new RecordatorioEvento();
        existente.setId(1L);
        Recordatorio existenteRecordatorio = new Recordatorio();
        existenteRecordatorio.setId(1L);
        Usuario existenteRecordatorioUsuario = new Usuario();
        existenteRecordatorioUsuario.setId(1L);
        existenteRecordatorioUsuario.setNombreUsuario("Ejemplo");
        existenteRecordatorioUsuario.setCorreo("demo@example.com");
        existenteRecordatorioUsuario.setContrasena("clave1234");
        Rol existenteRecordatorioUsuarioRol = new Rol();
        existenteRecordatorioUsuarioRol.setId(1L);
        existenteRecordatorioUsuarioRol.setNombre("USUARIO");
        existenteRecordatorioUsuario.setRol(existenteRecordatorioUsuarioRol);
        existenteRecordatorio.setUsuario(existenteRecordatorioUsuario);
        existente.setRecordatorio(existenteRecordatorio);
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
        Usuario actual = new Usuario();
        actual.setId(1L);
        when(usuarioAutenticadoService.obtenerUsuarioActual()).thenReturn(actual);
        when(usuarioAutenticadoService.esAdmin(actual)).thenReturn(false);
        when(repository.findByRecordatorioUsuarioIdOrderByFechaCreacionDesc(1L)).thenReturn(List.of(existente));
        assertEquals(List.of(existente), service.obtenerTodos(null, 99L, null));
        verify(repository).findByRecordatorioUsuarioIdOrderByFechaCreacionDesc(1L);
    }

    @Test
    void testGuardar() {
        RecordatorioEvento datos = new RecordatorioEvento();
        datos.setId(1L);
        Recordatorio datosRecordatorio = new Recordatorio();
        datosRecordatorio.setId(1L);
        Usuario datosRecordatorioUsuario = new Usuario();
        datosRecordatorioUsuario.setId(1L);
        datosRecordatorioUsuario.setNombreUsuario("Ejemplo");
        datosRecordatorioUsuario.setCorreo("demo@example.com");
        datosRecordatorioUsuario.setContrasena("clave1234");
        Rol datosRecordatorioUsuarioRol = new Rol();
        datosRecordatorioUsuarioRol.setId(1L);
        datosRecordatorioUsuarioRol.setNombre("USUARIO");
        datosRecordatorioUsuario.setRol(datosRecordatorioUsuarioRol);
        datosRecordatorio.setUsuario(datosRecordatorioUsuario);
        datos.setRecordatorio(datosRecordatorio);
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
        when(recordatorioService.buscarPorId(datos.getRecordatorio().getId())).thenReturn(datos.getRecordatorio());
        when(eventoService.buscarPorId(datos.getEvento().getId())).thenReturn(datos.getEvento());
        when(repository.saveAndFlush(any(RecordatorioEvento.class))).thenAnswer(inv -> inv.getArgument(0));
        var resultado = service.guardar(datos);
        assertSame(datosRecordatorio, resultado.getRecordatorio());
        assertSame(datosEvento, resultado.getEvento());
    }

    @Test
    void testGuardarSinRelacion() {
        var error = assertThrows(ResponseStatusException.class, () -> service.guardar(new RecordatorioEvento()));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repository, never()).saveAndFlush(any());
    }
}
