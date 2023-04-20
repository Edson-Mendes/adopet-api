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

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN_NAME;
import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER_NAME;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;

  private static final String[] POST_WHITELISTING = {"/api/auth", "/api/shelters", "/api/guardians"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests().requestMatchers(HttpMethod.POST, POST_WHITELISTING).permitAll()
        .requestMatchers(HttpMethod.POST, "/api/adoptions").hasRole(ROLE_GUARDIAN_NAME)
        .requestMatchers(HttpMethod.DELETE, "/api/guardians").hasRole(ROLE_GUARDIAN_NAME)
        .requestMatchers(HttpMethod.PUT, "/api/guardians/*").hasRole(ROLE_GUARDIAN_NAME)
        .requestMatchers(HttpMethod.DELETE, "/api/shelters").hasRole(ROLE_SHELTER_NAME)
        .requestMatchers(HttpMethod.PUT, "/api/adoptions/*/status").hasRole(ROLE_SHELTER_NAME)
        .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    return http.build();
  }

}
