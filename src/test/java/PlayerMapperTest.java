import app.foot.model.Player;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.PlayerMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO-2: complete these tests
public class PlayerMapperTest {
    PlayerMapper subject = mock(PlayerMapper.class);
    @Test
    void player_to_domain_ok() {
        Player expect = Player.builder()
                .id(1)
                .name("string")
                .teamName("T1")
                .isGuardian(false)
                .build();
        PlayerEntity toTest = PlayerEntity.builder()
                .team(TeamEntity.builder()
                        .name("T1")
                        .id(1)
                        .build())
                .build();
        when(subject.toDomain(toTest)).thenReturn(expect);
        Player actual = subject.toDomain(toTest);
        assertEquals(expect, actual);
    }

    @Test
    void player_scorer_to_domain_ok() {

    }

    @Test
    void player_scorer_to_entity_ok() {

    }
}
