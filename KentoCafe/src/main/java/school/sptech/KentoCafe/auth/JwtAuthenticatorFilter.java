package school.sptech.KentoCafe.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import school.sptech.KentoCafe.entity.Funcionario;
import school.sptech.KentoCafe.repository.FuncionarioRepository;

import java.io.IOException;
import java.util.Arrays;

//@component cria o objeto para você automaticamente
@Component
public class JwtAuthenticatorFilter extends OncePerRequestFilter {
    //extends. para cada requisição rode o doFilterInternal

    /*
    Intercepta TODAS as requisições
    Verifica se tem token
    Valida o token
    Se for válido → libera acesso
    Middleware
     */

    @Autowired
    private JwtService jwtTokenService; // Service que definimos anteriormente

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Verifica se o endpoint requer autenticação antes de processar a requisição
        if (checkIfEndpointIsNotPublic(request)) {
            System.out.println("URI: " + request.getRequestURI());
            System.out.println("requisição privada");
            String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token); // Obtém o assunto (neste caso, o nome de usuário) do token
                Funcionario user = funcionarioRepository.findByEmail(subject).get(); // Busca o usuário pelo email (que é o assunto do token)
                UserDetailsImpl userDetails = new UserDetailsImpl(user); // Cria um UserDetails com o usuário encontrado

                // Cria um objeto de autenticação do Spring Security
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

                // Define o objeto de autenticação no contexto de segurança do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
//            else {
//                //exception
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//erro de 401 status
//                response.setContentType("application/json");
//                response.getWriter().write("{\"error\": \"Token ausente\"}");//escreve oq ta no json
//                return;
//            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    //isso inclui endspoints/**
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return Arrays.stream(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
                .noneMatch(pattern -> {
                    // remove /** do final se existir
                    if (pattern.endsWith("/**")) {
                        String base = pattern.replace("/**", "");
                        return uri.startsWith(base);
                    }

                    // comparação normal
                    return uri.equals(pattern);
                });
    }
}
