package com.bergdavi.onlab.gameservice.jpa.repository;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplayPk;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * UserGameplayRepository
 */
public interface UserGameplayRepository extends CrudRepository<JpaUserGameplay, JpaUserGameplayPk> {
    
    @Query("SELECT ug.userIdx FROM user_gameplays ug WHERE ug.id = ?1")
    public Integer getGameplayUserIdx(JpaUserGameplayPk jpaUserGameplayPk);

}