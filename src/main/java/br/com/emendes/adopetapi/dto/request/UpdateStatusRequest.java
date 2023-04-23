package br.com.emendes.adopetapi.dto.request;

import br.com.emendes.adopetapi.validation.annotation.ValidStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateStatusRequest(
    @NotBlank(message = "status must not be blank")
    @ValidStatus
    String status) {

}
