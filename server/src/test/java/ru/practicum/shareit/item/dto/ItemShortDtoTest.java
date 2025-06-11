package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemShortDtoTest {

    @Autowired
    private JacksonTester<ItemShortDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemShortDto itemShortDto = new ItemShortDto(1L, "Drill", 1L);

        JsonContent<ItemShortDto> dtoInputSaved = jacksonTester.write(itemShortDto);

        assertThat(dtoInputSaved).hasJsonPath("$.id");
        assertThat(dtoInputSaved).hasJsonPath("$.name");
        assertThat(dtoInputSaved).hasJsonPath("$.owner");
    }

}