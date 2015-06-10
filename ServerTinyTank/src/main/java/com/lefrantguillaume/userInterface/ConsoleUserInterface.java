package com.lefrantguillaume.userInterface;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.master.EnumTargetTask;
import com.lefrantguillaume.master.MasterController;
import com.lefrantguillaume.gameComponent.maps.Map;
import com.lefrantguillaume.gameComponent.gameobjects.player.Player;
import com.lefrantguillaume.utils.GameConfig;
import com.lefrantguillaume.utils.ServerConfig;
import com.pyratron.frameworks.commands.parser.Argument;
import com.pyratron.frameworks.commands.parser.Command;
import com.pyratron.frameworks.commands.parser.CommandParser;
import com.pyratron.frameworks.commands.parser.ValidationRule;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by leniglo on 06/05/15.
 */
public class ConsoleUserInterface extends Observable implements UserInterface {
    private CommandParser parser;
    private MasterController parent;
    private boolean console;

    public ConsoleUserInterface(MasterController o) {
        parent = o;
        this.addObserver(o);
        parser = CommandParser.createNew().usePrefix("").onError(message -> onParseError(message));
        parser.addCommand(Command.create("Help").addAlias("help", "commands")
                .setDescription("Display the list of available commands")
                .setAction(args -> askHelp(args))
                .addArgument(Argument.create("command")
                        .makeOptional()
                        .setDefault("all")));

        parser.addCommand(Command.create("Start Game").addAlias("start")
                        .setDescription("Start the gameComponent with specified options")
                        .setExtendedDescription("<game_title> : title of the gameComponent. Spaces are forbidden\n" +
                                        "<max_players> : max number of players your server can handle\n" +
                                        "<max_ping> : max ping tolerated in ms\n" +
                                        "<tcp_port> : TCP port that will be used for communication with clients\n" +
                                        "<udp_port> : UDP port that will be used for communication with clients\n" +
                                        "<friendlyfire> : can you shoot your allies ? yes 'ff' else 'noff'\n" +
                                        "<ally_noblock> : can you go through your allies ? yes 'noblock' else 'block'"
                        )
                        .setAction(argues -> askStartGame(argues))
                        .addArgument(Argument.create("game_title")
                                .makeOptional().setDefault("My TinyTank Game")
                                .setValidator(ValidationRule.AlphaNumerical))
                        .addArgument(Argument.create("max_players")
                                .makeOptional().setDefault("8")
                                .setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("max_ping")
                                .makeOptional().setDefault("100")
                                .setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("tcp_port")
                                .makeOptional().setDefault("13333")
                                .setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("udp_port")
                                .makeOptional().setDefault("13444")
                                .setValidator(ValidationRule.Integer))
                        .addArgument(Argument.create("friendlyfire")
                                .makeOptional().setDefault("noff")
                                .addOption(Argument.create("noff"))
                                .addOption(Argument.create("ff")))
                        .addArgument(Argument.create("ally_noblock")
                                .makeOptional().setDefault("block")
                                .addOption(Argument.create("noblock"))
                                .addOption(Argument.create("block")))
        );

        parser.addCommand(Command.create("Stop gameComponent").addAlias("stop")
                        .setDescription("Stop the gameComponent")
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

        parser.addCommand(Command.create("Exit program").addAlias("exit", "quit")
                        .setDescription("Exit the program")
                        .setAction(args -> System.exit(0))
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
            try {
                String input = scanner.nextLine();
                this.parse(input);
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IllegalStateException e) {
                addToConsoleLog(e.getMessage() + " Try 'help' to show more informations.");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                WindowController.addConsoleMsg("\nInterrupted. use 'exit' next time !\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void addToConsoleErr(String msg) {
        System.err.println(msg);
    }

    private void onParseError(String message) {
        WindowController.addConsoleErr(message);
    }

    private void askHelp(ArrayList<Argument> args) {
        if (Argument.fromName(args, "command").equals("all")) {
            parser.getCommands().stream().forEachOrdered(command -> WindowController.addConsoleMsg(command.generateUsage()));
        } else {
            for (Command command : parser.getCommands()) {
                if (command.getAliases().contains((Argument.fromName(args, "command")))) {
                    WindowController.addConsoleMsg(command.generateExtendedUsage());
                }
            }
        }
    }

    private void askStartGame(ArrayList<Argument> args) {
        ServerConfig.gameName = (Argument.fromName(args, "game_title"));
        ServerConfig.maxAllowedPlayers = Integer.valueOf(Argument.fromName(args, "max_players"));
        ServerConfig.maxAllowedPing = Integer.valueOf(Argument.fromName(args, "max_ping"));
        ServerConfig.tcpPort = Integer.valueOf(Argument.fromName(args, "tcp_port"));
        ServerConfig.udpPort = Integer.valueOf(Argument.fromName(args, "udp_port"));
        ServerConfig.friendlyFire = (Argument.fromName(args, "friendlyfire").equals("ff"));
        ServerConfig.allyNoBlock = (Argument.fromName(args, "ally_noblock").equals("noblock"));
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_CONTROLLER, "start game"));
    }

    private void askStopGame(ArrayList<Argument> args) {
        this.setChanged();
        this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_CONTROLLER, "stop game"));
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
        List<Map> maps = parent.getMaps();
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
        this.notifyObservers(new Pair<>(EnumTargetTask.MASTER_CONTROLLER, "reload maps"));
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

    public void startGame() {
    }

    public void stopGame() {
    }

    public void refreshPlayers() {
    }

    public void refreshMaps() {
    }
}
