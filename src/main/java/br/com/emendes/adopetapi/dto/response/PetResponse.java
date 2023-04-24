package br.com.emendes.adopetapi.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PetResponse(
    Long id,
    String name,
    String description,
    String age,
    boolean adopted,
    List<ImageResponse> images,
    Long shelterId) {

}
