package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemDto itemDto = new ItemDto(1L, "Drill", "Drill", true, new UserDto(), 1L,
                new BookingShortDto(), new BookingShortDto(), List.of());

        JsonContent<ItemDto> dtoInputSaved = jacksonTester.write(itemDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.name");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.available");
        assertThat(dtoInputSaved).hasJsonPath("$.owner");
        assertThat(dtoInputSaved).hasJsonPath("$.request");
        assertThat(dtoInputSaved).hasJsonPath("$.lastBooking");
        assertThat(dtoInputSaved).hasJsonPath("$.nextBooking");
        assertThat(dtoInputSaved).hasJsonPath("$.comments");

    }

    @Test
    void testDeserialize() throws Exception {
        Long id = 1L;
        String name = "Drill";
        String description = "A power tool for drilling holes.";
        boolean available = true;

        String json = String.format("{" +
                "  \"id\": %d,\n" +
                "  \"name\": \"%s\",\n" +
                "  \"description\": \"%s\",\n" +
                "  \"available\": %b,\n" +
                "  \"owner\": {},\n" +
                "  \"request\": null,\n" +
                "  \"lastBooking\": {},\n" +
                "  \"nextBooking\": {},\n" +
                "  \"comments\": []\n" +
                "}", id, name, description, available);

        ItemDto deserializedItemDto = jacksonTester.parseObject(json);

        assertThat(deserializedItemDto.getId()).isEqualTo(id);
        assertThat(deserializedItemDto.getName()).isEqualTo(name);
        assertThat(deserializedItemDto.getDescription()).isEqualTo(description);
        assertThat(deserializedItemDto.getAvailable()).isEqualTo(available);
        assertThat(deserializedItemDto.getOwner()).isNotNull();
        assertThat(deserializedItemDto.getRequest()).isNull();
        assertThat(deserializedItemDto.getLastBooking()).isNotNull();
        assertThat(deserializedItemDto.getNextBooking()).isNotNull();
        assertThat(deserializedItemDto.getComments()).isEmpty();
    }

}