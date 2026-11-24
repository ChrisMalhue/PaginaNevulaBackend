package cl.duoc.nevulaback.config;

import cl.duoc.nevulaback.tablas.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey claveSecreta;
    private final long expiracionMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expiracionMs) {
        this.claveSecreta = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMs);

        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("usuarioId", usuario.getId())
                .claim("nombreUsuario", usuario.getNombreUsuario())
                .claim("rol", usuario.getRol().getNombre())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(claveSecreta)
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    public Long extraerUsuarioId(String token) {
        return extraerClaims(token).get("usuarioId", Long.class);
    }

    public boolean esValido(String token) {
        try {
            Claims claims = extraerClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}