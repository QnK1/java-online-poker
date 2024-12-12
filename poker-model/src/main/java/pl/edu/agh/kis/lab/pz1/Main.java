package pl.edu.agh.kis.lab.pz1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Card card = new Card(Card.Rank.FIVE, Card.Suit.SPADES);

        System.out.println(card);
    }
}