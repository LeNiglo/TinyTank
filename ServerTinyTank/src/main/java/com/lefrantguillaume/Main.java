package com.lefrantguillaume;

import com.esotericsoftware.minlog.Log;
import com.lefrantguillaume.master.MasterController;
import com.lefrantguillaume.utils.WindowConfig;
import org.codehaus.jettison.json.JSONException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by Styve on 10/03/2015.
 */

public class Main  {
    private static WindowConfig windowConfig;

    @Option(name = "-c", usage = "console mode", aliases = "--console")
    private boolean console = false;
    @Option(name = "-h", usage = "shows this help", aliases = "--help", help = true)
    private boolean help = false;

    public static void main(String args[]) {
        new Main(args);
    }

    public Main(String args[]) {
        windowConfig = new WindowConfig();
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (help) {
                parser.printUsage(System.out);
                return;
            }
            if (!console) {
                Log.info("Graphical interface");
                new MasterController("GUI");
            } else {
                System.out.println("TinyTank - Console interface. type 'help' to get available commands or 'help <command>' to get help about a specific command.");
                new MasterController("Console");
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());e.printStackTrace();
            System.err.println("java -jar "+ new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName() +" [options]");
            parser.printUsage(System.err);
            System.err.println();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
