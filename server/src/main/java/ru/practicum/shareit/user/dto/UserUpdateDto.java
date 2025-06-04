package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;
    private String name;
    private String email;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }
}
