package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

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
        itemDto = new ItemDto(1L, "Перфоратор", "Ударный перфоратор", true, userDto,
                1L, new BookingShortDto(), new BookingShortDto(), List.of());
        itemCreateDto = new ItemCreateDto("Перфоратор", "Ударный перфоратор",
                true, 1L, 1L);
        itemUpdateDto = new ItemUpdateDto("Перфоратор", "Ударный перфоратор",
                true, 1L, 1L);
        commentDto = new CommentDto(1L, 1L, "Отличный перфоратор! Рекомендую.", "Ivanov Ivan",
                LocalDateTime.now());
        commentCreateDto = new CommentCreateDto("Отличный перфоратор! Рекомендую.", itemDto, userDto);
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(any(), any())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService, times(1)).addItem(any(), any());

    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(any(), any(), any())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{Id}", 1)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).updateItem(any(), any(), any());
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/{Id}", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(any(), any());
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(any(), any())).thenReturn(itemDto);

        mockMvc.perform(get("/items/{Id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).getItemById(any(), any());
    }

    @Test
    void getItems() throws Exception {
        when(itemService.getItems(any())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).getItems(any());
    }

    @Test
    void findItems() throws Exception {
        when(itemService.findItems(any(), any())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "ударный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())));

        verify(itemService, times(1)).findItems(any(), any());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(any(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{id}/comment", 1)
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));

        verify(itemService, times(1)).addComment(any(), any(), any());
    }

}