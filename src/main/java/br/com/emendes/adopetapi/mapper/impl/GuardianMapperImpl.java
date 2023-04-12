package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.mapper.GuardianMapper;
import br.com.emendes.adopetapi.model.entity.Guardian;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GuardianMapperImpl implements GuardianMapper {

  private final ModelMapper modelMapper;

  @Override
  public Guardian guardianRequestToGuardian(CreateGuardianRequest createGuardianRequest) {
    return modelMapper.map(createGuardianRequest, Guardian.class);
  }

  @Override
  public GuardianResponse guardianToGuardianResponse(Guardian guardian) {
    return modelMapper.map(guardian, GuardianResponse.class);
  }

}
