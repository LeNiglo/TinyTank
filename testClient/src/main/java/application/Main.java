package application;

/**
 * Created by Styve on 17/03/2015.
 */
public class Main {

    public static void main(String args[]) {
        if (args[0] != null)
            new ClientSide(args[0]);
    }
}
