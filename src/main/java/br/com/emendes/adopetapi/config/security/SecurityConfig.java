package br.com.emendes.adopetapi.config.security;

import br.com.emendes.adopetapi.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;

  private static final String[] POST_WHITELISTING = {"/api/auth", "/api/shelters", "/api/guardians"};
  private static final String GUARDIAN = "GUARDIAN";
  private static final String SHELTER = "SHELTER";

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests().requestMatchers(HttpMethod.POST, POST_WHITELISTING).permitAll()
        .requestMatchers(HttpMethod.POST, "/api/adoptions").hasRole(GUARDIAN)

        .requestMatchers(HttpMethod.DELETE, "/api/guardians/*").hasRole(GUARDIAN)
        .requestMatchers(HttpMethod.PUT, "/api/guardians/*").hasRole(GUARDIAN)

        .requestMatchers(HttpMethod.POST, "/api/pets").hasRole(SHELTER)
        .requestMatchers(HttpMethod.PUT, "api/pets/*").hasRole(SHELTER)
        .requestMatchers(HttpMethod.DELETE, "api/pets/*").hasRole(SHELTER)

        .requestMatchers(HttpMethod.DELETE, "/api/shelters/*").hasRole(SHELTER)
        .requestMatchers(HttpMethod.PUT, "/api/shelters/*").hasRole(SHELTER)

        .requestMatchers(HttpMethod.PUT, "/api/adoptions/*/status").hasRole(SHELTER)
        .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    return http.build();
  }

}
