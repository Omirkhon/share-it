package com.practice.shareit.item;

import com.practice.shareit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwner(User owner);

    List<Item> findByNameContainingOrDescriptionContaining(String text, String text2);
}
