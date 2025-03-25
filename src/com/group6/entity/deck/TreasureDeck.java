// TreasureDeck.java
package com.group6.entity.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.group6.entity.common.Card;
import com.group6.entity.common.CardType;

public class TreasureDeck extends Deck {

    //构造方法，指定水位上升牌数量（难度相关）
    public TreasureDeck(int waterRiseCount){
        super();
        initialize(waterRiseCount);
    }

    protected void initialize(int waterRiseCount) {
        for (int i = 0; i < 5; i++) {
            deck.add(new Card(CardType.TREASURE, "Lion", null));
            deck.add(new Card(CardType.TREASURE, "Statue", null));
            deck.add(new Card(CardType.TREASURE, "Crystal", null));
            deck.add(new Card(CardType.TREASURE, "Cup", null));
        }

        for (int i = 0; i < 3; i++) {
            deck.add(new Card(CardType.HELICOPTER, "Helicopter", null));
        }

        for (int i = 0; i < 2; i++) {
            deck.add(new Card(CardType.SANDBAG, "Sandbag", null));
        }

        for (int i = 0; i < waterRiseCount; i++) {
            deck.add(new Card(CardType.WATERS_RISE, "Waters Rise", null));
        }

        Collections.shuffle(deck);
    }

    //初始发牌，不包含“水位上升”牌，避免死循环
    public List<Card> getNoRiseCards(int n){
        List<Card> noRiseCards = new ArrayList<>();

        while(noRiseCards.size() < n){
            Card card = deck.remove(0);
            if(card.getType() == CardType.WATERS_RISE){
                discard(card);
            } else {
                noRiseCards.add(card);
            }
        }

        deck.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(deck);

        return noRiseCards;
    }
}
