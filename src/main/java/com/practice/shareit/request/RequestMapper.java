package com.practice.shareit.request;

import com.practice.shareit.item.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    ItemMapper itemMapper;
    public RequestReadDto toDto(Request request) {
        RequestReadDto requestReadDto = new RequestReadDto();
        requestReadDto.setId(request.getId());
        requestReadDto.setDescription(request.getDescription());
        requestReadDto.setCreated(request.getCreated());
        requestReadDto.setItems(itemMapper.toDto(request.getItems()));
        return requestReadDto;
    }

    public List<RequestReadDto> toDto(List<Request> requests) {
        return requests.stream()
                .map(this::toDto)
                .toList();
    }
}
