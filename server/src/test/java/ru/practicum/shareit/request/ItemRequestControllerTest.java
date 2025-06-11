package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestServiceImpl requestService;

    @Autowired
    private MockMvc mockMvc;

    private ItemRequestDto itemRequestDto;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Igor Plastinin", "plastinin-i@ya.ru");
        itemRequestDto = new ItemRequestDto(1L, "Очень нужен перфоратор!", userDto, LocalDateTime.now(),
                List.of());
    }

    @Test
    void addItemRequest() throws Exception {
        when(requestService.addItemRequest(any(), any())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

        verify(requestService, times(1)).addItemRequest(any(), any());
    }

    @Test
    void getListOfYourRequests() throws Exception {
        when(requestService.getListOfYourRequests(any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));

        verify(requestService, times(1)).getListOfYourRequests(any());
    }

    @Test
    void getListQueriesCreatedByOtherUsers() throws Exception {
        when(requestService.getListQueriesCreatedByOtherUsers(any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())));

        verify(requestService, times(1)).getListQueriesCreatedByOtherUsers(any());
    }

    @Test
    void getItemRequestById() throws Exception {
        when(requestService.getItemRequestById(any())).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/{requestId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

        verify(requestService, times(1)).getItemRequestById(any());
    }

}