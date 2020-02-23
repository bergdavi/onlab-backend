package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameplayRepository extends CrudRepository<JpaGameplay, String> {
    
}