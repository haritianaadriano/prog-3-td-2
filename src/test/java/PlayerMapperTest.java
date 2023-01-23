import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.PlayerMapper;
import app.foot.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO-2: complete these tests
public class PlayerMapperTest {
    PlayerMapper subject = mock(PlayerMapper.class);
    DateUtils dateUtils = mock(DateUtils.class);
    @Test
    void player_to_domain_ok() {
        Player expect = Player.builder()
                .id(1)
                .name("string")
                .teamName("T1")
                .isGuardian(false)
                .build();
        PlayerEntity actual = PlayerEntity.builder()
                .team(TeamEntity.builder()
                        .name("T1")
                        .id(1)
                        .build())
                .build();
        when(subject.toDomain(actual)).thenReturn(expect);
        assertEquals(expect, subject.toDomain(actual));
    }

    @Test
    void player_scorer_to_domain_ok() {
        PlayerScorer expect = PlayerScorer.builder()
                .player(Player.builder()
                        .isGuardian(false)
                        .teamName("T1")
                        .name("string")
                        .id(1)
                        .build())
                .build();
        PlayerScoreEntity actual = PlayerScoreEntity.builder()
                .match(MatchEntity.builder()
                        .datetime(Instant.now())
                        .stadium("Stade-1")
                        .teamB(TeamEntity.builder()
                                .id(1)
                                .name("T1")
                                .build())
                        .teamA(TeamEntity.builder()
                                .name("T2")
                                .id(2)
                                .build())
                        .scorers(new ArrayList<>())
                        .build())
                .build();
        when(subject.toDomain(actual)).thenReturn(expect);
        assertEquals(expect, subject.toDomain(actual));
    }

    @Test
    void player_scorer_to_entity_ok() {

    }
}
