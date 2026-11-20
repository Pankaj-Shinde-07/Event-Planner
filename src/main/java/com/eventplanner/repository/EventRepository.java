package com.eventplanner.repository;

import com.eventplanner.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e left join fetch e.user left join fetch e.theme left join fetch e.booking order by e.eventDate asc")
    List<Event> findAllForList();

    @Query("select distinct e from Event e left join fetch e.user left join fetch e.theme " +
            "left join fetch e.decorations d left join fetch d.item left join fetch e.booking where e.id = :id")
    Optional<Event> findWithDetailsById(@Param("id") Long id);
}
