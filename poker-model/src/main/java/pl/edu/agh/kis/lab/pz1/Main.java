package pl.edu.agh.kis.lab.pz1;

import pl.edu.agh.kis.lab.pz1.GameLogic.Deck;
import pl.edu.agh.kis.lab.pz1.GameLogic.ShuffledDeckFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ShuffledDeckFactory df = new ShuffledDeckFactory();
        Deck deck = df.getDeck();

        System.out.println(deck.popCard());
    }
}