package com.practice.shareitgateway.item;

import com.practice.shareitserver.comment.CommentCreateDto;
import com.practice.shareitserver.item.ItemCreateDto;
import com.practice.shareitserver.item.ItemDto;
import com.practice.shareitserver.utils.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemClient {
    private final RestTemplate restTemplate;

    public ResponseEntity<Object> create(int userId, ItemCreateDto itemCreateDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<ItemCreateDto> entity = new HttpEntity<>(itemCreateDto, httpHeaders);
        return restTemplate.postForEntity("/items", entity, Object.class);
    }

    public ResponseEntity<Object> update(int userId, int itemId, ItemDto itemDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<ItemDto> entity = new HttpEntity<>(itemDto, httpHeaders);
        return restTemplate.exchange("/items", HttpMethod.PATCH, entity, Object.class, Map.of("itemId", itemId));
    }

    public ResponseEntity<Object> findById(int itemId) {
        return restTemplate.getForEntity("/items/{itemId}",
                Object.class,
                Map.of("itemId", itemId)
        );
    }

    public ResponseEntity<Object> findAllOwnItems(int userId, int from, int size) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange("/items?from={from}&size={size}", HttpMethod.GET, objectHttpEntity,
                Object.class,
                Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> findByText(String text, int from, int size) {
        return restTemplate.exchange("/items/search?text={text}&from={from}&size={size}", HttpMethod.GET, null,
                Object.class,
                Map.of("text", text, "from", from, "size", size));
    }

    public ResponseEntity<Object> createComment(int userId, int itemId, CommentCreateDto commentCreateDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<CommentCreateDto> entity = new HttpEntity<>(commentCreateDto, httpHeaders);
        return restTemplate.postForEntity("/items/{itemId}/comment", entity, Object.class,
                Map.of("itemId", itemId));
    }
}
