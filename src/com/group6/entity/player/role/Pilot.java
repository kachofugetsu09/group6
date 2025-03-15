package com.group6.entity.player.role;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;

public class Pilot extends Player {

    @Override
    public void performSpecialAbility(Object... params) {
        if(params.length==1&&params[0] instanceof Tile){
            Tile destination = (Tile) params[0];
            this.setCurrentPosition(destination);
        }
    }
}
