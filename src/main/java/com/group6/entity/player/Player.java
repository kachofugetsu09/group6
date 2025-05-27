package com.group6.entity.player;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

import com.group6.controller.GameController;
import com.group6.entity.common.*;
import com.group6.entity.gameBoard.GameBoard;
import com.group6.entity.player.role.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Engineer.class, name = "ENGINEER"),
        @JsonSubTypes.Type(value = Pilot.class, name = "PILOT"),
        @JsonSubTypes.Type(value = Explorer.class, name = "EXPLORER"),
        @JsonSubTypes.Type(value = Diver.class, name = "DIVER"),
        @JsonSubTypes.Type(value = Messenger.class, name = "MESSENGER"),
        @JsonSubTypes.Type(value = Navigator.class, name = "NAVIGATOR")
})
@JsonAutoDetect(
        getterVisibility = NONE,
        isGetterVisibility = NONE,
        fieldVisibility = ANY
)
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
    @JsonIgnore
    private Tile currentPosition;
    //手牌
    private ArrayList<Card>  hand = new ArrayList<>();
    // 手牌上限
    private static final int MAX_HAND_SIZE = 5;
    //剩余行动点数
    private int actions;
    private boolean hasTurn;
    private Card selectedCard =null;
    private boolean isStandingOnTreasure = false;   //是否站在宝藏上

    @JsonIgnore
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
        if(destination.getTreasure() != null){
            this.isStandingOnTreasure = true;
        }
        return true;
    }


    // 添加卡牌到手牌（含上限检查）
    public boolean addCard(Card card) {
        if (hand.size() >= MAX_HAND_SIZE) {
            return false; // 手牌已满，无法添加
        }
        hand.add(card);
        card.setOwner(this);
        return true;
    }

    // 检查是否需要弃牌
    public boolean needsToDiscard() {
        return hand.size() > MAX_HAND_SIZE;
    }
    // 获取需要弃牌的数量
    public int getRequiredDiscardCount() {
        return hand.size() - MAX_HAND_SIZE;
    }

    // 弃置卡牌
    public boolean discardCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            card.setOwner(null);
            return true;
        }
        return false;
    }
    public boolean takeTreasure() {
        if (this.actions <= 0) return false;

        if (this.isStandingOnTreasure && countCardNumbers(this.currentPosition.getTreasure().getName(), this.hand) >= 4) {
            // 提前保存宝藏对象,避免空指针
            Treasure treasure = this.currentPosition.getTreasure();

            String treasureName = this.currentPosition.getTreasure().getName();

            // tile上移除宝藏
            this.currentPosition.setTreasure(null);
            // 设置该宝藏已被获得
            treasure.setCaptured(true);
            this.currentPosition.getTreasure().setCaptured(true); // 设置已被获取
            gameController.getCapturedTreasures().put(treasureName, true);
            this.actions--; // 消耗行动
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
        if (this.actions <= 0) return false;
        if(other.hand.size()==5&&card==null){
            return false;
        }
        this.hand.remove(card);
        other.hand.add(card);
        card.setOwner(other);
        this.actions--;
        return true;
    }

    // 与其他玩家交换卡牌（双向交易）
    public boolean tradeCardWith(Player other, Card cardToGive, Card cardToReceive) {
        // 检查交易条件
        if (!hand.contains(cardToGive) ||
                !other.hand.contains(cardToReceive) ||
                !currentPosition.isNearBy(other.currentPosition)) {
            return false;
        }

        // 执行交易
        if (discardCard(cardToGive) && other.discardCard(cardToReceive)) {
            addCard(cardToReceive);
            other.addCard(cardToGive);
            return true;
        }
        return false;
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
    public boolean hasMovablePosition() {
        int currentX = currentPosition.getPosition().x;
        int currentY = currentPosition.getPosition().y;

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int newX = currentX + dx[i];
            int newY = currentY + dy[i];

            if (newX >= 0 && newX < 6 && newY >= 0 && newY < 6) {
                Tile adjacentTile = gameController.findTileAt(newX, newY);
                if (adjacentTile != null && canMoveTo(adjacentTile)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 在Player类中添加使用直升机卡的方法
    public boolean useHelicopterCard(Tile destination) {
        if (hand.stream().anyMatch(card -> card.getType() == CardType.HELICOPTER) && actions > 0) {
            // 消耗一张直升机卡
            hand.removeIf(card -> card.getType() == CardType.HELICOPTER);
            actions--;

            // 移动所有玩家到目标位置
            gameController.moveAllPlayers(destination);
            return true;
        }
        return false;
    }

    // 在Player类中添加使用沙袋卡的方法
    public boolean useSandbagsCard(Tile tileToShoreUp) {
        if (hand.stream().anyMatch(card -> card.getType() == CardType.SANDBAG) && actions > 0) {
            // 消耗一张沙袋卡
            hand.removeIf(card -> card.getType() == CardType.SANDBAG);
            actions--;

            // 修复指定的瓷砖（无论距离）
            tileToShoreUp.setFlooded(false);
            return true;
        }
        return false;
    }


}
