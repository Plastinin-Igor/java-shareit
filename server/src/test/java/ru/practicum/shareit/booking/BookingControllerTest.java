package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private BookingCreateDto bookingCreateDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private CommentDto commentDto;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        userDto = new UserDto(1L, "Igor Plastinin", "plastinin-i@ya.ru");
        itemDto = new ItemDto(1L, "Перфоратор", "Ударный перфоратор", true, userDto,
                1L, new BookingShortDto(), new BookingShortDto(), List.of());

        bookingCreateDto = new BookingCreateDto(start, end, 1L, 1L, BookingStatus.WAITING);
        bookingDto = new BookingDto(1L, start, end, itemDto, userDto, BookingStatus.WAITING);
    }

    @Test
    void add() throws Exception {
        when(bookingService.addBooking(any(), any())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));

        verify(bookingService, times(1)).addBooking(any(), any());
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(any(), any(), any())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));

        verify(bookingService, times(1)).approveBooking(any(), any(), any());
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(any(), any())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));

        verify(bookingService, times(1)).getBookingById(any(), any());
    }

    @Test
    void getBookingByUser() throws Exception {
        when(bookingService.getBookingByUser(any(), any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.[0].id", Matchers.is(bookingDto.getId()), Long.class));

        verify(bookingService, times(1)).getBookingByUser(any(), any());
    }

    @Test
    void getBookingItemByUser() throws Exception {
        when(bookingService.getBookingItemByUser(any(), any())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.[0].id", Matchers.is(bookingDto.getId()), Long.class));

        verify(bookingService, times(1)).getBookingItemByUser(any(), any());
    }

}