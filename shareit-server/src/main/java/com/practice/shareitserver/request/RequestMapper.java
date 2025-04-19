package com.practice.shareitserver.request;

import com.practice.shareitserver.item.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    private final ItemMapper itemMapper;
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
