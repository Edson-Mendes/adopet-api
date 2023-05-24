package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.model.entity.Adoption;

/**
 * Interface component que contém as abstrações de mapeamento de DTOs para a entidade Adoption e vice-versa.
 */
public interface AdoptionMapper {

  /**
   * Mapeia o DTO adoptionRequest para a entidade Adoption.
   * @param adoptionRequest que deve ser mapeado para Adoption
   * @return Adoption com dados vindo de adoptionRequest.
   */
  Adoption adoptionRequestToAdoption(AdoptionRequest adoptionRequest);

  /**
   * Mapeia uma entidade Adoption para o DTO AdoptionResponse.
   * @param adoption que deve ser mapeado para AdoptionResponse
   * @return AdoptionResponse com as informações necessárias de Adoption.
   */
  AdoptionResponse adoptionToAdoptionResponse(Adoption adoption);

}
