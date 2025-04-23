package com.practice.shareitgateway.booking;

import com.practice.shareitserver.booking.BookingCreateDto;
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
public class BookingClient {
    private final RestTemplate restTemplate;

    public BookingClient(@Value("${shareit.server.url}") String url,
                      RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public ResponseEntity<Object> create(int userId, BookingCreateDto bookingCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<BookingCreateDto> entity = new HttpEntity<>(bookingCreateDto, headers);
        return restTemplate.postForEntity("/bookings", entity, Object.class);
    }

    public ResponseEntity<Object> updateStatus(int userId, int bookingId, Boolean approved) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/bookings/{bookingId}", HttpMethod.PATCH, entity,
                Object.class,
                Map.of("bookingId", bookingId, "approved", approved));
    }

    public ResponseEntity<Object> findById(int bookingId) {
        return restTemplate.getForEntity("/bookings/{bookingId}", Object.class,
                Map.of("bookingId", bookingId));
    }

    public ResponseEntity<Object> findAllByCurrentUser(int userId, String state, int from, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/bookings?state={state}&from={from}&size={size}", HttpMethod.GET,
                entity,
                Object.class,
                Map.of("state", state, "from", from, "size", size));
    }

    public ResponseEntity<Object> findAllByOwner(int userId, String state, int from, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(RequestConstants.USER_ID_HEADER, String.valueOf(userId));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("/bookings/owner?state={state}&from={from}&size={size}", HttpMethod.GET,
                entity,
                Object.class,
                Map.of("state", state, "from", from, "size", size));
    }
}
