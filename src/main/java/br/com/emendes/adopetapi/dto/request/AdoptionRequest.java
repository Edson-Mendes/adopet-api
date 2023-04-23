package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AdoptionRequest(
    @NotNull(message = "petId must not be null")
    @Positive(message = "petId must be greater than zero")
    Long petId) {

}
