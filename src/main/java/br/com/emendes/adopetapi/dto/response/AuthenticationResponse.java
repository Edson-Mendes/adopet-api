package br.com.emendes.adopetapi.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token, String type) {

}
