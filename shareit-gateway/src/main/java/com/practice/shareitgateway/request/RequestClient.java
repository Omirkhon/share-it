package com.practice.shareitgateway.request;

import com.practice.shareitserver.request.RequestCreateDto;
import com.practice.shareitserver.utils.RequestConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Component
public class RequestClient {
    private final RestTemplate restTemplate;

    public RequestClient(@Value("${posts.server.url}") String url,
                      RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public ResponseEntity<Object> create(int userId, RequestCreateDto requestCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<RequestCreateDto> entity = new HttpEntity<>(requestCreateDto, headers);
        return restTemplate.postForEntity("/requests", entity, Object.class);
    }

    public ResponseEntity<Object> findAllByUser(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<RequestCreateDto> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/requests", HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> findById(int userId, int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<RequestCreateDto> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/requests/{id}", HttpMethod.GET, entity, Object.class,
                Map.of("id", id));
    }

    public ResponseEntity<Object> findByPageAndSize(int userId, int from, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<RequestCreateDto> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/requests/all?from={from}&size={size}", HttpMethod.GET, entity, Object.class,
                Map.of("from", from, "size", size));
    }
}
