package com.group6.entity.player.role;

import com.group6.entity.common.Card;
import com.group6.entity.player.Player;

public class Messenger extends Player {

    @Override
    public void performSpecialAbility(Object... params) {
        if(params.length>=2&& params[0] instanceof Player&&params[1] instanceof Card){
            Player other = (Player) params[0];
            Card card = (Card) params[1];
            this.passCardTo(other,card);
        }
    }
}
