package school.sptech.KentoCafe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        System.out.println("🔍 Requisição: " + request.getMethod() + " " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("✅ Sem token — passando direto");
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("🔍 Token extraído: " + token);

        try {
            String email = jwtService.extrairEmail(token);
            System.out.println("🔍 Email extraído do token: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(email);
                System.out.println("🔍 Usuário carregado: " + user.getUsername());
                System.out.println("🔍 Token válido? " + jwtService.tokenValido(token, user));

                if (jwtService.tokenValido(token, user)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("✅ Usuário autenticado com sucesso");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro no filtro: " + e.getClass().getName() + " — " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
