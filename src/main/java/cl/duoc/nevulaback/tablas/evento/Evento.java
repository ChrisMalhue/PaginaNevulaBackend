package cl.duoc.nevulaback.tablas.evento;

import cl.duoc.nevulaback.tablas.estado.Estado;
import cl.duoc.nevulaback.tablas.imagen.Imagen;
import cl.duoc.nevulaback.tablas.local.Local;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonPropertyOrder({"id", "fecha", "horaInicio", "horaFin", "local", "estado", "imagen"})
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(nullable = false)
    private LocalTime horaFin;

    @NotNull(message = "El local es obligatorio")
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @NotNull(message = "El estado es obligatorio")
    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;
    
    @NotNull(message = "La imagen es obligatoria")
    @ManyToOne
    @JoinColumn(name = "imagen_id", nullable = false)
    private Imagen imagen;

    public Evento() {
    }

    public Evento(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                    Local local, Estado estado, Imagen imagen) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.local = local;
        this.estado = estado;
        this.imagen = imagen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Imagen getImagen() { return imagen; }
    public void setImagen(Imagen imagen) { this.imagen = imagen; }
}