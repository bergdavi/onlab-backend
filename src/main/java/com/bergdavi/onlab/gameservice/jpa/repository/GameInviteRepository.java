package com.bergdavi.onlab.gameservice.jpa.repository;

import java.util.List;

import javax.persistence.LockModeType;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvite;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameInviteRepository extends CrudRepository<JpaGameInvite, String> {
    
}