package integration;

import app.foot.FootApi;
import app.foot.controller.rest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class MatchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping

    @Test
    void read_matches_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<Match> actual = convertFromHttpResponse_match(response);

        assertEquals(3, actual.size());
        assertTrue(actual.contains(expectedMatch2()));
        //TODO: add these checks and its values
        System.out.println(actual.get(2));
        System.out.println(expectedMatch3());
        assertTrue(actual.contains(expectedMatch1()));
        assertTrue(actual.contains(expectedMatch3()));
    }

    @Test
    void create_score_ok() throws Exception{
        PlayerScorer toCreate = PlayerScorer.builder()
                .scoreTime(75)
                .isOG(true)
                .player(Player.builder()
                        .id(1)
                        .name("J1")
                        .isGuardian(false)
                        .teamName("E1")
                        .build())
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(
                         post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(toCreate)))
                        .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertEquals(expectedMatch3(), actual);
    }

    private List<Match> convertFromHttpResponse_match(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType match = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Match.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                match);
    }

    private List<PlayerScorer> convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType playerScorerType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Player.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                playerScorerType);
    }

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMatch2(), actual);
    }

    @Test
    void read_match_by_id_ko() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/matches/4")
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
        RuntimeException actual = objectMapper.readValue(
                response.getContentAsString(), RuntimeException.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Match#4 not found. ", actual.getMessage());
    }

    private static Match expectedMatch1(){
        return Match.builder()
                .id(1)
                .stadium("S1")
                .datetime(Instant.parse("2023-01-01T10:00:00Z"))
                .teamA(TeamMatch.builder()
                        .score(4)
                        .scorers(List.of(
                                j1_scorer(30),
                                j1_scorer(20),
                                j1_scorer(10),
                                j4_scorer()
                        ))
                        .team(Team.builder()
                                .name("E1")
                                .id(1)
                                .build())
                    .build())
                .teamB(TeamMatch.builder()
                        .score(2)
                        .team(Team.builder()
                                .id(2)
                                .name("E2")
                                .build())
                        .scorers(List.of(
                                j2_scorer(),
                                j3_scorer()
                            ))
                        .team(Team.builder()
                                .name("E2")
                                .id(2)
                                .build())
                        .build())
                .build();
    }

    public static PlayerScorer j1_scorer(int scoreTime){
        return PlayerScorer.builder()
                .scoreTime(scoreTime)
                .isOG(false)
                .player(Player.builder()
                        .teamName("E1")
                        .isGuardian(false)
                        .name("J1")
                        .teamName("E1")
                        .id(1)
                        .build())
                .build();
    }

    public static PlayerScorer j2_scorer(){
        return PlayerScorer.builder()
                .scoreTime(40)
                .isOG(true)
                .player(Player.builder()
                        .id(2)
                        .teamName("E1")
                        .name("J2")
                        .isGuardian(false)
                        .build())
                .build();
    }

    public static PlayerScorer j3_scorer(){
        return PlayerScorer.builder()
                .isOG(false)
                .scoreTime(50)
                .player(Player.builder()
                        .isGuardian(false)
                        .name("J3")
                        .teamName("E2")
                        .id(3)
                        .build())
                .build();
    }

    public static PlayerScorer j4_scorer(){
        return PlayerScorer.builder()
                .player(Player.builder()
                        .id(4)
                        .teamName("E2")
                        .name("J4")
                        .isGuardian(false)
                        .build())
                .scoreTime(60)
                .isOG(true)
                .build();
    }

    private static Match expectedMatch3(){
        return Match.builder()
                .id(3)
                .teamA(TeamMatch.builder()
                        .team(Team.builder()
                                .id(1)
                                .name("E1")
                                .build())
                        .scorers(List.of())
                        .score(0)
                        .build())
                .teamB(TeamMatch.builder()
                        .team(Team.builder()
                                .id(3)
                                .name("E3")
                                .build())
                        .scorers(List.of(PlayerScorer.builder()
                                        .player(Player.builder()
                                                .id(1)
                                                .name("J1")
                                                .isGuardian(false)
                                                .teamName("E1")
                                                .build())
                                        .isOG(true)
                                        .scoreTime(75)
                                        .build()))
                        .score(1)
                        .build())
                .datetime(Instant.parse("2023-01-01T18:00:00Z"))
                .stadium("S3")
                .build();
    }

    private static Match expectedMatch2() {
        return Match.builder()
                .id(2)
                .teamA(teamMatchA())
                .teamB(teamMatchB())
                .stadium("S2")
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(70)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(80)
                                .isOG(true)
                                .build()))
                .build();
    }

    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .teamName("E3")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .id(2)
                .name("E2")
                .build();
    }
}
