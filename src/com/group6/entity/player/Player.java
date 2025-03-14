package com.group6.entity.player;

import com.group6.entity.common.Card;
import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@AllArgsConstructor
@NoArgsConstructor
public abstract class Player {
    //当前玩家职业
    private RoleType roletype;
    //当前玩家颜色
    private String  color;
    //当前玩家位置
    private Tile currentPosition;
    //手牌
    private ArrayList<Card>  hand;
    //剩余行动点数
    private int actions;
    private boolean hasTurn;

    public Player(RoleType roleType, String color, Tile startingTile) {
        this.roletype = roleType;
        this.color = color;
        this.currentPosition = startingTile;
        this.hand = new ArrayList<>();
        this.actions = 3;
        this.hasTurn = false;
    }

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
    // 特殊能力相关（由具体角色实现）

    public abstract void performSpecialAbility();

    public void shoreUp(Tile tile){};
    public boolean isMoveAble(){
        return actions>0;
    }



}
