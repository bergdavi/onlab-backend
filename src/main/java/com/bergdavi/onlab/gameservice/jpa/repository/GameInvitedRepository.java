package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvited;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvitedPK;

import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameInvitedRepository extends CrudRepository<JpaGameInvited, JpaGameInvitedPK> {
}