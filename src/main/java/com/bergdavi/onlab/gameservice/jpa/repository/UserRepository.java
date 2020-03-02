package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<JpaUser, String> {
    public JpaUser findByUsername(String username);

    @Query("SELECT id FROM user_details ud WHERE username = ?1")
    public String getIdByUsername(String username);
}