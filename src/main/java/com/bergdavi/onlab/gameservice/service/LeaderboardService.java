package com.bergdavi.onlab.gameservice.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bergdavi.onlab.gameservice.jpa.model.GameplayResult;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.UserGameplayRepository;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.GameLeaderboard;
import com.bergdavi.onlab.gameservice.model.Leaderboard;
import com.bergdavi.onlab.gameservice.model.LeaderboardEntry;
import com.bergdavi.onlab.gameservice.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeaderboardService {

    @Autowired
    private UserGameplayRepository userGameplayRepository;

    @Autowired
    private ConversionService conversionService;

    public Leaderboard getGlobalLeaderboard() {
        Iterable<JpaUserGameplay> gameplays = userGameplayRepository.findAll();
        if(!gameplays.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<LeaderboardEntry> entries = getLeaderboardEntries(gameplays);
        return new Leaderboard(entries);
    }

    public GameLeaderboard getGameLeaderboard(String gameId) {
        Set<JpaUserGameplay> gameplays = userGameplayRepository.getGameplaysByGame(gameId);
        if(gameplays.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }        

        List<LeaderboardEntry> entries = getLeaderboardEntries(gameplays);

        return new GameLeaderboard(
            conversionService.convert(gameplays.iterator().next().getGameplay().getGame(), Game.class),
            entries
        );
    }

    private List<LeaderboardEntry> getLeaderboardEntries(Iterable<JpaUserGameplay> gameplays) {
        Map<JpaUser, Integer> wins = new HashMap<>();

        for(JpaUserGameplay gameplay : gameplays) {
            if(gameplay.getResult() == GameplayResult.WIN) {
                JpaUser user = gameplay.getUser();
                if(wins.containsKey(user)) {
                    wins.put(user, wins.get(user) + 1);
                } else {
                    wins.put(user, 1);
                }
            }
        }

        List<LeaderboardEntry> entries = new LinkedList<>();
        for(Entry<JpaUser, Integer> win : wins.entrySet()) {
            entries.add(new LeaderboardEntry(null, conversionService.convert(win.getKey(), User.class), win.getValue().toString()));
        }

        entries.sort((e1, e2) -> Integer.parseInt(e2.getScore()) - Integer.parseInt(e1.getScore()));
        for(int pos = 1; pos <= entries.size(); pos++) {
            entries.get(pos - 1).setPosition((long)pos);
        }

        return entries;
    }
}
