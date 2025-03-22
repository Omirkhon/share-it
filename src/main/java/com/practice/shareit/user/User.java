package com.practice.shareit.user;

import com.practice.shareit.booking.Booking;
import com.practice.shareit.comment.Comment;
import com.practice.shareit.request.Request;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String email;
    @OneToMany(mappedBy = "booker")
    final List<Booking> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "author")
    final List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "requester")
    final List<Request> requests = new ArrayList<>();
}
