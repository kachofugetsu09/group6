package com.group6.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {
    //类型名称
    private CardType type;
    private String name;
    @JsonIgnore
    private Player owner;

}
