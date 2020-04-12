package com.bergdavi.onlab.gameservice.jpa.repository;

import java.util.Set;

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

    @Query("SELECT username FROM user_details ud WHERE id = ?1")
    public String getUsernameById(String id);

    @Query("SELECT ud FROM user_details ud WHERE ud.username LIKE CONCAT('%',?1,'%')")
    public Set<JpaUser> searchByName(String username);
}