package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    public void testFullMapping() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Electric Drill");
        itemDto.setDescription("Powerful electric tool");
        itemDto.setAvailable(true);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        itemDto.setOwner(userDto);

        Item item = ItemMapper.toItem(itemDto);

        assertThat(item.getId()).isEqualTo(itemDto.getId());
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner().getId()).isEqualTo(userDto.getId());
    }

    @Test
    public void testNoOwner() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Saw");
        itemDto.setDescription("Manual saw");
        itemDto.setAvailable(false);
        itemDto.setOwner(null); // владелец отсутствует

        Item item = ItemMapper.toItem(itemDto);

        assertThat(item.getOwner()).isNull();
    }

    @Test
    public void testMinimalData() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Ruler");
        itemDto.setDescription("Basic ruler");
        itemDto.setAvailable(true);
        itemDto.setOwner(null); // Без владельца

        Item item = ItemMapper.toItem(itemDto);

        assertThat(item.getId()).isEqualTo(itemDto.getId());
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isNull();
    }

    @Test
    public void testNormalConversion() {

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("New Hammer");
        itemCreateDto.setDescription("A brand-new hammer");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(1L); // Идентификатор запроса присутствует

        Item item = ItemMapper.toItemFromCreateDto(itemCreateDto);

        assertThat(item.getName()).isEqualTo("New Hammer");
        assertThat(item.getDescription()).isEqualTo("A brand-new hammer");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getRequest().getId()).isEqualTo(1L); // Проверка наличия запроса
    }

    // Тест на отсутствие связи с запросом
    @Test
    public void testWithoutRequest() {

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Old Screwdriver");
        itemCreateDto.setDescription("An old screwdriver");
        itemCreateDto.setAvailable(false);
        itemCreateDto.setRequestId(null); // Нет запроса

        Item item = ItemMapper.toItemFromCreateDto(itemCreateDto);

        assertThat(item.getName()).isEqualTo("Old Screwdriver");
        assertThat(item.getDescription()).isEqualTo("An old screwdriver");
        assertThat(item.getAvailable()).isFalse();
        assertThat(item.getRequest()).isNull(); // Проверка отсутствия запроса
    }

    // Тест на частичную информацию
    @Test
    public void testPartialInformation() {

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Flashlight");
        itemCreateDto.setDescription(null); // Описание отсутствует
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(null); // Нет запроса

        Item item = ItemMapper.toItemFromCreateDto(itemCreateDto);

        assertThat(item.getName()).isEqualTo("Flashlight");
        assertThat(item.getDescription()).isNull(); // Ожидалось отсутствие описания
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getRequest()).isNull(); // Проверка отсутствия запроса
    }

}