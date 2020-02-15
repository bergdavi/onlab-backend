package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<JpaUser, String> {
        
}