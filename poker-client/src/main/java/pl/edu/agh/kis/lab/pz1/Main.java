package pl.edu.agh.kis.lab.pz1;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            NioClient client = new NioClient();
            client.start(12345, scanner);
        }
    }
}