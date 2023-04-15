package br.com.emendes.adopetapi.config.security.service.impl;

import br.com.emendes.adopetapi.config.security.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${adopetapi.jwt.expiration:86400000}")
  private String expiration;

  @Value("${adopetapi.jwt.secret:12341234123412341234123412341234}")
  private String secret;

  @Override
  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setIssuer("Adopet API")
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getKey() {
    byte[] secretBytes = secret.getBytes();
    return Keys.hmacShaKeyFor(secretBytes);
  }

}
