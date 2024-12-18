package pl.edu.agh.kis.lab.pz1;

import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * A server class for handling multiple clients using non-blocking I/O (NIO) for a Texas Hold'em game lobby system.
 * It manages client connections, their associated players, and game lobbies.
 */
public class NioServer {
    /** A map of socket channels to TexasHoldemLobby instances, representing clients currently in a lobby. */
    private final Map<SocketChannel, TexasHoldemLobby> clientsInLobby;

    /** A map of lobby IDs to TexasHoldemLobby instances, representing the game lobbies by their unique identifiers. */
    private final Map<String, TexasHoldemLobby> lobbyIds;

    /** A map of socket channels to THPlayer instances, representing the players associated with each client. */
    private final Map<SocketChannel, THPlayer> clientPlayers;

    /**
     * Initializes a new NioServer instance with empty maps for managing clients, lobbies, and players.
     */
    public NioServer() {
        clientsInLobby = new HashMap<>();
        lobbyIds = new HashMap<>();
        clientPlayers = new HashMap<>();
    }

    /**
     * Starts the NIO server, listening on the specified port for client connections.
     * This method sets up necessary channels and manages client communications using selectors.
     *
     * @param portNumber the port number on which the server will listen for incoming connections.
     */
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
                            throw new UnknownChannelException("Unknown channel: " + key.channel());
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
                                        String winData = getString(lobby);
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
                            throw new UnknownChannelException("Unknown channel: " + key.channel());
                        }
                    }
                }
                selector.selectedKeys().clear();
            }

        } catch(Exception e){
            for(var client : clients){
                try{
                    client.close();
                } catch(IOException ex){
                    throw new FailedToCloseConnectionException("Error while closing connection.", ex);
                }
            }

            throw new FailedToStartServerException("Error while establishing server connection.", e);

        }
    }

    /**
     * Retrieves information about the last winner in the given TexasHoldemLobby.
     * The method checks if a new winner has been found and, if so, returns the winner's name,
     * the amount they won, and the cards they used to win.
     *
     * @param lobby The TexasHoldemLobby to retrieve the winner data from.
     * @return A string containing the winner's name, the amount they won, and the cards used to win.
     *         If no winner is found, an empty string is returned.
     */
    private static String getString(TexasHoldemLobby lobby) {
        String winData = "";
        if(lobby.game.isNewWinnerFound()){
            String lastWinner = lobby.game.getLastWinnerNames().get(0);
            int lastWin =  lobby.game.getLastWin();
            List<String> lastCards = lobby.game.getLastWinningCards();

            winData = "\n" + lastWinner + " WON " + lastWin +
                    "\nWITH" + lastCards;
        }
        return winData;
    }

    /**
     * Sends a welcome message to the client containing available commands
     * that can be executed within the lobby.
     *
     * @param client The SocketChannel representing the client that will receive the welcome message.
     * @throws BufferWritingException If there is an error while writing the welcome message to the client.
     */
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
                throw new BufferWritingException("Failed to write to a buffer while creating welcome message", e);
            }
        }

    }
}
