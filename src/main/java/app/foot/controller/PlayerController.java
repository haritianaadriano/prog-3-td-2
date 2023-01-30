package app.foot.controller;

import app.foot.controller.rest.CreatePlayer;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping("/players")
    public List<Player> insertPlayers(@RequestBody List<CreatePlayer> toSave){
        return service.savePlayers(toSave).stream()
                .map(mapper::toRest)
                .toList();
    }

    //TODO: add POST /players and add integration test ok and ko
}
