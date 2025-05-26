package com.group6.entity.deck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.group6.entity.common.Card;
import lombok.Getter;

@Getter
public abstract class Deck {
    //当前牌堆
    protected List<Card> deck;
    //弃牌堆
    protected List<Card> discardPile;

    public Deck(){
        this.deck = new ArrayList<>();
        this.discardPile = new ArrayList<>();
    }

    //抽取n张牌
    public List<Card> getCards(int n){
        checkAvailability(n);
        List<Card> drawnCards = new ArrayList<>();
        while (drawnCards.size() < n){
            if (!deck.isEmpty()) {
                drawnCards.add(deck.remove(0));
            }
        }
        return drawnCards;
    }

    //弃牌
    public void discard(Card card){
        discardPile.add(card);
    }

    //检查是否需要从弃牌堆补充卡牌
    protected void  checkAvailability(int n){
        if (deck.size() < n) {
            Collections.shuffle(discardPile);
            deck.addAll(discardPile);
            discardPile.clear();
        }
    }

    protected void initialize(){};
}
