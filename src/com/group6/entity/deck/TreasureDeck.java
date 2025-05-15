// TreasureDeck.java
package com.group6.entity.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.group6.entity.common.Card;
import com.group6.entity.common.CardType;
import com.group6.factory.CardFactory;

public class TreasureDeck extends Deck {

    //构造方法，指定水位上升牌数量（难度相关）
    public TreasureDeck(int waterRiseCount){
        super();
        initialize(waterRiseCount);
    }

    protected void initialize(int waterRiseCount) {
        for (int i = 0; i < 5; i++) {
            deck.add(CardFactory.createCard(CardType.TREASURE, "Lion"));
            deck.add(CardFactory.createCard(CardType.TREASURE, "Statue"));
            deck.add(CardFactory.createCard(CardType.TREASURE, "Crystal"));
            deck.add(CardFactory.createCard(CardType.TREASURE, "Cup"));
        }

        for (int i = 0; i < 3; i++) {
            deck.add(CardFactory.createCard(CardType.HELICOPTER, null));
        }

        for (int i = 0; i < 2; i++) {
            deck.add(CardFactory.createCard(CardType.SANDBAG, null));
        }

        for (int i = 0; i < waterRiseCount; i++) {
            deck.add(CardFactory.createCard(CardType.WATERS_RISE, null));
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
