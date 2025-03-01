package com.practice.shareit.item;

import com.practice.shareit.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    String description;
    @Column(name = "is_available")
    @NotNull(message = "Статус наличия не указан")
    Boolean available;
    @ManyToOne
    @JoinColumn(name="owner_id")
    User owner;
}
