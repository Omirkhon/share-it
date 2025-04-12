package com.practice.shareit.request;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.user.User;
import com.practice.shareit.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RequestService requestService;

    @Test
    void create_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setDescription("Запрос");

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.save(Mockito.any()))
                .thenReturn(request);

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription(request.getDescription());

        Request savedRequest = requestService.create(user.getId(), requestCreateDto);
        assertEquals(request.getDescription(), savedRequest.getDescription());
    }

    @Test
    void create_epicFail_userNotFound() {
        String message = "Пользователь не найден";

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("Запрос");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.create(1, requestCreateDto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findById_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setDescription("Описание");
        request.setCreated(LocalDateTime.now());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(request));

        Request foundRequest = requestService.findById(user.getId(), request.getId());

        assertEquals(request.getId(), foundRequest.getId());
        assertEquals(request.getDescription(), foundRequest.getDescription());
        assertEquals(request.getCreated(), foundRequest.getCreated());
    }

    @Test
    void findById_epicFail_userNotFound() {
        String message = "Пользователь не найден";

        Request request = new Request();
        request.setId(1);
        request.setDescription("Описание");
        request.setCreated(LocalDateTime.now());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.findById(1, request.getId()));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findById_epicFail_requestNotFound() {
        String message = "Запрос не найден";

        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.findById(user.getId(), 1));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findByPageAndSize_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setDescription("Описание");
        request.setCreated(LocalDateTime.now());

        Request request2 = new Request();
        request2.setId(1);
        request2.setDescription("Описание");
        request2.setCreated(LocalDateTime.now());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.findAllWhereNotRequesterId(Mockito.anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(request, request2)));

        List<Request> requests = requestService.findByPageAndSize(user.getId(), 0, 5);

        assertEquals(List.of(request, request2), requests);
    }

    @Test
    void findByPageAndSize_epicFail_userNotFound() {
        String message = "Пользователь не найден";

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.findByPageAndSize(1, 0, 5));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAllByUser_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Пользователь");
        user.setEmail("user@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setDescription("Описание");
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);

        Request request2 = new Request();
        request2.setId(1);
        request2.setDescription("Описание");
        request2.setCreated(LocalDateTime.now());
        request2.setRequester(user);

        when(requestRepository.findAllByRequesterId(Mockito.anyInt()))
                .thenReturn(List.of(request, request2));

        List<Request> requests = requestService.findAllByUser(user.getId());

        assertEquals(List.of(request, request2), requests);
    }
}
