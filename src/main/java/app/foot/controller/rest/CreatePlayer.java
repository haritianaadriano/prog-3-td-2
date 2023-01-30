package app.foot.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayer {
    private String name;
    private Boolean isGuardian;
}
