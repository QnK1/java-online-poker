package pl.edu.agh.kis.lab.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient {
    public void start(final int portNumber, final Scanner scanner) {
        try(var serverChannel = SocketChannel.open()) {
            serverChannel.connect(new InetSocketAddress(portNumber));
            System.out.println("Connected");
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while(true) {
                var line = scanner.nextLine();

                if(line.toLowerCase().contains("quit")) {
                    break;
                }
                line += System.lineSeparator();
                buffer.clear().put(line.getBytes()).flip();

                while(buffer.hasRemaining()) {
                    serverChannel.write(buffer);
                }
                buffer.clear();
                var bytesRead = serverChannel.read(buffer);
                if(bytesRead > 0) {
                    buffer.flip();
                    var data = new String(buffer.array(), buffer.position(), bytesRead);
                    System.out.println("SERVER: " + data);
                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
