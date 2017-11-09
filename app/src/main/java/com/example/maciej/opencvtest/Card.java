package com.example.maciej.opencvtest;

/**
 * Created by Trollo on 2017-10-31.
 */

public class Card {
    private CardSuit suit;
    private CardValue value;

    @Override
    public String toString() {
        return value.toString() + " of " + suit.toString();
    }

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }
}
