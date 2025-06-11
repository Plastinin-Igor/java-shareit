package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "I urgently need a tool",
                new UserDto(), LocalDateTime.now(), List.of());

        JsonContent<ItemRequestDto> dtoInputSaved = jacksonTester.write(itemRequestDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.requestor");
        assertThat(dtoInputSaved).hasJsonPath("$.created");
        assertThat(dtoInputSaved).hasJsonPath("$.items");
    }

    @Test
    void testDeserialize() throws Exception {
        Long id = 1L;
        String description = "I urgently need a tool";
        UserDto requestor = new UserDto();
        LocalDateTime created = LocalDateTime.now();
        List<Object> items = Collections.emptyList();

        String json = String.format("{" +
                "  \"id\": %d,\n" +
                "  \"description\": \"%s\",\n" +
                "  \"requestor\": {},\n" +
                "  \"created\": \"%s\",\n" +
                "  \"items\": []\n" +
                "}", id, description, created.toString());

        ItemRequestDto deserializedItemRequestDto = jacksonTester.parseObject(json);

        assertThat(deserializedItemRequestDto.getId()).isEqualTo(id);
        assertThat(deserializedItemRequestDto.getDescription()).isEqualTo(description);
        assertThat(deserializedItemRequestDto.getRequestor()).isNotNull();
        assertThat(deserializedItemRequestDto.getCreated()).isEqualTo(created);
        assertThat(deserializedItemRequestDto.getItems()).isEmpty();
    }

}