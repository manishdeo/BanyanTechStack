package com.banyan.stack.repository;

import com.banyan.stack.entity.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {

    @Query("SELECT t FROM TvShow t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<TvShow> findByNameContainingIgnoreCase(@Param("name") String name);
}