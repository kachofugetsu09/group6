package com.group6.factory;

import com.group6.entity.deck.FloodDeck;
import com.group6.entity.deck.TreasureDeck;

public class DeckFactory {
    public static TreasureDeck createTreasureDeck(int waterRiseCount) {
        return new TreasureDeck(waterRiseCount);
    }

    public static FloodDeck createFloodDeck() {
        return new FloodDeck();
    }
}
