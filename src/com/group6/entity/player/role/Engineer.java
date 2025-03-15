package com.group6.entity.player.role;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;

public class Engineer extends Player {
    @Override
    public void performSpecialAbility(Object... params) {
        if (params.length >= 2 && params[0] instanceof Tile && params[1] instanceof Tile) {
            Tile tile1 = (Tile) params[0];
            Tile tile2 = (Tile) params[1];
            shoreUp(tile1,tile2);
        }
    }

    public void shoreUp(Tile tile1,Tile tile2){
        shoreUp(tile1);
        setActions(getActions()+1);
        shoreUp(tile2);
    }


}
