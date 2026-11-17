package cl.duoc.nevulaback.config;

import cl.duoc.nevulaback.tablas.model.Comuna;
import cl.duoc.nevulaback.tablas.model.Estado;
import cl.duoc.nevulaback.tablas.model.Evento;
import cl.duoc.nevulaback.tablas.model.Imagen;
import cl.duoc.nevulaback.tablas.model.Local;
import cl.duoc.nevulaback.tablas.model.Region;
import cl.duoc.nevulaback.tablas.repository.ComunaRepository;
import cl.duoc.nevulaback.tablas.repository.EstadoRepository;
import cl.duoc.nevulaback.tablas.repository.EventoRepository;
import cl.duoc.nevulaback.tablas.repository.ImagenRepository;
import cl.duoc.nevulaback.tablas.repository.LocalRepository;
import cl.duoc.nevulaback.tablas.repository.RegionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

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

        // 4) Imágenes (flyers en ImgBB, de evento1 a evento6)
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
            "https://i.ibb.co/9m11bs4R/evento6.webp", "Flyer Rock x Palestina"));

        // 5) Locales. Los Secret Spot son secretos: no llevan dirección ni link de mapa.
        Local azotea = localRepository.save(new Local("Azotea Los Dominicos", null, null, lasCondes));
        Local secretProvidencia = localRepository.save(new Local("Secret Spot Providencia", null, null, providencia));
        Local salaMistral = localRepository.save(new Local("Sala Mistral", null, null, santiago));
        Local escuelaMilitar = localRepository.save(new Local("Spot Escuela Militar", null, null, lasCondes));
        Local secretNunoa = localRepository.save(new Local("Secret Spot Ñuñoa", null, null, nunoa));
        Local casaFech = localRepository.save(new Local("Casa FECH", null, null, santiago));

        // 6) Eventos: fecha, hora de inicio, hora de fin, local, estado, imagen
        eventoRepository.saveAll(List.of(
            new Evento(LocalDate.of(2025, 6, 21), LocalTime.of(18, 0), LocalTime.of(2, 0),
                azotea, finalizado, img1, null, null),
            new Evento(LocalDate.of(2025, 9, 6), LocalTime.of(18, 0), LocalTime.of(4, 30),
                secretProvidencia, finalizado, img2, null, null),
            new Evento(LocalDate.of(2025, 11, 6), LocalTime.of(22, 0), LocalTime.of(4, 0),
                salaMistral, finalizado, img3, null, null),
            new Evento(LocalDate.of(2026, 5, 8), LocalTime.of(23, 0), LocalTime.of(5, 0),
                escuelaMilitar, finalizado, img4, null, null),
            new Evento(LocalDate.of(2026, 8, 7), LocalTime.of(22, 0), LocalTime.of(3, 0),
                secretNunoa, finalizado, img5, null, null),
            new Evento(LocalDate.of(2026, 9, 12), LocalTime.of(15, 0), LocalTime.of(21, 0),
                casaFech, finalizado, img6, null, null)
        ));
    }
}