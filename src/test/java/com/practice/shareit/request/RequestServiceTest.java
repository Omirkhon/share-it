package com.practice.shareit.request;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    RequestRepository requestRepository;
    @InjectMocks
    RequestService requestService;
}
