package com.group6.entity.player.role;

import com.group6.controller.GameController;
import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Pilot extends Player {

    @Override
    public void performSpecialAbility(Object... params) {
    }

    public List<Tile> getAvailableDestinations(){
        List<Tile> result = new ArrayList<>();
        List<Tile> tiles = getGameController().getGameBoard().getTiles();
        for(Tile tile:tiles){
            if(!tile.isFlooded()){
                result.add(tile);
            }
        }
        return result;
    }
    @Override
    public boolean move(Tile destination){
        if(getActions()>0){
            List<Tile> availableDestinations = getAvailableDestinations();
            if(availableDestinations.contains(destination)){
                setActions(getActions()-1);
                this.setCurrentPosition(destination);
                return true;
            }
        }
        return false;
    }
}
