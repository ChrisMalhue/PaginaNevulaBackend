package cl.duoc.nevulaback.config;

import cl.duoc.nevulaback.tablas.model.Comuna;
import cl.duoc.nevulaback.tablas.model.Dj;
import cl.duoc.nevulaback.tablas.model.Estado;
import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.model.EventoDj;
import cl.duoc.nevulaback.tablas.model.Imagen;
import cl.duoc.nevulaback.tablas.model.Local;
import cl.duoc.nevulaback.tablas.model.Region;
import cl.duoc.nevulaback.tablas.model.Rol;
import cl.duoc.nevulaback.tablas.model.Usuario;
import cl.duoc.nevulaback.tablas.repository.ComunaRepository;
import cl.duoc.nevulaback.tablas.repository.DjRepository;
import cl.duoc.nevulaback.tablas.repository.EstadoRepository;
import cl.duoc.nevulaback.tablas.repository.EventoDjRepository;
import cl.duoc.nevulaback.tablas.repository.EventoRepository;
import cl.duoc.nevulaback.tablas.repository.ImagenRepository;
import cl.duoc.nevulaback.tablas.repository.LocalRepository;
import cl.duoc.nevulaback.tablas.repository.RegionRepository;
import cl.duoc.nevulaback.tablas.repository.RolRepository;
import cl.duoc.nevulaback.tablas.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final LocalRepository localRepository;
    private final EstadoRepository estadoRepository;
    private final ImagenRepository imagenRepository;
    private final EventoRepository eventoRepository;
    private final DjRepository djRepository;
    private final EventoDjRepository eventoDjRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;
    private final String usuarioPassword;

    public DatosIniciales(
        RegionRepository regionRepository,
        ComunaRepository comunaRepository,
        LocalRepository localRepository,
        EstadoRepository estadoRepository,
        ImagenRepository imagenRepository,
        EventoRepository eventoRepository,
        DjRepository djRepository,
        EventoDjRepository eventoDjRepository,
        RolRepository rolRepository,
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        @Value("${ADMIN_PASSWORD:}") String adminPassword,
        @Value("${USUARIO_PASSWORD:}") String usuarioPassword
    ) {
        this.regionRepository = regionRepository;
        this.comunaRepository = comunaRepository;
        this.localRepository = localRepository;
        this.estadoRepository = estadoRepository;
        this.imagenRepository = imagenRepository;
        this.eventoRepository = eventoRepository;
        this.djRepository = djRepository;
        this.eventoDjRepository = eventoDjRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
        this.usuarioPassword = usuarioPassword;
    }

    @Override
    public void run(String... args) {

        // Si ya hay datos cargados, no hace nada (para limpiar: TRUNCATE de todas las tablas).
        if (regionRepository.count() > 0) {
            return;
        }

        // 1) Regiones
        Region arica = regionRepository.save(new Region("Arica y Parinacota"));
        Region metropolitana = regionRepository.save(new Region("Metropolitana"));

        // 2) Comunas
        comunaRepository.saveAll(List.of(
            new Comuna("Arica", arica),
            new Comuna("Camarones", arica),
            new Comuna("Maipú", metropolitana),
            new Comuna("Puente Alto", metropolitana)
        ));

        Comuna santiago = comunaRepository.save(new Comuna("Santiago", metropolitana));
        Comuna providencia = comunaRepository.save(new Comuna("Providencia", metropolitana));
        Comuna lasCondes = comunaRepository.save(new Comuna("Las Condes", metropolitana));
        Comuna nunoa = comunaRepository.save(new Comuna("Ñuñoa", metropolitana));

        // 3) Estados
        estadoRepository.save(new Estado("Programado"));
        estadoRepository.save(new Estado("Hoy"));
        Estado finalizado = estadoRepository.save(new Estado("Finalizado"));
        estadoRepository.save(new Estado("Cancelado"));

        // 4) Roles
        Rol usuario = rolRepository.save(new Rol("USUARIO"));
        Rol admin = rolRepository.save(new Rol("ADMIN"));

        // 5) Imágenes (flyers en ImgBB, de evento1 a evento6)
        Imagen img1 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/vCGX0hCd/evento1.webp", "Flyer Azotea Los Dominicos"));
        Imagen img2 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/hx7xFJh8/evento2.webp", "Flyer Save the Date Secret Spot Providencia"));
        Imagen img3 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/KPBzSQ4/evento3.webp", "Flyer Sequoia Sala Mistral"));
        Imagen img4 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/5WYyL1TN/evento4.webp", "Flyer Save the Date Spot Escuela Militar"));
        Imagen img5 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/JW9mXd9z/evento5.webp", "Flyer Secret Spot Ñuñoa"));
        Imagen img6 = imagenRepository.save(new Imagen(
            "https://i.ibb.co/HpKnq6DX/evento6.webp", "Flyer Rock x Palestina"));

        // 6) Locales. Los Secret Spot son secretos: no llevan dirección ni link de mapa.
        Local azotea = localRepository.save(new Local("Azotea Los Dominicos", null, null, lasCondes));
        Local secretProvidencia = localRepository.save(new Local("Secret Spot Providencia", null, null, providencia));
        Local salaMistral = localRepository.save(new Local("Sala Mistral", null, null, santiago));
        Local escuelaMilitar = localRepository.save(new Local("Spot Escuela Militar", null, null, lasCondes));
        Local secretNunoa = localRepository.save(new Local("Secret Spot Ñuñoa", null, null, nunoa));
        Local casaFech = localRepository.save(new Local("Casa FECH", null, null, santiago));

        // 7) Eventos: fecha, hora de inicio, hora de fin, local, estado, imagen, linkEntradas, genero
        Evento evento1 = eventoRepository.save(new Evento(
            LocalDate.of(2025, 6, 21), LocalTime.of(18, 0), LocalTime.of(2, 0),
            azotea, finalizado, img1, null, null));
        Evento evento2 = eventoRepository.save(new Evento(
            LocalDate.of(2025, 9, 6), LocalTime.of(18, 0), LocalTime.of(4, 30),
            secretProvidencia, finalizado, img2, null, null));
        Evento evento3 = eventoRepository.save(new Evento(
            LocalDate.of(2025, 11, 6), LocalTime.of(22, 0), LocalTime.of(4, 0),
            salaMistral, finalizado, img3, null, null));
        Evento evento4 = eventoRepository.save(new Evento(
            LocalDate.of(2026, 5, 8), LocalTime.of(23, 0), LocalTime.of(5, 0),
            escuelaMilitar, finalizado, img4, null, null));
        Evento evento5 = eventoRepository.save(new Evento(
            LocalDate.of(2026, 8, 7), LocalTime.of(22, 0), LocalTime.of(3, 0),
            secretNunoa, finalizado, img5, null, null));
        Evento evento6 = eventoRepository.save(new Evento(
            LocalDate.of(2026, 9, 12), LocalTime.of(15, 0), LocalTime.of(21, 0),
            casaFech, finalizado, img6, null, null));

        // 8) Djs (ids 1 a 20 en este orden)
        Dj ruilove = djRepository.save(new Dj("Ruilove"));
        Dj sonDeBeise = djRepository.save(new Dj("Son de Beise"));
        Dj derekSolari = djRepository.save(new Dj("Derek Solari"));
        Dj ghost = djRepository.save(new Dj("Ghost"));
        Dj lian = djRepository.save(new Dj("Lian"));
        Dj florencia = djRepository.save(new Dj("Florencia!"));
        Dj noel = djRepository.save(new Dj("Noel"));
        Dj chosen = djRepository.save(new Dj("Chosen"));
        Dj papitu = djRepository.save(new Dj("Papitu"));
        Dj gulian = djRepository.save(new Dj("Gulian"));
        Dj nayib = djRepository.save(new Dj("Nayib"));
        Dj threelights = djRepository.save(new Dj("Threelights"));
        Dj benjaZamorano = djRepository.save(new Dj("Benja Zamorano"));
        Dj benjaAravena = djRepository.save(new Dj("Benja Aravena"));
        Dj polly = djRepository.save(new Dj("Polly"));
        Dj coteBlanlot = djRepository.save(new Dj("Cote Blanlot"));
        Dj njv = djRepository.save(new Dj("3NJV"));
        Dj benjaWaves = djRepository.save(new Dj("Benja Waves"));
        Dj kelly = djRepository.save(new Dj("Kelly"));
        Dj alejandroVivanco = djRepository.save(new Dj("Alejandro Vivanco"));

        // 9) Line-ups: evento, dj, orden (el orden de las etiquetas en la página)
        eventoDjRepository.saveAll(List.of(
            // Evento 1: Azotea Los Dominicos
            new EventoDj(evento1, ruilove, 1),
            new EventoDj(evento1, sonDeBeise, 2),
            new EventoDj(evento1, derekSolari, 3),
            new EventoDj(evento1, ghost, 4),
            new EventoDj(evento1, lian, 5),
            new EventoDj(evento1, florencia, 6),
            new EventoDj(evento1, noel, 7),
            new EventoDj(evento1, chosen, 8),
            new EventoDj(evento1, papitu, 9),
            new EventoDj(evento1, gulian, 10),
            new EventoDj(evento1, nayib, 11),

            // Evento 2: Secret Spot Providencia
            new EventoDj(evento2, threelights, 1),
            new EventoDj(evento2, benjaZamorano, 2),
            new EventoDj(evento2, derekSolari, 3),
            new EventoDj(evento2, ghost, 4),
            new EventoDj(evento2, ruilove, 5),
            new EventoDj(evento2, sonDeBeise, 6),
            new EventoDj(evento2, papitu, 7),
            new EventoDj(evento2, benjaAravena, 8),
            new EventoDj(evento2, polly, 9),
            new EventoDj(evento2, coteBlanlot, 10),
            new EventoDj(evento2, nayib, 11),
            new EventoDj(evento2, njv, 12),

            // Evento 3: Sala Mistral
            new EventoDj(evento3, njv, 1),
            new EventoDj(evento3, derekSolari, 2),
            new EventoDj(evento3, ghost, 3),
            new EventoDj(evento3, sonDeBeise, 4),

            // Evento 4: Spot Escuela Militar
            new EventoDj(evento4, benjaWaves, 1),
            new EventoDj(evento4, chosen, 2),
            new EventoDj(evento4, kelly, 3),
            new EventoDj(evento4, alejandroVivanco, 4),
            new EventoDj(evento4, sonDeBeise, 5),

            // Evento 5: Secret Spot Ñuñoa
            new EventoDj(evento5, derekSolari, 1),
            new EventoDj(evento5, sonDeBeise, 2),
            new EventoDj(evento5, njv, 3),

            // Evento 6: Casa FECH
            new EventoDj(evento6, njv, 1),
            new EventoDj(evento6, derekSolari, 2),
            new EventoDj(evento6, ghost, 3)
        ));

        // 10) Usuarios de prueba: solo se crean si su contraseña está definida
        //     en una variable de entorno (ADMIN_PASSWORD y USUARIO_PASSWORD).
        if (!adminPassword.isBlank()) {
            usuarioRepository.save(new Usuario("admin", "admin@nevula.cl",
                passwordEncoder.encode(adminPassword), admin));
        }
        if (!usuarioPassword.isBlank()) {
            usuarioRepository.save(new Usuario("usuario", "usuario@nevula.cl",
                passwordEncoder.encode(usuarioPassword), usuario));
        }
    }
}
