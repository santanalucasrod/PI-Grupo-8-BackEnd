package school.sptech.KentoCafe;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KentoCafe API")
                        .description("""
                    API para gerenciamento da cafeteria Kento.
                    
                    ## Fluxo de autenticação
                    1. Faça login em `/auth/login` com email e senha
                    2. Copie o token retornado
                    3. Clique em **Authorize** no topo da página
                    4. Cole o token no campo **Value** e confirme
                    
                    ## Níveis de acesso
                    - **Atendente (USER)** — cria e conclui pedidos, consulta produtos
                    - **Gerente (ADMIN)** — acesso total, incluindo cadastros e cancelamentos
                    """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("KentoCafe")
                                .email("contato@kentocafe.com.br")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token", new SecurityScheme()
                                .name("Bearer Token")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token obtido no endpoint **/auth/login**. Cole apenas o token, sem o prefixo 'Bearer'.")
                        )
                );
    }
}

