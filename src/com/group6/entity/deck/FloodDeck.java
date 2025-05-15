// FloodDeck.java
package com.group6.entity.deck;

import com.group6.entity.common.Card;
import com.group6.factory.CardFactory;
import com.group6.entity.common.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FloodDeck extends Deck {

    // 构造方法
    public FloodDeck() {
        super();
        initialize();
    }

    // 初始化24张洪水牌
    @Override
    protected void initialize() {
        // TODO: 之后替换为 TileName 枚举或 Board 提供的 tile 列表
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
        Collections.shuffle(deck);
    }

    // 抽取 n 张洪水牌
    public List<Card> getNCards(int n) {
        checkAvailability(n);
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            drawnCards.add(deck.remove(0));
        }
        return drawnCards;
    }

    // 弃牌操作
    @Override
    public void discard(Card card) {
        super.discard(card);
    }

    // 水位上升时调用，将弃牌堆洗牌并放回牌堆顶部
    public void putBack2Top() {
        Collections.shuffle(discardPile);
        deck.addAll(0, discardPile);
        discardPile.clear();
    }
}
