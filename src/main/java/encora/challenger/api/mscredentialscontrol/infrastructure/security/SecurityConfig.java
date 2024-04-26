package encora.challenger.api.mscredentialscontrol.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf().disable()  // Desactiva CSRF si no es necesario
        .authorizeExchange()
        .pathMatchers("/auth/*").permitAll()  // Permitir el acceso sin autenticación
        .anyExchange().authenticated()  // Requerir autenticación para otros endpoints
        .and()
        .build();
  }
}
