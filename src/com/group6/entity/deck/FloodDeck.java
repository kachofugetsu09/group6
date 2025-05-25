package com.group6.entity.deck;

import com.group6.entity.common.Card;
import com.group6.factory.CardFactory;
import com.group6.entity.common.CardType;

import java.util.Collections;

public class FloodDeck extends Deck {

    // 构造方法
    public FloodDeck() {
        super();
        initialize();
    }

    // 初始化24张洪水牌
    @Override
    protected void initialize() {
        String[] tileNames = {
                "Temple of the Moon", "Temple of the Sun",
                "Coral Palace", "Tidal Palace",
                "Cave of Embers", "Cave of Shadows",
                "Whispering Garden", "Howling Garden",
                "Bronze Gate", "Silver Gate",
                "Gold Gate", "Iron Gate",
                "Fools' Landing", "Observatory",
                "Crimson Forest", "Lost Lagoon",
                "Dunes of Deception", "Phantom Rock",
                "Breakers Bridge", "Cliffs of Abandon",
                "Misty Marsh", "Watchtower",
                "Twilight Hollow", "Flooded Ruins"
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
