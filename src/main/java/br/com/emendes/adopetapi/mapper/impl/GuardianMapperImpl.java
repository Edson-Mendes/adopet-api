package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.mapper.GuardianMapper;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GuardianMapperImpl implements GuardianMapper {

  @Override
  public Guardian createGuardianRequestToGuardian(CreateGuardianRequest createGuardianRequest) {
    User user = User.builder()
        .email(createGuardianRequest.email())
        .password(createGuardianRequest.password())
        .build();

    return Guardian.builder()
        .name(createGuardianRequest.name())
        .user(user)
        .build();
  }

  @Override
  public GuardianResponse guardianToGuardianResponse(Guardian guardian) {
    return GuardianResponse.builder()
        .id(guardian.getId())
        .name(guardian.getName())
        .email(guardian.getUser().getEmail())
        .build();
  }

}
