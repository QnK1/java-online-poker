package pl.edu.agh.kis.lab.pz1;

import pl.edu.agh.kis.lab.pz1.game_logic.Game;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;


public class NioServer {
    private final Map<SocketChannel, TexasHoldemLobby> clientsInLobby;
    private final Map<String, TexasHoldemLobby> lobbyIds;
    private final Map<SocketChannel, THPlayer> clientPlayers;

    public NioServer() {
        clientsInLobby = new HashMap<>();
        lobbyIds = new HashMap<>();
        clientPlayers = new HashMap<>();
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
                        } else {
                            System.out.println("HERE 1");
                            throw new RuntimeException("Unknown channel: " + key.channel());
                        }
                    } else if(key.isReadable()){
                        if(key.channel() instanceof SocketChannel client){
                            buffer = ByteBuffer.allocate(1024);
                            var bytesRead = client.read(buffer);
                            if(bytesRead == -1){
                                var socket = client.socket();
                                var clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                                System.out.println("CLIENT DISCONNECTED: " + clientInfo);
                                client.close();
                                clients.remove(client);

                            }else{
                                buffer.flip();
                                var data = new String(buffer.array(), buffer.position(), bytesRead);
                                var args = Arrays.asList(data.strip().split(" "));
                                buffer.clear();

                                if(!clientsInLobby.containsKey(client)){
                                    if(args.size() == 3
                                            && args.get(0).equals("create")
                                            && !args.get(1).isEmpty() && !lobbyIds.containsKey(args.get(1)) &&
                                            !args.get(2).isEmpty()){
                                        var lobby = new TexasHoldemLobby();

                                        lobbyIds.put(args.get(1), lobby);
                                        clientsInLobby.put(client, lobby);
                                        THPlayer player = new THPlayer();
                                        player.setName(args.get(2));
                                        clientPlayers.put(client, player);
                                        lobby.addPlayer(player);

                                        buffer.put(("lobby " + args.get(1) + " created").getBytes());
                                        buffer.flip();
                                        while(buffer.hasRemaining()){
                                            client.write(buffer);
                                        }
                                        buffer.clear();
                                    } else if(
                                            args.size() == 3 && args.get(0).equals("join")
                                                    && !args.get(1).isEmpty() && lobbyIds.containsKey(args.get(1)) &&
                                                    !args.get(2).isEmpty()){
                                        var lobby = lobbyIds.get(args.get(1));
                                        clientsInLobby.put(client, lobby);
                                        THPlayer player = new THPlayer();
                                        player.setName(args.get(2));
                                        clientPlayers.put(client, player);
                                        lobby.addPlayer(player);

                                        buffer.put(("game " + args.get(1) + " joined").getBytes());
                                        buffer.flip();
                                        while(buffer.hasRemaining()){
                                            client.write(buffer);
                                        }
                                        buffer.clear();

                                    }
                                } else {
                                    var lobby = clientsInLobby.get(client);
                                    var player = clientPlayers.get(client);
                                    if(lobby.isStarted()){
                                        String message;

                                        boolean commandSuccess = lobby.game.executeCommand(player, data);

                                        if(commandSuccess && data.strip().split(" ")[0].equals("LEAVE")
                                                || lobby.game.isGameOver()){
                                            clientPlayers.remove(client);
                                            clientsInLobby.remove(client);

                                        }

                                        if(commandSuccess){
                                            message = "\nCOMMAND SUCCESSFUL";
                                        }else{
                                            message = "\nENTER VALID COMMAND";
                                        }
                                        String winData = "";
                                        if(lobby.game.isNewWinnerFound()){
                                            String lastWinner = lobby.game.getLastWinnerNames().get(0);
                                            int lastWin =  lobby.game.getLastWin();
                                            List<String> lastCards = lobby.game.getLastWinningCards();

                                            winData = "\n" + lastWinner + " WON " + lastWin +
                                                    "\nWITH" + lastCards;
                                        }
                                        String gameData = lobby.getGame().getGameState(player.getName()) + message +
                                                winData;

                                        buffer.put(gameData.getBytes());
                                        buffer.flip();
                                        while(buffer.hasRemaining()){
                                            client.write(buffer);
                                        }
                                    } else {
                                        if(args.size() == 1 && args.get(0).equals("start") &&
                                                lobby.getPlayers().size() >= 2){
                                            lobby.createGame();
                                            buffer.put("GAME STARTED".getBytes());
                                            buffer.flip();
                                            while(buffer.hasRemaining()){
                                                client.write(buffer);
                                            }
                                            buffer.clear();
                                        } else {
                                            buffer.put("TYPE START TO START".getBytes());
                                            buffer.flip();
                                            while(buffer.hasRemaining()){
                                                client.write(buffer);
                                            }
                                            buffer.clear();
                                        }
                                    }
                                }

                                if(!clientsInLobby.containsKey(client)){
                                    sendWelcomeMessage(client);
                                }
                            }
                            buffer.clear();


                        } else {
                            System.out.println("HERE 2");
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

    private void sendWelcomeMessage(SocketChannel client){
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");

        sb.append("\t").append("create").append("\n");
        sb.append("\t").append("join").append("\n");


        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sb.toString().getBytes());
        buffer.flip();
        while(buffer.hasRemaining()){
            try {
                client.write(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

//    private void sendWelcomeMessage(SocketChannel client) {
//        try(client){
//            StringBuilder sb = new StringBuilder();
//            sb.append("Welcome to the Poker Server\n");
//            sb.append("\n");
//            sb.append("Available variants:\n");
//
//            for(String gameName : gameMap.keySet()){
//                sb.append("\t").append(gameName).append("\n");
//            }
//
//            ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
//            buffer.flip();
//            while(buffer.hasRemaining()){
//                client.write(buffer);
//            }
//        } catch(IOException e){
//            throw new RuntimeException(e);
//        }
//    }
}
