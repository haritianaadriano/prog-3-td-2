package app.foot.repository.mapper;

import app.foot.controller.rest.CreatePlayer;
import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerMapper {
  private final MatchRepository matchRepository;
  private final PlayerRepository playerRepository;

  public PlayerEntity toDomain(CreatePlayer rest){
    return PlayerEntity.builder()
            .name(rest.getName())
            .guardian(rest.getIsGuardian())
            .team(TeamEntity.builder()
                    .name(rest.getName())
                    .build())
            .build();
  }

  public Player toDomain(PlayerEntity entity) {
    return Player.builder()
        .id(entity.getId())
        .name(entity.getName())
        .isGuardian(entity.isGuardian())
        .teamName(entity.getTeam().getName())
        .build();
  }

  public PlayerScorer toDomain(PlayerScoreEntity entity) {
    return PlayerScorer.builder()
        .player(toDomain(entity.getPlayer()))
        .minute(entity.getMinute())
        .isOwnGoal(entity.isOwnGoal())
        .build();
  }

  public PlayerScoreEntity toEntity(int matchId, PlayerScorer scorer) {
    return PlayerScoreEntity.builder()
        .player(playerRepository.findById(scorer.getPlayer().getId()).get())
        .match(matchRepository.findById(matchId).get())
        .ownGoal(scorer.getIsOwnGoal())
        .minute(scorer.getMinute())
        .build();
  }
}
