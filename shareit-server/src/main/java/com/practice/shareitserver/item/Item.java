package com.practice.shareitserver.item;

import com.practice.shareitserver.booking.Booking;
import com.practice.shareitserver.comment.Comment;
import com.practice.shareitserver.request.Request;
import com.practice.shareitserver.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    @Column(name = "is_available")
    Boolean available;
    @ManyToOne
    @JoinColumn(name="owner_id")
    User owner;
    @OneToMany(mappedBy = "item")
    final List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    final List<Booking> bookings = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "request_id")
    Request request;
}
