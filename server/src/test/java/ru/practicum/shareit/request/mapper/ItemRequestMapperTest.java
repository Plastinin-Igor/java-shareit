package ru.practicum.shareit.request.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;


class ItemRequestMapperTest {

    @Test
    void testToItemRequest_BasicScenario() {
        // Подготовка данных
        Long id = 1L;
        String description = "Sample request";
        LocalDateTime created = LocalDateTime.now();

        UserDto userDto = new UserDto(); // Предполагается наличие класса UserDto
        userDto.setName("John Doe");

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(id);
        dto.setDescription(description);
        dto.setRequestor(userDto); // Предположительно передаем валидный UserDto
        dto.setCreated(created);

        // Преобразуем
        ItemRequest result = ItemRequestMapper.toItemRequest(dto);

        // Проверка результата
        assertEquals(id, result.getId(), "ID не совпадает");
        assertEquals(description, result.getDescription(), "Описание не совпадает");
        assertNotNull(result.getRequestor(), "Пользователь не преобразован");
        assertEquals(created, result.getCreated(), "Время создания не совпадают");
    }

    @Test
    void testToItemRequest_NullFields() {
        // Данные с пустыми значениями
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(null);
        dto.setDescription(null);
        dto.setRequestor(null);
        dto.setCreated(LocalDateTime.now()); // Время оставляем непустым

        // Преобразуем
        ItemRequest result = ItemRequestMapper.toItemRequest(dto);

        // Проверка результатов
        assertNull(result.getId(), "ID должно быть пустым");
        assertNull(result.getDescription(), "Описание должно быть пустым");
        assertNull(result.getRequestor(), "Запрашивающий пользователь должен отсутствовать");
        assertNotNull(result.getCreated(), "Время создания не должно быть пустым");
    }

    @Test
    void testToItemRequest_EmptyValues() {
        // Пустой DTO
        ItemRequestDto dto = new ItemRequestDto();

        // Преобразуем
        ItemRequest result = ItemRequestMapper.toItemRequest(dto);

        // Проверка результатов
        assertNull(result.getId(), "ID должно быть пустым");
        assertNull(result.getDescription(), "Описание должно быть пустым");
        assertNull(result.getRequestor(), "Запрашивающий пользователь должен отсутствовать");
        assertNull(result.getCreated(), "Время создания должно быть пустым");
    }

}