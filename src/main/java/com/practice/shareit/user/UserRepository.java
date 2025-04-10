package com.practice.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);

    User deleteUserById(int id);
}
