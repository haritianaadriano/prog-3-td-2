import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.validator.GoalValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO-1: complete these tests
public class GoalValidatorTest {
    GoalValidator subject = new GoalValidator();

    private Player scorer() {
        return Player.builder()
                .id(1)
                .name("John Doe")
                .isGuardian(false)
                .build();
    }

    private Player scorer_guardian() {
        return Player.builder()
                .id(2)
                .name("John Doe")
                .isGuardian(true)
                .build();
    }

    private PlayerScorer player_scorer(Player john) {
        return PlayerScorer.builder()
                .player(john)
                .isOG(false)
                .scoreTime(26)
                .build();
    }

    private PlayerScorer player_scorer_with_scoring_time(Integer scoringTime) {
        return PlayerScorer.builder()
                .player(scorer())
                .isOG(false)
                .scoreTime(scoringTime)
                .build();
    }


    private PlayerScorer player_scorer_with_no_score_time() {
        return PlayerScorer.builder()
                .player(scorer())
                .scoreTime(null)
                .isOG(true)
                .build();
    }

    @Test
    void accept_ok() {
        subject.accept(player_scorer(scorer()));
        assertDoesNotThrow(() -> new RuntimeException());
    }


    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {
        RuntimeException error = assertThrows(RuntimeException.class, () -> subject.accept(player_scorer_with_no_score_time()));

        String expectedMessage = "Score minute is mandatory.";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void when_guardian_throws_exception() {
        PlayerScorer scorer = player_scorer(scorer_guardian());

        RuntimeException error = assertThrows(RuntimeException.class, () -> subject.accept(scorer));
        String expectedMessage = "Player#" + scorer.getPlayer().getId() + " is a guardian so they cannot score.";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {
        PlayerScorer scorer = player_scorer_with_scoring_time(91);

        RuntimeException error = assertThrows(RuntimeException.class, () -> subject.accept(scorer));
        String expectedMessage = "Player#" + scorer.getPlayer().getId() + " cannot score after minute 90.";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void when_score_time_less_than_0_throws_exception() {
        PlayerScorer scorer = player_scorer_with_scoring_time(-90);

        RuntimeException error = assertThrows(RuntimeException.class, () -> subject.accept(scorer));
        String expectedMessage = "Player#" + scorer.getPlayer().getId() + " cannot score before minute 0.";
        String actualMessage = error.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}