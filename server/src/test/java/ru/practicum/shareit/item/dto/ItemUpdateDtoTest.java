package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemUpdateDtoTest {

    @Autowired
    private JacksonTester<ItemUpdateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto("Drill", "Drill", true, 1L, 1L);

        JsonContent<ItemUpdateDto> dtoInputSaved = jacksonTester.write(itemUpdateDto);

        assertThat(dtoInputSaved).hasJsonPath("$.name");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.available");
        assertThat(dtoInputSaved).hasJsonPath("$.owner");
        assertThat(dtoInputSaved).hasJsonPath("$.request");

    }
}