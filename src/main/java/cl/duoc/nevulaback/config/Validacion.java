package cl.duoc.nevulaback.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


/** Validación compartida: la usan los PATCH de todos los controllers. */
@Component
public class Validacion {

    private final Validator validator;

    public Validacion(Validator validator) {
        this.validator = validator;
    }

    /** Revisa las reglas de la entidad. Si alguna falla, responde 400 con los mensajes. */
    public <T> void validar(T objeto) {
        Set<ConstraintViolation<T>> errores = validator.validate(objeto);
        if (!errores.isEmpty()) {
        String mensaje = errores.stream()
            .map(ConstraintViolation::getMessage)
            .sorted()
            .collect(Collectors.joining(". "));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
        }
    }

    // Dentro de la clase:
    private static final ZoneId ZONA_CHILE = ZoneId.of("America/Santiago");

    /** La fecha debe ser hoy o posterior (según la hora de Chile). */
    public void fechaNoPasada(LocalDate fecha) {
    if (fecha != null && fecha.isBefore(LocalDate.now(ZONA_CHILE))) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "La fecha del evento no puede ser anterior a hoy");
    }
    }
}