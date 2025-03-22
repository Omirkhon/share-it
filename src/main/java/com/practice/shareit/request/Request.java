package com.practice.shareit.request;

import com.practice.shareit.item.Item;
import com.practice.shareit.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String description;
    @ManyToOne
    @JoinColumn(name="requester_id")
    User requester;
    @OneToMany(mappedBy = "request")
    final List<Item> items = new ArrayList<>();
    final LocalDateTime created = LocalDateTime.now();
}
