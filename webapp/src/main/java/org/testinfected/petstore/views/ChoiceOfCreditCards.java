package org.testinfected.petstore.views;

import org.testinfected.petstore.billing.CreditCardType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChoiceOfCreditCards implements Iterable<ChoiceOfCreditCards.Option> {

    private final Map<String, Option> options = new HashMap<String, Option>();

    public static ChoiceOfCreditCards from(CreditCardType[] cardTypes) {
        ChoiceOfCreditCards choice = new ChoiceOfCreditCards();
        for (CreditCardType cardType : cardTypes) {
            choice.add(cardType);
        }
        return choice;
    }

    public static ChoiceOfCreditCards from(CreditCardType[] cardTypes, CreditCardType selected) {
        ChoiceOfCreditCards choice = from(cardTypes);
        choice.select(selected);
        return choice;
    }

    private void add(CreditCardType cardType) {
        options.put(cardType.name(), new Option(cardType));
    }

    public void select(CreditCardType cardType) {
        options.get(cardType.name()).select();
    }

    public Iterator<Option> iterator() {
        return options.values().iterator();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ChoiceOfCreditCards)) return false;

        ChoiceOfCreditCards choiceOfCreditCards = (ChoiceOfCreditCards) other;
        return options.equals(choiceOfCreditCards.options);
    }

    public String toString() {
        return options.toString();
    }

    public static class Option {

        private final CreditCardType card;
        private boolean selected;

        public Option(CreditCardType card) {
            this.card = card;
        }

        public void select() {
            this.selected = true;
        }

        public String name() {
            return card.commonName();
        }

        public String type() {
            return card.name();
        }

        public String selected() {
            return selected ? " selected=\"selected\"" : "";
        }

        public boolean equals(Object other) {
            if (!(other instanceof Option)) return false;

            Option option = (Option) other;
            return card.equals(option.card);
        }

        public String toString() {
            return name() + (selected ? "(selected)" : "");
        }
    }
}



