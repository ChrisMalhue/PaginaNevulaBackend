package cl.duoc.nevulaback.config;

import cl.duoc.nevulaback.tablas.comuna.Comuna;
import cl.duoc.nevulaback.tablas.comuna.ComunaRepository;
import cl.duoc.nevulaback.tablas.estado.Estado;
import cl.duoc.nevulaback.tablas.estado.EstadoRepository;
import cl.duoc.nevulaback.tablas.evento.Evento;
import cl.duoc.nevulaback.tablas.evento.EventoRepository;
import cl.duoc.nevulaback.tablas.imagen.Imagen;
import cl.duoc.nevulaback.tablas.imagen.ImagenRepository;
import cl.duoc.nevulaback.tablas.local.Local;
import cl.duoc.nevulaback.tablas.local.LocalRepository;
import cl.duoc.nevulaback.tablas.region.Region;
import cl.duoc.nevulaback.tablas.region.RegionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    // Links de los flyers en ImgBB (evento1 a evento6)
    private static final String IMG_1 = "https://i.ibb.co/vCGX0hCd/evento1.webp";
    private static final String IMG_2 = "https://i.ibb.co/hx7xFJh8/evento2.webp";
    private static final String IMG_3 = "https://i.ibb.co/KPBzSQ4/evento3.webp";
    private static final String IMG_4 = "https://i.ibb.co/5WYyL1TN/evento4.webp";
    private static final String IMG_5 = "https://i.ibb.co/JW9mXd9z/evento5.webp";
    private static final String IMG_6 = "https://i.ibb.co/9m11bs4R/evento6.webp";

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final LocalRepository localRepository;
    private final EstadoRepository estadoRepository;
    private final ImagenRepository imagenRepository;
    private final EventoRepository eventoRepository;

    public DatosIniciales(
        RegionRepository regionRepository,
        ComunaRepository comunaRepository,
        LocalRepository localRepository,
        EstadoRepository estadoRepository,
        ImagenRepository imagenRepository,
        EventoRepository eventoRepository
    ) {
        this.regionRepository = regionRepository;
        this.comunaRepository = comunaRepository;
        this.localRepository = localRepository;
        this.estadoRepository = estadoRepository;
        this.imagenRepository = imagenRepository;
        this.eventoRepository = eventoRepository;
    }

    @Override
    public void run(String... args) {

        // 1) Regiones
        if (regionRepository.count() == 0) {
            regionRepository.saveAll(List.of(
                new Region("Arica y Parinacota"),
                new Region("Metropolitana")
            ));
        }

        // 2) Comunas
        if (comunaRepository.count() == 0) {
            Region arica = buscarRegion("Arica y Parinacota");
            Region metropolitana = buscarRegion("Metropolitana");

            comunaRepository.saveAll(List.of(
                new Comuna("Arica", arica),
                new Comuna("Camarones", arica),

                new Comuna("Santiago", metropolitana),
                new Comuna("Providencia", metropolitana),
                new Comuna("Las Condes", metropolitana),
                new Comuna("Maipú", metropolitana),
                new Comuna("Puente Alto", metropolitana)
            ));
        }
        // Ñuñoa va aparte, por si las comunas ya estaban cargadas de antes
        crearComunaSiNoExiste("Ñuñoa", buscarRegion("Metropolitana"));

        // 3) Estados
        if (estadoRepository.count() == 0) {
            estadoRepository.saveAll(List.of(
                new Estado("Programado"),
                new Estado("Hoy"),
                new Estado("Finalizado"),
                new Estado("Cancelado")
            ));
        }

        // 4) Imágenes (cada una se crea solo si su link no existe)
        crearImagenSiNoExiste(IMG_1, "Flyer Azotea Los Dominicos");
        crearImagenSiNoExiste(IMG_2, "Flyer Save the Date Secret Spot Providencia");
        crearImagenSiNoExiste(IMG_3, "Flyer Sequoia Sala Mistral");
        crearImagenSiNoExiste(IMG_4, "Flyer Save the Date Spot Escuela Militar");
        crearImagenSiNoExiste(IMG_5, "Flyer Secret Spot Ñuñoa");
        crearImagenSiNoExiste(IMG_6, "Flyer Rock x Palestina");

        // 5) Locales (cada uno se crea solo si no existe).
        // Los Secret Spot son secretos: no llevan dirección ni link de mapa.
        crearLocalSiNoExiste("Azotea Los Dominicos", buscarComuna("Las Condes"));
        crearLocalSiNoExiste("Secret Spot Providencia", buscarComuna("Providencia"));
        crearLocalSiNoExiste("Sala Mistral", buscarComuna("Santiago"));
        crearLocalSiNoExiste("Spot Escuela Militar", buscarComuna("Las Condes"));
        crearLocalSiNoExiste("Secret Spot Ñuñoa", buscarComuna("Ñuñoa"));
        crearLocalSiNoExiste("Casa FECH", buscarComuna("Santiago"));

        // 6) Eventos (siempre al final: dependen de locales, estados e imágenes)
        if (eventoRepository.count() == 0) {
            Estado finalizado = buscarEstado("Finalizado");

            eventoRepository.saveAll(List.of(
                // evento1: Azotea Los Dominicos
                new Evento(LocalDate.of(2025, 6, 21), LocalTime.of(18, 0), LocalTime.of(2, 0),
                    buscarLocal("Azotea Los Dominicos"), finalizado, buscarImagen(IMG_1)),
                // evento2: Secret Spot Providencia
                new Evento(LocalDate.of(2025, 9, 6), LocalTime.of(18, 0), LocalTime.of(4, 30),
                    buscarLocal("Secret Spot Providencia"), finalizado, buscarImagen(IMG_2)),
                // evento3: Sala Mistral
                new Evento(LocalDate.of(2025, 11, 6), LocalTime.of(22, 0), LocalTime.of(4, 0),
                    buscarLocal("Sala Mistral"), finalizado, buscarImagen(IMG_3)),
                // evento4: Spot Escuela Militar
                new Evento(LocalDate.of(2026, 5, 8), LocalTime.of(23, 0), LocalTime.of(5, 0),
                    buscarLocal("Spot Escuela Militar"), finalizado, buscarImagen(IMG_4)),
                // evento5: Secret Spot Ñuñoa
                new Evento(LocalDate.of(2026, 8, 7), LocalTime.of(22, 0), LocalTime.of(3, 0),
                    buscarLocal("Secret Spot Ñuñoa"), finalizado, buscarImagen(IMG_5)),
                // evento6: Casa FECH
                new Evento(LocalDate.of(2026, 9, 12), LocalTime.of(15, 0), LocalTime.of(21, 0),
                    buscarLocal("Casa FECH"), finalizado, buscarImagen(IMG_6))
            ));
        }
    }

    // ---------- crear si no existe ----------

    private void crearComunaSiNoExiste(String nombre, Region region) {
        boolean existe = comunaRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
        if (!existe) {
            comunaRepository.save(new Comuna(nombre, region));
        }
    }

    private void crearLocalSiNoExiste(String nombre, Comuna comuna) {
        boolean existe = localRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .anyMatch(l -> l.getNombre().equalsIgnoreCase(nombre));
        if (!existe) {
            localRepository.save(new Local(nombre, null, null, comuna));
        }
    }

    private void crearImagenSiNoExiste(String url, String descripcion) {
        if (imagenRepository.findByUrl(url).isEmpty()) {
            imagenRepository.save(new Imagen(url, descripcion));
        }
    }

    // ---------- buscar ----------

    private Region buscarRegion(String nombre) {
        return regionRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .filter(r -> r.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElseThrow();
    }

    private Comuna buscarComuna(String nombre) {
        return comunaRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElseThrow();
    }

    private Estado buscarEstado(String nombre) {
        return estadoRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .filter(e -> e.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElseThrow();
    }

    private Local buscarLocal(String nombre) {
        return localRepository.findByNombreContainingIgnoreCase(nombre)
            .stream()
            .filter(l -> l.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElseThrow();
    }

    private Imagen buscarImagen(String url) {
        return imagenRepository.findByUrl(url).orElseThrow();
    }
}