package com.lefrantguillaume.ui;

import com.lefrantguillaume.game.GameController;
import com.lefrantguillaume.game.Map;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.utils.GameConfig;
import com.pyratron.frameworks.commands.parser.Argument;
import com.pyratron.frameworks.commands.parser.Command;
import com.pyratron.frameworks.commands.parser.CommandParser;
import com.pyratron.frameworks.commands.parser.ValidationRule;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by leniglo on 06/05/15.
 */
public class UserIO extends Observable implements IInterface {
    private CommandParser parser;
    private GameController parent;

    public UserIO(GameController o) {
        parent = o;
        this.addObserver(o);
        parser = CommandParser.createNew().usePrefix("").onError(message -> onParseError(message));
        parser.addCommand(Command.create("Help").addAlias("help")
                .setDescription("Display the list of available commands")
                .setAction(args -> parser.getCommands().stream().forEachOrdered(command -> System.out.println(command.showHelp()))));

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

        parser.addCommand(Command.create("List players").addAlias("list", "players")
                        .setDescription("Display the list of players on the server")
                        .setAction(args -> onListPlayers(args))
        );
    }

    public void parse(String msg) {
        parser.parse(msg);
    }

    public void addToConsoleLog(String msg) {
        System.out.println("- " + msg);
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
            }
        }
    }

    private void onParseError(String message) {
        System.err.println(message);
    }

    private void askStartGame(ArrayList<Argument> args) {
        System.out.println(Argument.fromName(args, "game_title"));
        System.out.println(Argument.fromName(args, "max_players"));
        System.out.println(Argument.fromName(args, "max_ping"));
        System.out.println(Argument.fromName(args, "tcp_port"));
        System.out.println(Argument.fromName(args, "udp_port"));
        System.out.println(Argument.fromName(args, "friendlyfire [0|1]"));
        System.out.println(Argument.fromName(args, "ally_noblock [0|1]"));
        this.setChanged();
        this.notifyObservers("start game");
    }

    private void askStopGame(ArrayList<Argument> args) {
        this.setChanged();
        this.notifyObservers("stop game");
    }

    private void onListPlayers(ArrayList<Argument> args) {
        if (args.isEmpty()) {

        }
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
