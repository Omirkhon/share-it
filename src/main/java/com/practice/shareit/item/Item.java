package com.practice.shareit.item;

import com.practice.shareit.booking.Booking;
import com.practice.shareit.comment.Comment;
import com.practice.shareit.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
