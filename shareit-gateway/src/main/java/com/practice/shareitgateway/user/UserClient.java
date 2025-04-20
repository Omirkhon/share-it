package com.practice.shareitgateway.user;

import com.practice.shareitserver.user.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Component
public class UserClient {
    private final RestTemplate restTemplate;

    public UserClient(@Value("${posts.server.url}") String url,
                      RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        return restTemplate.postForEntity("/users", userDto, Object.class);
    }

    public ResponseEntity<Object> findAll() {
        return restTemplate.getForEntity("/users",
                Object.class);
    }

    public ResponseEntity<Object> findById(int id) {
        return restTemplate.getForEntity("/users/{id}",
                Object.class,
                Map.of("id", id));
    }

    public ResponseEntity<Object> update(UserDto user, int userId) {
        HttpEntity<UserDto> entity = new HttpEntity<>(user);
        return restTemplate.exchange("/users/{userId}", HttpMethod.PATCH, entity, Object.class,
                Map.of("userId", userId));
    }

    public ResponseEntity<Object> delete(int userId) {
        return restTemplate.exchange("/users/{userId}", HttpMethod.DELETE, null, Object.class,
                Map.of("userId", userId));
    }
}
