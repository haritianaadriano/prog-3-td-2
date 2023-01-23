import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.validator.GoalValidator;
import org.junit.jupiter.api.Test;

//TODO-1: complete these tests
public class GoalValidatorTest {
    GoalValidator subject = new GoalValidator();

    @Test
    void accept_ok() {
    }

    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {

    }

    @Test
    void when_guardian_throws_exception() {

    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {

    }

    @Test
    void when_score_time_less_than_0_throws_exception() {

    }

    public static app.foot.controller.rest.PlayerScorer scorer_ok(){
        return PlayerScorer.builder()
                .isOG(true)
                .scoreTime(15)
                .player(Player.builder()
                        .id(1)
                        .isGuardian(false)
                        .name("rakoto")
                        .build())
                .build();
    }
}
