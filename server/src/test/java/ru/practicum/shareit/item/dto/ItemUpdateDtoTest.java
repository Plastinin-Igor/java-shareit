package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    @Test
    void testHasName_NameProvided_ReturnsTrue() {

        ItemUpdateDto dto = new ItemUpdateDto("Table", "", null, null, null);

        boolean result = dto.hasName();

        assertTrue(result);
    }

    @Test
    void testHasName_NameNotProvided_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto(null, "", null, null, null);

        boolean result = dto.hasName();

        assertFalse(result);
    }

    @Test
    void testHasName_EmptyString_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", "", null, null, null);

        boolean result = dto.hasName();

        assertFalse(result);
    }

    @Test
    void testHasName_SpacesOnly_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto(" ", "", null, null, null);

        boolean result = dto.hasName();

        assertFalse(result);
    }

    @Test
    void testHasDescription_DescriptionProvided_ReturnsTrue() {

        ItemUpdateDto dto = new ItemUpdateDto("", "Very nice table", null, null, null);

        boolean result = dto.hasDescription();

        assertTrue(result);
    }

    @Test
    void testHasDescription_DescriptionNotProvided_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", null, null, null, null);

        boolean result = dto.hasDescription();

        assertFalse(result);
    }

    @Test
    void testHasDescription_EmptyString_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", "", null, null, null);

        boolean result = dto.hasDescription();

        assertFalse(result);
    }

    @Test
    void testHasDescription_SpacesOnly_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", " ", null, null, null);

        boolean result = dto.hasDescription();

        assertFalse(result);
    }

    @Test
    void testHasOwner_OwnerNotProvided_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", "", null, null, null);

        boolean result = dto.hasOwner();

        assertFalse(result);
    }

    @Test
    void testHasRequest_RequestNotProvided_ReturnsFalse() {

        ItemUpdateDto dto = new ItemUpdateDto("", "", null, null, null);

        boolean result = dto.hasRequest();

        assertFalse(result);
    }

}