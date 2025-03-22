package com.practice.shareit.request;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public Request create(int userId, RequestCreateDto requestCreateDto) {
        Request request = new Request();
        request.setDescription(requestCreateDto.getDescription());
        request.setRequester(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public List<Request> findAllByUser(int userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    public Request findById(int userId, int id) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }

    public List<Request> findByPageAndSize(int userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Request> pageResult = requestRepository.findAll(pageable);
        return pageResult.getContent();
    }
}
