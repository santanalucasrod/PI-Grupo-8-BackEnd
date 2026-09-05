package school.sptech.KentoCafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import school.sptech.KentoCafe.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;


    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categorias/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/produtos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/produtos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/ingredientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/ingredientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/ingredientes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,   "/personalizacoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/personalizacoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/personalizacoes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/pedidos/**").hasRole("ADMIN")
                        .requestMatchers("/pedidos/{id}/cancelar").hasRole("ADMIN")
                        .requestMatchers("/funcionarios/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/produtos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/categorias/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/ingredientes/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/personalizacoes/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.POST,   "/tamanhos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/tamanhos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tamanhos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/tamanhos/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/pedidos/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
