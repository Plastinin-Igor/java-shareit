package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private UserDto userDto;
    private CommentDto commentDto;
    private CommentCreateDto commentCreateDto;


    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Igor Plastinin", "plastinin-i@ya.ru");
        itemDto = new ItemDto(1L, "Drill", "Impact drill", true, userDto,
                1L, new BookingShortDto(), new BookingShortDto(), List.of());
        itemCreateDto = new ItemCreateDto("Drill", "Impact drill",
                true, 1L, 1L);
        itemUpdateDto = new ItemUpdateDto("Drill", "Impact drill",
                true, 1L, 1L);
        commentDto = new CommentDto(1L, 1L, "Excellent Impact drill! I recommend it.",
                "Ivanov Ivan", LocalDateTime.now());
        commentCreateDto = new CommentCreateDto("Excellent Impact drill! I recommend it.", itemDto, userDto);
    }

    @Test
    void addItem() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.addItem(any(), any())).thenReturn(response);

        String content = mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(itemJson, content);

        verify(itemClient).addItem(any(), any());
    }

    @Test
    void updateItem() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.updateItem(any(), any(), any())).thenReturn(response);

        String content = mockMvc.perform(patch("/items/{Id}", 1)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(itemJson, content);

        verify(itemClient).updateItem(any(), any(), any());
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/{Id}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).deleteItem(any(), any());
    }

    @Test
    void getItemById() throws Exception {
        mockMvc.perform(get("/items/{Id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).getItemById(any(), any());
    }

    @Test
    void getItems() throws Exception {
        mockMvc.perform(get("/items", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).getItems(any());

    }

    @Test
    void findItems() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "drill"))
                .andExpect(status().isOk());

        verify(itemClient).findItems(any(), any());
    }

    @Test
    void addComment() throws Exception {
        String commentJson = objectMapper.writeValueAsString(commentDto);
        ResponseEntity<Object> response = new ResponseEntity<>(commentJson, HttpStatus.OK);

        when(itemClient.addComment(any(), any(), any())).thenReturn(response);

        String content = mockMvc.perform(post("/items/{id}/comment", 1)
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(commentJson, content);

        verify(itemClient).addComment(any(), any(), any());
    }
}