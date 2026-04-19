package school.sptech.KentoCafe.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//por padrão as portas das rotas são bloquados apartir do momento que vc implementa as portas
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticatorFilter userAuthenticationFilter;

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/favicon.ico",
            "/h2-console/**",
            "/users/login", // Url que usaremos para fazer login
            "/users", // Url que usaremos para criar um usuário
            "/users/none",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/public/**",
            "/api/public/authenticate",
            "/webjars/**",
            "/v3/api-docs/**",
            "/actuator/*",
            "/usuarios/login/**",
            "/usuarios/logout/**",
            "/h2-console/**",
            "/h2-console/*/**",
            "/auth/**",
            "/cafeteria/**",
            "/ingrediente/**",
            "/produto/**",
            "/funcionario/cadastro",
            "/funcionario/crud/**",
            "/funcionario/crud",
            "/funcionario/none",
            //habilita todas as rotas (xpto)
            //"/**"
    };

    // Endpoints que requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/funcionario/autenticado"
    };


    // Endpoints que só podem ser acessador por usuários com permissão de administrador
    public static final String [] ENDPOINTS_GERENTE = {
            "/funcionario/gerenteautenticado"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //habilita todos os cors
                .cors(Customizer.withDefaults())
                //h2
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // H2 console
                )
                //deixa a api stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //middleware resposável por montar o corpo do header. Identifica se o token existe
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //middleware

                //especifica quais rotas estão bloqueadas e quais não estão
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_GERENTE).access((authentication, context) -> {
                            UserDetailsImpl user = (UserDetailsImpl) authentication.get().getPrincipal();
                            return new AuthorizationDecision(user.isManager());
                        })
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .anyRequest().permitAll()
                )
                //tratativa de erro
                .exceptionHandling(ex -> ex
                        // 401 → não autenticado
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Sem autenticação\"}");
                        })

                        // 403 → sem permissão
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Acesso negado\"}");
                        })
                );
                //libera as rotas a fim de desenvolvimento (xpto)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
