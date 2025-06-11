package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ErrorHandlerTest {


    @InjectMocks
    private ErrorHandler handler;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws IOException {
        // Готовим исходные данные
        FieldError fieldError = new FieldError("dto", "field", "must not be empty");
        BindingResult bindingResult = new BindException(new Object(), "object");
        bindingResult.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Вызываем обработчик вручную
        ErrorResponse response = handler.handleMethodArgumentNotValidException(exception);

        // Проверяем результат
        assertEquals("must not be empty", response.getError());
    }

}