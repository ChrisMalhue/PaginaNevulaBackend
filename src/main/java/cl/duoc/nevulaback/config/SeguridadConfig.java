package cl.duoc.nevulaback.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    private final JwtFiltro jwtFiltro;
    private final String[] allowedOrigins;

    public SeguridadConfig(
            JwtFiltro jwtFiltro,
            @Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
        this.jwtFiltro = jwtFiltro;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) ->
                    escribirErrorJson(res, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized",
                        "Debes iniciar sesión con un token válido", req.getRequestURI()))
                .accessDeniedHandler((req, res, deniedEx) ->
                    escribirErrorJson(res, HttpServletResponse.SC_FORBIDDEN, "Forbidden",
                        "No tienes permisos para acceder a este recurso", req.getRequestURI()))
            )
            .authorizeHttpRequests(auth -> auth
                // 1) Endpoints públicos: login, salud y documentación Swagger
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/health", "/api/salud/**", "/salud/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                // 2) Catálogo público (lectura libre para visitantes)
                .requestMatchers(HttpMethod.GET,
                    "/api/eventos/**",
                    "/api/djs/**",
                    "/api/evento-djs/**",
                    "/api/locales/**",
                    "/api/comunas/**",
                    "/api/regiones/**",
                    "/api/estados/**",
                    "/api/imagenes/**"
                ).permitAll()

                // 3) Recursos de usuarios autenticados (USUARIO o ADMIN)
                .requestMatchers(
                    "/api/cotizaciones/**",
                    "/api/recordatorios/**",
                    "/api/recordatorios-evento/**"
                ).hasAnyRole("USUARIO", "ADMIN")

                // 4) Administración (solo ADMIN)
                .requestMatchers("/api/roles/**", "/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers(
                    "/api/eventos/**",
                    "/api/djs/**",
                    "/api/evento-djs/**",
                    "/api/locales/**",
                    "/api/comunas/**",
                    "/api/regiones/**",
                    "/api/estados/**",
                    "/api/imagenes/**"
                ).hasRole("ADMIN")

                // 5) Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    private void escribirErrorJson(
            HttpServletResponse response, int status, String error, String mensaje, String ruta)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = String.format(
            "{\"status\":%d,\"error\":\"%s\",\"mensaje\":\"%s\",\"ruta\":\"%s\"}",
            status, error, mensaje, ruta
        );
        response.getWriter().write(json);
    }
}