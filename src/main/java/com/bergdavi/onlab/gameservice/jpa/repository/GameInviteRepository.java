package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvite;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameInviteRepository extends CrudRepository<JpaGameInvite, String> {
    
}