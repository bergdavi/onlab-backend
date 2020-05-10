package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameRepository extends CrudRepository<JpaGame, String> {
    
}