package br.com.emendes.adopetapi.dto.response;

import lombok.Builder;

@Builder
public record PetResponse(Long id, String name, String description, String age, boolean adopted, Long shelterId) {

}
