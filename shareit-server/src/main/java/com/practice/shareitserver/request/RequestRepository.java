package com.practice.shareitserver.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequesterId(int id);

    @Query("select r from Request r " +
            "where r.requester.id != ?1")
    Page<Request> findAllWhereNotRequesterId(int requesterId, Pageable pageable);
}
