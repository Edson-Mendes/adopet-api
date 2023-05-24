package br.com.emendes.adopetapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

/**
 * Record DTO para enviar informações do Pet para o cliente no corpo da resposta.
 * @param id do Pet
 * @param name do Pet
 * @param description do Pet
 * @param age do Pet
 * @param adopted se o Pet está adotado ou não
 * @param images Imagens do Pet
 * @param shelterId id do Shelter que está com o Pet.
 */
@Builder
public record PetResponse(
    Long id,
    String name,
    String description,
    String age,
    boolean adopted,
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<ImageResponse> images,
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    Long shelterId) {

}
