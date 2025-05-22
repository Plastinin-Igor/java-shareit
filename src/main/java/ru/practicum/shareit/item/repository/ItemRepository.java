package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_Id(Long userId);

    @Query("select it " +
           "  from Item as it " +
           "where (lower(it.name) like concat('%', :searchText, '%') or lower(it.description) like concat('%', :searchText, '%'))  " +
           "   and it.available = true")
    List<Item> findByText(@Param("searchText") String searchText);


}
