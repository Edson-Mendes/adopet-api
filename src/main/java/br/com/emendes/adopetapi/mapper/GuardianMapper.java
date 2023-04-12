package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.model.entity.Guardian;

public interface GuardianMapper {

  Guardian guardianRequestToGuardian(CreateGuardianRequest createGuardianRequest);

  GuardianResponse guardianToGuardianResponse(Guardian guardian);

}
