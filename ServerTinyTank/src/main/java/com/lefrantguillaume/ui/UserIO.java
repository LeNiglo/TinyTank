package com.lefrantguillaume.ui;

import com.lefrantguillaume.utils.GameConfig;
import com.pyratron.frameworks.commands.parser.Command;
import com.pyratron.frameworks.commands.parser.CommandParser;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by leniglo on 06/05/15.
 */
public class UserIO extends Observable implements IInterface {
    private CommandParser parser;

    public UserIO(Observer o) {
        this.addObserver(o);
        parser = CommandParser.createNew().usePrefix("").onError(message -> onParseError(message));
        parser.addCommand(Command.create("help").addAlias("commands")
                .setDescription("Display the list of available commands")
                .setAction(args -> parser.getCommands().stream().forEachOrdered(command -> System.out.println(command.showHelp()))));
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
            this.parse(input);
        }
    }

    private void onParseError(String message) {
        System.err.println(message);
    }


    public void clearMapList() {}
    public void addMap(String mapName) {}
    public void tellNoMap() {}
    public int getSelectedMapIndex() { return 0; }
    public GameConfig getGameConfig() { return null; }
    public void gameStarted() {}
    public void gameStopped() {}
    public void addPlayer(String pseudo) {}
    public void delPlayer(String pseudo) {}
    public void clearPlayerList() {}
}
