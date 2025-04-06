package com.practice.shareit.item;

import com.practice.shareit.booking.Booking;
import com.practice.shareit.booking.BookingRepository;
import com.practice.shareit.booking.Status;
import com.practice.shareit.comment.Comment;
import com.practice.shareit.comment.CommentCreateDto;
import com.practice.shareit.comment.CommentRepository;
import com.practice.shareit.exceptions.NotFoundException;
import com.practice.shareit.exceptions.ValidationException;
import com.practice.shareit.request.Request;
import com.practice.shareit.request.RequestRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RequestRepository requestRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    ItemService itemService;

    @Test
    void createWithoutRequest_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Item item = new Item();
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName(item.getName());
        itemCreateDto.setAvailable(item.getAvailable());
        itemCreateDto.setDescription(item.getDescription());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        Item savedItem = itemService.create(user.getId(), itemCreateDto);

        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getDescription(), savedItem.getDescription());
        assertEquals(item.getAvailable(), savedItem.getAvailable());
    }

    @Test
    void create_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setDescription("Запрос");
        request.setCreated(LocalDateTime.now());

        Item item = new Item();
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setRequest(request);

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName(item.getName());
        itemCreateDto.setAvailable(item.getAvailable());
        itemCreateDto.setDescription(item.getDescription());
        itemCreateDto.setRequestId(item.getRequest().getId());

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(request));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        Item savedItem = itemService.create(user.getId(), itemCreateDto);

        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getDescription(), savedItem.getDescription());
        assertEquals(item.getAvailable(), savedItem.getAvailable());
        assertEquals(item.getRequest().getId(), savedItem.getRequest().getId());
    }

    @Test
    void create_epicFail_notFoundUser() {
        String message = "Пользователь не найден";

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("Описание");

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.create(1, itemCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void create_epicFail_notFoundRequest() {
        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        String message = "Запрос не найден";

        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Предмет");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setDescription("Описание");
        itemCreateDto.setRequestId(10);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(requestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.create(user.getId(), itemCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void createComment_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Request request = new Request();
        request.setId(1);
        request.setDescription("Запрос");
        request.setCreated(LocalDateTime.now());

        Item item = new Item();
        item.setId(8);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setRequest(request);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(2);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2020, 10, 20, 9, 0, 5));

        Comment comment = new Comment();
        comment.setText("Фуу отписка");

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndItem(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));

        when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText(comment.getText());

        Comment savedComment = itemService.createComment(user.getId(), item.getId(), commentCreateDto);

        assertEquals(comment.getText(), savedComment.getText());
    }

    @Test
    void createComment_epicFail_ItemNotFound() {
        String message = "Вещь не найдена";

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Фуу не берите");

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.createComment(1, 1, commentCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void createComment_epicFail_UserNotFound() {
        String message = "Пользователь не найден";

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Фуу не берите");

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.createComment(item.getId(), 1, commentCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void createComment_epicFail_WrongBooking() {
        String message = "Вы не брали данную вещь в аренду";

        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Фуу не берите");

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndItem(Mockito.any(), Mockito.any()))
                .thenReturn(List.of());

        ValidationException e = assertThrows(ValidationException.class, () -> itemService.createComment(item.getId(), user.getId(), commentCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void createComment_epicFail_WrongBookingStatus() {
        String message = "Вы не брали данную вещь в аренду.";

        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Фуу не берите");

        Booking booking = new Booking();
        booking.setId(2);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.REJECTED);
        booking.setStartDate(LocalDateTime.of(2020, 10, 20, 9, 0, 5));

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndItem(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));

        ValidationException e = assertThrows(ValidationException.class, () -> itemService.createComment(item.getId(), user.getId(), commentCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void createComment_epicFail_WrongCreatedTime() {
        String message = "Вы не брали данную вещь в аренду.";

        User user = new User();
        user.setId(1);
        user.setName("Орангутан");
        user.setEmail("orangutan@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Фуу не берите");

        Booking booking = new Booking();
        booking.setId(2);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        booking.setStartDate(LocalDateTime.of(2026, 10, 20, 9, 0, 5));

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByBookerAndItem(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(booking));

        ValidationException e = assertThrows(ValidationException.class, () -> itemService.createComment(item.getId(), user.getId(), commentCreateDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void findById_epicSuccess() {
        Item item = new Item();
        item.setId(9);
        item.setName("Вещь");
        item.setAvailable(true);
        item.setDescription("Описание вещи");

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        Item foundItem = itemService.findById(item.getId());
        assertEquals(item.getId(), foundItem.getId());
    }

    @Test
    void findById_epicFail_ItemNotFound() {
        String message = "Вещь не найдена.";

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.findById(9));
        assertEquals(message, e.getMessage());
    }

    @Test
    void findByText_epicSuccess() {
        String text = "пр";

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Название");
        item2.setAvailable(true);
        item2.setDescription("Описание предмета 2");

        when(itemRepository.search(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(item, item2)));

        List<Item> items = itemService.findByText(text, 0, 5);

        assertEquals(List.of(item, item2), items);
    }

    @Test
    void findByText_epicFail_textIsBlank() {
        String text = "";

        List<Item> items = itemService.findByText(text, 0, 5);

        assertEquals(List.of(), items);
    }

    @Test
    void findByText_epicFail_textIsNull() {
        List<Item> items = itemService.findByText(null, 0, 5);

        assertEquals(List.of(), items);
    }

    @Test
    void findAllOwnItems_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Бабуин");
        user.setEmail("monkey@gmail.com");

        Item item = new Item();
        item.setId(1);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Название");
        item2.setAvailable(true);
        item2.setDescription("Описание предмета 2");
        item.setOwner(user);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findAllByOwner(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(item, item2)));

        List<Item> items = itemService.findAllOwnItems(user.getId(), 0, 5);

        assertEquals(List.of(item, item2), items);
    }

    @Test
    void findAllOwnItems_epicFail_userNotFound() {
        String message = "Пользователь не найден";

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.findAllOwnItems(10, 0, 5));

        assertEquals(message, e.getMessage());
    }

    @Test
    void updateName_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Бабуин");
        user.setEmail("monkey@gmail.com");

        Item item = new Item();
        item.setId(10);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        Item newItem = new Item();
        newItem.setName("Пользователь");

        ItemDto newItemDto = new ItemDto();
        newItemDto.setName(newItem.getName());

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(newItem);

        Item result = itemService.update(user.getId(), item.getId(), newItemDto);

        assertEquals(newItem.getName(), result.getName());
    }

    @Test
    void updateDescription_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Бабуин");
        user.setEmail("monkey@gmail.com");

        Item item = new Item();
        item.setId(10);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        Item newItem = new Item();
        newItem.setDescription("Новое описание");

        ItemDto newItemDto = new ItemDto();
        newItemDto.setDescription(newItem.getDescription());

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(newItem);

        Item result = itemService.update(user.getId(), item.getId(), newItemDto);

        assertEquals(newItem.getDescription(), result.getDescription());
    }

    @Test
    void updateAvailable_epicSuccess() {
        User user = new User();
        user.setId(1);
        user.setName("Бабуин");
        user.setEmail("monkey@gmail.com");

        Item item = new Item();
        item.setId(10);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        Item newItem = new Item();
        newItem.setAvailable(false);

        ItemDto newItemDto = new ItemDto();
        newItemDto.setAvailable(newItem.getAvailable());

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(newItem);

        Item result = itemService.update(user.getId(), item.getId(), newItemDto);

        assertEquals(newItem.getAvailable(), result.getAvailable());
    }

    @Test
    void update_epicFail_ItemNotFound() {
        String message = "Вещь не найдена.";

        ItemDto newItemDto = new ItemDto();

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.update(1, 1, newItemDto));

        assertEquals(message, e.getMessage());
    }

    @Test
    void update_epicFail_WrongUser() {
        String message = "У вас нет такой вещи";

        User user = new User();
        user.setId(1);
        user.setName("Горилла");
        user.setEmail("monkey@gmail.com");

        Item item = new Item();
        item.setId(10);
        item.setName("Предмет");
        item.setAvailable(true);
        item.setDescription("Описание");
        item.setOwner(user);

        ItemDto newItemDto = new ItemDto();

        when(itemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(item));

        NotFoundException e = assertThrows(NotFoundException.class, () -> itemService.update(2, item.getId(), newItemDto));

        assertEquals(message, e.getMessage());
    }
}
