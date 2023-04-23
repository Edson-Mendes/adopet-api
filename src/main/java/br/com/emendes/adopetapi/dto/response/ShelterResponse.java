package br.com.emendes.adopetapi.dto.response;

import lombok.Builder;

@Builder
public record ShelterResponse(Long id, String name, String email) {

}
