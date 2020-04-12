package com.bergdavi.onlab.gameservice.jpa.repository;

import java.util.List;

import javax.persistence.LockModeType;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueuePk;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * UserRepository
 */
public interface GameQueueRepository extends CrudRepository<JpaGameQueue, JpaGameQueuePk> {

    // TODO there is probably a better way to get the top user
    @Query(value = "SELECT gq FROM game_queue gq WHERE game_id = ?1 ORDER BY joined DESC")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public List<JpaGameQueue> getTopUsersInQueue(String game, Pageable page);

}