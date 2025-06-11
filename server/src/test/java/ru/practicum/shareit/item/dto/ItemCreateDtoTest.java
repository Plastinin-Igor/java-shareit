package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCreateDtoTest {

    @Autowired
    private JacksonTester<ItemCreateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemCreateDto itemCreateDto = new ItemCreateDto("Drill", "drill", true, 1L, 1L);

        JsonContent<ItemCreateDto> dtoInputSaved = jacksonTester.write(itemCreateDto);

        assertThat(dtoInputSaved).hasJsonPath("$.name");
        assertThat(dtoInputSaved).hasJsonPath("$.description");
        assertThat(dtoInputSaved).hasJsonPath("$.available");
        assertThat(dtoInputSaved).hasJsonPath("$.owner");
        assertThat(dtoInputSaved).hasJsonPath("$.requestId");

    }

}