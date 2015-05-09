package com.lefrantguillaume.ui;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.game.GameController;
import com.lefrantguillaume.game.Map;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.utils.GameConfig;
import com.lefrantguillaume.utils.ServerConfig;
import com.pyratron.frameworks.commands.parser.Argument;
import com.pyratron.frameworks.commands.parser.Command;
import com.pyratron.frameworks.commands.parser.CommandParser;
import com.pyratron.frameworks.commands.parser.ValidationRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by leniglo on 06/05/15.
 */
public class UserIO extends Observable implements IInterface {
    private CommandParser parser;
    private GameController parent;
    private boolean console;

    public UserIO(GameController o) {
        parent = o;
        this.addObserver(o);
        parser = CommandParser.createNew().usePrefix("").onError(message -> onParseError(message));
        parser.addCommand(Command.create("Help").addAlias("help", "commands")
                .setDescription("Display the list of available commands")
                .setAction(args -> parser.getCommands().stream().forEachOrdered(command -> WindowController.addConsoleMsg(command.generateUsage()))));

        parser.addCommand(Command.create("Start Game").addAlias("start")
                        .setDescription("Start the game with specified options")
                        .setAction(argues -> askStartGame(argues))
                        .addArgument(Argument.create("game_title").setValidator(ValidationRule.AlphaNumerical))
                        .addArgument(Argument.create("max_players").setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("max_ping").setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("tcp_port").setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("udp_port").setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("friendlyfire [0|1]").setValidator(ValidationRule.Binary))
                        .addArgument(Argument.create("ally_noblock [0|1]").setValidator(ValidationRule.Binary))
        );

        parser.addCommand(Command.create("Stop game").addAlias("stop")
                        .setDescription("Stop the game")
                        .setAction(args -> askStopGame(args))
        );

        parser.addCommand(Command.create("List players").addAlias("players", "player", "p")
                        .setDescription("Display the list of players on the server")
                        .setAction(args -> onListPlayers(args))
        );

        parser.addCommand(Command.create("List maps").addAlias("maps", "map", "m")
                        .setDescription("Display the list of maps on the server")
                        .setAction(args -> onListMaps(args))
        );

        parser.addCommand(Command.create("Reload maps").addAlias("reload", "reload_maps")
                        .setDescription("Reloads your maps folder")
                        .setAction(args -> onReloadMaps(args))
        );
    }

    public void parse(String msg) {
        parser.parse(msg);
    }

    public void addToConsoleLog(String msg) {
        System.out.println(msg);
    }

    public void fromConsole() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            try {
                this.parse(input);
            } catch (IllegalStateException e) {
                addToConsoleLog(e.getMessage() + " Try 'help' to show more informations.");
                try {
                    Thread.sleep(10);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void addToConsoleErr(String msg) {
        System.err.println(msg);
    }

    private void onParseError(String message) {
        WindowController.addConsoleErr(message);
    }

    private void askStartGame(ArrayList<Argument> args) {
        ServerConfig.gameName = (Argument.fromName(args, "game_title"));
        ServerConfig.maxAllowedPlayers = Integer.valueOf(Argument.fromName(args, "max_players"));
        ServerConfig.maxAllowedPing = Integer.valueOf(Argument.fromName(args, "max_ping"));
        ServerConfig.tcpPort = Integer.valueOf(Argument.fromName(args, "tcp_port"));
        ServerConfig.udpPort = Integer.valueOf(Argument.fromName(args, "udp_port"));
        ServerConfig.friendlyFire = (Argument.fromName(args, "friendlyfire [0|1]").equals("1"));
        ServerConfig.allyNoBlock = (Argument.fromName(args, "ally_noblock [0|1]").equals("1"));
        this.setChanged();
        this.notifyObservers("start game");
    }

    private void askStopGame(ArrayList<Argument> args) {
        this.setChanged();
        this.notifyObservers("stop game");
    }

    private void onListPlayers(ArrayList<Argument> args) {
        WindowController.addConsoleMsg("+---------------------+---------+---------+");
        WindowController.addConsoleMsg("| Pseudo              | Kills   | Deaths  |");
        WindowController.addConsoleMsg("+---------------------+---------+---------+");
        if (args.isEmpty()) {
            HashMap<String, Player> players = parent.getPlayers();
            for (java.util.Map.Entry<String, Player> p : players.entrySet()) {
                String buff = "";
                buff += ("| " + p.getValue().getPseudo());
                for (int i = p.getValue().getPseudo().length(); i < 19; ++i) {
                    System.out.print(" ");
                }
                buff += (" | " + p.getValue().getKills());
                for (int i = String.valueOf(p.getValue().getKills()).length(); i < 7; ++i) {
                    System.out.print(" ");
                }
                buff += (" | " + p.getValue().getKills());
                for (int i = String.valueOf(p.getValue().getDeaths()).length(); i < 7; ++i) {
                    System.out.print(" ");
                }
                buff += (" |");
                WindowController.addConsoleMsg(buff);
            }
        }
        WindowController.addConsoleMsg("+---------------------+---------+---------+");
    }

    private void onListMaps(ArrayList<Argument> args) {
        ArrayList<Map> maps = parent.getMaps();
        int maxNameLength = 8;
        int maxIdLength = String.valueOf(maps.size()).length();
        int j = 0;
        maxIdLength = maxIdLength == 1 ? 2 : maxIdLength;
        for (Map map : maps) {
            maxNameLength = map.getName().length() > maxNameLength ? map.getName().length() : maxNameLength;
        }
        String buff = "";
        for (int i = 0; i < maxIdLength + 10 + maxNameLength; ++i)
            buff += ("=");
        WindowController.addConsoleMsg(buff);
        buff = "";
        buff += ("== ID == MAP NAME");
        for (int i = 8; i < maxNameLength; ++i)
            buff += (" ");
        buff += (" ==");
        WindowController.addConsoleMsg(buff);
        buff = "";
        for (int i = 0; i < maxIdLength + 10 + maxNameLength; ++i)
            buff += ("=");
        WindowController.addConsoleMsg(buff);
        for (Map map : maps) {
            buff = "";
            buff += ("== " + j);
            for (int i = String.valueOf(j).length(); i < maxIdLength; ++i)
                buff += (" ");
            buff += (" == " + map.getName());
            for (int i = map.getName().length(); i < maxNameLength; ++i)
                buff += (" ");
            buff += (" ==");
            WindowController.addConsoleMsg(buff);
            ++j;
        }
        buff = "";
        for (int i = 0; i < maxIdLength + 10 + maxNameLength; ++i)
            buff += ("=");
        WindowController.addConsoleMsg(buff);
    }

    private void onReloadMaps(ArrayList<Argument> args) {
        this.setChanged();
        this.notifyObservers("reload maps");
    }

    public void tellNoMap() {
        addToConsoleLog("Please check that your maps folder exists and is not empty.\n" +
                "For more information, read the docs @ http://blablabl.com\n\n" +
                "Once you've added a map, please type 'reload_maps'.");
    }

    public int getSelectedMapIndex() {
        return 0;
    }

    public GameConfig getGameConfig() {
        return null;
    }

    public void gameStarted() {
    }

    public void gameStopped() {
    }

    public void refreshPlayers() {
    }

    public void refreshMaps() {
    }
}
