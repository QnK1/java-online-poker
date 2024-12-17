package pl.edu.agh.kis.lab.pz1;


public class Main {
    public static void main(String[] args) {
        NioServer server = new NioServer();
        server.start(12345);
    }
}