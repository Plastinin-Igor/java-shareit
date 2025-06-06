package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Username must be specified.")
    private String name;

    @NotBlank(message = "The field must contain an email address.")
    @Email(message = "The field must contain an email address.")
    private String email;
}
