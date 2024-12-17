package pl.edu.agh.kis.lab.pz1;

import pl.edu.agh.kis.lab.pz1.game_logic.Game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Map;

public class NioServer {
    private final Map<String, Game> gameMap;

    public NioServer() {
        gameMap = GameMapFactory.getGameMap();
    }

    public void start(final int portNumber){
        var clients = new HashSet<SocketChannel>();

        try(var serverSocketChannel = ServerSocketChannel.open();
            var selector = Selector.open();){

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(portNumber));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while(true){
                if(selector.select() == 0){
                    continue;
                }

                for(SelectionKey key : selector.selectedKeys()){
                    if(key.isAcceptable()){
                        if(key.channel() instanceof ServerSocketChannel channel){
                            var client = channel.accept();
                            var socket = client.socket();
                            var clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                            System.out.println("CLIENT CONNECTED: " + clientInfo);
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            clients.add(client);

                            sendWelcomeMessage(client);
                        } else {
                            throw new RuntimeException("Unknown channel: " + key.channel());
                        }
                    } else if(key.isReadable()){
                        if(key.channel() instanceof SocketChannel client){
                            var bytesRead = client.read(buffer);
                            if(bytesRead == -1){
                                var socket = client.socket();
                                var clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                                System.out.println("CLIENT DISCONNECTED: " + clientInfo);
                                client.close();
                                clients.remove(client);

                            }
                            buffer.flip();
                            var data = new String(buffer.array(), buffer.position(), bytesRead);
                            System.out.println("CLIENT READ: " + data);
                            while(buffer.hasRemaining()){
                                client.write(buffer);
                            }
                            buffer.clear();


                        } else {
                            throw new RuntimeException("Unknown channel: " + key.channel());
                        }
                    }
                }
                selector.selectedKeys().clear();
            }

        } catch(IOException e){
            throw new RuntimeException(e);
        } finally {
            for(var client : clients){
                try{
                    client.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendWelcomeMessage(SocketChannel client) {
        try(client){
            StringBuilder sb = new StringBuilder();
            sb.append("Welcome to the Poker Server\n");
            sb.append("\n");
            sb.append("Available variants:\n");

            for(String gameName : gameMap.keySet()){
                sb.append("\t").append(gameName).append("\n");
            }

            ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
            buffer.flip();
            while(buffer.hasRemaining()){
                client.write(buffer);
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
