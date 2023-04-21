package br.com.emendes.adopetapi.config.security.filter;

import br.com.emendes.adopetapi.config.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Profile(value = {"dev"})
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    //Extrair authorization header
    String authHeader = request.getHeader("Authorization");

    //Verificar se o token foi enviado
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    //Extrair token
    String token = authHeader.substring(7);
    String username = jwtService.extractUsername(token);

    //Validar token
    if (!jwtService.isTokenExpired(token)) {
      try {
        //Buscar user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Inserir user no contexto
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } catch (UsernameNotFoundException exception) {
        log.info("User {} not found", username);
      }
    }

    // Seguir fluxo de filters
    filterChain.doFilter(request, response);
  }

}
