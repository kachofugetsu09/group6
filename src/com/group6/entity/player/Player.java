package com.group6.entity.player;

import com.group6.entity.common.Card;
import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class Player {
    //当前玩家职业
    private RoleType roletype;
    //当前玩家颜色
    private String  color;
    //当前玩家位置
    private Tile currentPosition;
    //手牌
    private ArrayList<Card>  hand = new ArrayList<>();
    //剩余行动点数
    private int actions;
    private boolean hasTurn;
    private Card selectedCard =null;


    // 回合管理
    public void startTurn() {
        this.hasTurn = true;
        this.actions = 3;
        onTurnStart();
    }

    private void onTurnStart() {
    }

    public void endTurn() {
        this.hasTurn = false;
        this.actions = 0;
        onTurnEnd();
    }

    private void onTurnEnd() {
    }

    public boolean move(Tile  destination){
        if(actions<=0){
            return false;
        }
        if(!canMoveTo(destination)){
            return false;
        }
        this.currentPosition = destination;
        this.actions--;
        return true;
    }

    private boolean canMoveTo(Tile destination) {
        if(destination.isFlooded()){
            return false;
        }
        return currentPosition.isNearBy(destination);
    }

    public abstract void performSpecialAbility(Object... params);

    public void shoreUp(Tile tile){// 如果flood但没有sunk，就可以提起来，否则没flooded或已经sunk都不能抬起来
        if(tile.isSunk() || !tile.isFlooded()){
            return;
        }
        if(this.getCurrentPosition().isNearBy(tile)&&actions>0){
            tile.setFlooded(false);
            this.actions--;
        }
    };
    public boolean isMoveAble(){
        return this.actions>0;
    }

    public boolean passCardTo(Player other,Card card){
        if(other.hand.size()==5&&card==null){
            return false;
        }
        this.hand.remove(card);
        other.hand.add(card);
        card.setOwner(other);
        return true;
    }

}
