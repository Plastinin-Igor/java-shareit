package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestUpdateDtoTest {

    @Autowired
    private JacksonTester<ItemRequestUpdateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemRequestUpdateDto itemRequestUpdateDto = new ItemRequestUpdateDto(1L, "I urgently need a tool",
                1L, LocalDateTime.now());

        JsonContent<ItemRequestUpdateDto> dtoInputSaved = jacksonTester.write(itemRequestUpdateDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.requestor");
        assertThat(dtoInputSaved).hasJsonPath("$.created");
    }

}