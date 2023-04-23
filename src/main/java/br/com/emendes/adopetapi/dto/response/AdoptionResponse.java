package br.com.emendes.adopetapi.dto.response;

import br.com.emendes.adopetapi.model.AdoptionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdoptionResponse(Long id, Long petId, Long guardianId, AdoptionStatus status, LocalDateTime date) {

}
