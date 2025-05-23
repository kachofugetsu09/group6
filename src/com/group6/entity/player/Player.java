package com.group6.entity.player;

import com.group6.controller.GameController;
import com.group6.entity.common.Card;
import com.group6.entity.common.RoleType;
import com.group6.entity.common.Tile;
import com.group6.entity.gameBoard.GameBoard;
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
    //当前玩家职业234
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
    private boolean isStandingOnTreasure = false;   //是否站在宝藏上

    GameController gameController;


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
        if(destination.isTreasure()){
            this.isStandingOnTreasure = true;
        }
        return true;
    }

    public boolean takeTreasure(){//如果站在宝藏上，则可以带走宝藏  ，后面还要加进去：如果手牌数可以，并且玩家选择带走宝藏，则可以带走宝藏
        if(this.isStandingOnTreasure && countCardNumbers(this.currentPosition.getTreasure().getName(),this.hand)>=4){
            this.currentPosition.setTreasure(null); //表示tile没有宝物，用于更新地图
            String treasureName = this.currentPosition.getTreasure().getName();
            this.currentPosition.getTreasure().setCaptured(true);//表示这个宝物已经拿到，用于更新地图
            GameController.getCapturedTreasures().put(treasureName, true);//在controller里面表示这中宝物已经拿到
            return true;
        }
        return false;
    }

    protected boolean canMoveTo(Tile destination) {
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

    public int countCardNumbers(String cardName,ArrayList<Card> hand){//计算手牌中有几张某个牌
        int count = 0;
        for(Card card:hand){
            if(card.getName().equals(cardName)){
                count++;
            }
        }
        return count;
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
    public boolean judgingFlooded(){
        if(this.currentPosition.isFlooded()){
            actions++;
            return true;
        }
        return false;
    }

    /**
     * 检查是否有可移动的位置
     * @return true 如果有可移动的位置，false 如果没有
     */
    public boolean hasMovablePosition(Player player) {
        int currentX = currentPosition.getPosition().x;
        int currentY = currentPosition.getPosition().y;

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int newX = currentX + dx[i];
            int newY = currentY + dy[i];

            if (newX >= 0 && newX < 6 && newY >= 0 && newY < 6) {
                // 获取该位置的瓦片
               Tile adjacentTile = player.getGameController().findTileAt(newX, newY);

                if (adjacentTile != null && canMoveTo(adjacentTile)) {
                    return true;
                }
            }
        }
        return false;
    }


}
