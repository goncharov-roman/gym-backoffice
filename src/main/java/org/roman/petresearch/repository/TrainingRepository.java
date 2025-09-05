package org.roman.petresearch.repository;

import org.roman.petresearch.entity.Training;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {

    @Query("SELECT * FROM trainings ORDER BY name")
    List<Training> findAllOrderByName();

    @Query("SELECT * FROM trainings WHERE name ILIKE :name ORDER BY name")
    List<Training> findByNameContaining(@Param("name") String name);
} 