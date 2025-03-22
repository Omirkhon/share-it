package com.practice.shareit.request;

import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.user.UserRepository;
import com.practice.shareit.utils.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
        return requestRepository.save(request);
    }

    public List<Request> findAllByUser(int userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    public Request findById(int userId, int id) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }

    public List<Request> findByPageAndSize(@RequestHeader(RequestConstants.HEADER) int userId, @RequestParam int page, @RequestParam int size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Request> pageResult = requestRepository.findAll(pageable);
        return pageResult.getContent();
    }
}
