package com.group6.entity.deck;

import com.group6.entity.common.Card;
import com.group6.factory.CardFactory;
import com.group6.entity.common.CardType;


import java.util.Collections;
import java.util.List;

public class FloodDeck extends Deck {

    // 构造方法
    public FloodDeck() {
        super();
        initialize();
    }

    //读档的构造方法
    public FloodDeck(List<Card> deck, List<Card> discard) {
        super();
        this.deck = deck;
        this.discardPile = discard;
    }


    // 初始化24张洪水牌
    @Override
    protected void initialize() {
        String[] tileNames = {
                "Temple_of_the_Moon", "Temple_of_the_Sun",
                "Coral_Palace", "Tidal_Palace",
                "Cave_of_Embers", "Cave_of_Shadows",
                "Whispering_Garden", "Howling_Garden",
                "Bronze_Gate", "Silver_Gate",
                "Gold_Gate", "Iron_Gate",
                "Fools'_Landing", "Observatory",
                "Crimson_Forest", "Lost_Lagoon",
                "Dunes_of_Deception", "Phantom_Rock",
                "Breakers_Bridge", "Cliffs_of_Abandon",
                "Misty_Marsh", "Watchtower",
                "Twilight_Hollow", "Copper_Gate"
        };

        for (String name : tileNames) {
            deck.add(CardFactory.createCard(CardType.FLOOD, name));
        }

        Collections.shuffle(deck); // 初始洗牌
    }

    // 重写 getNCards（推荐使用父类 getCards，保留向下兼容）
    public java.util.List<Card> getNCards(int n) {
        return getCards(n); // 调用父类方法
    }

    // 水位上升时调用，将弃牌堆洗牌后放回牌堆顶部
    public void putBack2Top() {
        Collections.shuffle(discardPile);
        deck.addAll(0, discardPile);
        discardPile.clear();
    }

    // 可选：调试或 UI 展示使用
    public int getDeckSize() {
        return deck.size();
    }

    public int getDiscardPileSize() {
        return discardPile.size();
    }
}
