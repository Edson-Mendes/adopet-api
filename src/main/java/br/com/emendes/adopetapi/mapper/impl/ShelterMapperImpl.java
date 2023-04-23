package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ShelterMapperImpl implements ShelterMapper {

  @Override
  public Shelter createShelterRequestToShelter(CreateShelterRequest createShelterRequest) {
    User user = User.builder()
        .email(createShelterRequest.email())
        .password(createShelterRequest.password())
        .build();

    return Shelter.builder()
        .name(createShelterRequest.name())
        .user(user)
        .build();
  }

  @Override
  public ShelterResponse shelterToShelterResponse(Shelter shelter) {
    return ShelterResponse.builder()
        .id(shelter.getId())
        .name(shelter.getName())
        .email(shelter.getUser().getEmail())
        .build();
  }

}
