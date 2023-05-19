package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO para receber dados de atualização de senha do usuário no corpo da requisição.
 * @param oldPassword do User
 * @param newPassword do User
 * @param confirmPassword do User
 */
@Builder
public record UpdatePasswordRequest(
    @NotBlank(message = "oldPassword must not be blank")
    String oldPassword,
    @NotBlank(message = "newPassword must not be blank")
    @Size(min = 8, max = 30, message = "newPassword must contain between {min} and {max} characters")
    String newPassword,
    @NotBlank(message = "confirmPassword must not be blank")
    String confirmPassword
) {
}
