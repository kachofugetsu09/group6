package com.group6.factory;

import com.group6.entity.common.Card;
import com.group6.entity.common.CardType;

//卡牌工厂类：用于统一创建各种类型的卡牌
public class CardFactory {

    public static Card createCard(CardType type, String name) {
        switch (type) {
            case TREASURE:
                return new Card(CardType.TREASURE, name, null);
            case FLOOD:
                return new Card(CardType.FLOOD, name, null);
            case HELICOPTER:
                return new Card(CardType.HELICOPTER, "Helicopter", null);
            case SANDBAG:
                return new Card(CardType.SANDBAG, "Sandbag", null);
            case WATERS_RISE:
                return new Card(CardType.WATERS_RISE, "Waters Rise", null);
            default:
                throw new IllegalArgumentException("Unknown: " + type);
        }
    }
}
