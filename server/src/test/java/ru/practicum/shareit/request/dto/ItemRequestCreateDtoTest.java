package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemRequestCreateDtoTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {

        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("I urgently need a tool");

        JsonContent<ItemRequestCreateDto> dtoInputSaved = jacksonTester.write(itemRequestCreateDto);

        assertThat(dtoInputSaved).hasJsonPath("$.description");
    }
}