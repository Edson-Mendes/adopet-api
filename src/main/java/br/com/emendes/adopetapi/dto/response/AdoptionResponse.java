package br.com.emendes.adopetapi.dto.response;

import br.com.emendes.adopetapi.model.AdoptionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO para enviar informações da Adoption para o cliente no corpo da resposta.
 * @param id da Adoption
 * @param pet Pet relacionado com a Adoption
 * @param guardianId Guardian que solicitou a Adoption.
 * @param status da Adoption
 * @param date da Adoption
 */
@Builder
public record AdoptionResponse(
    Long id,
    PetResponse pet,
    Long guardianId,
    AdoptionStatus status,
    LocalDateTime date) {

}
