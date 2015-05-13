package com.lefrantguillaume.interfaces;

import com.lefrantguillaume.game.GameController;
import com.lefrantguillaume.game.Map;
import com.lefrantguillaume.game.enums.eGameMode;
import com.lefrantguillaume.game.gameobjects.player.Player;
import com.lefrantguillaume.utils.GameConfig;
import com.lefrantguillaume.utils.ServerConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 12/03/2015.
 */
public class GraphicalInterface extends JFrame implements Interface {
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private ScheduledFuture<?> t = null;
    private DefaultTableModel model = new DefaultTableModel();
    private GUITalker talker = null;
    private GameController parent;
    private ConsoleInterface parser;

    public GraphicalInterface(GameController o) {
        parent = o;
        parser = new ConsoleInterface(parent);
        talker = new GUITalker(o);
        initComponents();
        init();
        setVisible(true);
    }

    private class GUITalker extends Observable implements Interface {
        public GUITalker(Observer o) {
            this.addObserver(o);
        }

        public void askStartGame() {
            this.setChanged();
            this.notifyObservers("start game");
        }

        public void askStopGame() {
            this.setChanged();
            this.notifyObservers("stop game");
        }

        public void reloadMaps() {
            this.setChanged();
            this.notifyObservers("reload maps");
        }

        public void tellNoMap() {}
        public void addToConsoleLog(String msg) {}
        public void addToConsoleErr(String msg) {}
        public int getSelectedMapIndex() { return 0; }
        public void gameStarted() {}
        public void gameStopped() {}
        public void refreshPlayers() {}
        public void refreshMaps() {}
    }

    public void init() {
        if (!ServerConfig.loadConfig())
            ServerConfig.writeConfig();
        model.addColumn("Player");
        model.addColumn("Kills");
        model.addColumn("Deaths");
        table_players.setModel(model);
        table_players.getColumn("Player").setMinWidth(150);
        button_stop.setEnabled(false);
        setContentPane(rootPanel);
        setBounds(200, 200, 1000, 580);
        setMaximumSize(new Dimension(1000, 900));
        setMinimumSize(new Dimension(1000, 580));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        field_name.setText(com.lefrantguillaume.utils.ServerConfig.gameName);
        field_max_ping.setText(String.valueOf(com.lefrantguillaume.utils.ServerConfig.maxAllowedPing));
        field_max_players.setText(String.valueOf(com.lefrantguillaume.utils.ServerConfig.maxAllowedPlayers));
        field_tcp.setText(String.valueOf(com.lefrantguillaume.utils.ServerConfig.tcpPort));
        field_udp.setText(String.valueOf(com.lefrantguillaume.utils.ServerConfig.udpPort));
        check_ff.setSelected(com.lefrantguillaume.utils.ServerConfig.friendlyFire);
        check_noblock.setSelected(com.lefrantguillaume.utils.ServerConfig.allyNoBlock);
        field_timelimit.setText("600");
        field_pts.setText("30");
    }

    public void addToConsoleLog(String msg) {
        text_console.append(msg + "\n");
    }

    public void addToConsoleErr(String msg) {
        text_console.append("ERR: " + msg + "\n");
    }

    public void tellNoMap() {
        JOptionPane.showMessageDialog(null, "Please check that your maps folder exists and is not empty.\n" +
                "For more information, read the docs @ http://blablabl.com\n\n" +
                "Once you've added a map, press the Refresh button.", "No map found", JOptionPane.WARNING_MESSAGE);
    }

    public int getSelectedMapIndex() {
        return combo_map.getSelectedIndex();
    }

    public GameConfig getGameConfig() {
        return new GameConfig(Integer.valueOf(field_pts.getText()),
                Integer.valueOf(field_timelimit.getText()),
                (combo_mode.getName() != null ? eGameMode.FFA : eGameMode.FFA));
    }

    private void button_startMouseClicked(MouseEvent e) {
        talker.askStartGame();
    }

    private void button_saveMouseClicked(MouseEvent e) {
        com.lefrantguillaume.utils.ServerConfig.udpPort = Integer.valueOf(field_udp.getText());
        com.lefrantguillaume.utils.ServerConfig.tcpPort = Integer.valueOf(field_tcp.getText());
        com.lefrantguillaume.utils.ServerConfig.maxAllowedPing = Integer.valueOf(field_max_ping.getText());
        com.lefrantguillaume.utils.ServerConfig.maxAllowedPlayers = Integer.valueOf(field_max_players.getText());
        com.lefrantguillaume.utils.ServerConfig.gameName = field_name.getText();
        com.lefrantguillaume.utils.ServerConfig.friendlyFire = check_ff.isSelected();
        com.lefrantguillaume.utils.ServerConfig.allyNoBlock = check_noblock.isSelected();
    }

    private void button_stopMouseClicked(MouseEvent e) {
        if (button_stop.isEnabled()) {
            talker.askStopGame();
        }
    }

    private void button_refreshMouseClicked(MouseEvent e) {
        talker.reloadMaps();
    }

    public void gameStarted() {
        button_start.setEnabled(true);
        button_stop.setEnabled(true);
        if (button_start.getText().equals("Start")) {
            button_start.setText("Restart");
        }
        new Thread("uptime") {
            public void run() {
                final long uptime1 = ManagementFactory.getRuntimeMXBean().getUptime();
                if (t != null) {
                    t.cancel(true);
                }
                t = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() - uptime1;
                        String d = uptime / (3600 * 1000 * 24) + ":" + dateFormat.format(uptime);
                        label_uptime.setText(d);
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
        }.start();
    }

    public void gameStopped() {
        if (t != null)
            t.cancel(true);
        button_stop.setEnabled(false);
        button_start.setText("Start");
    }

    public void refreshPlayers() {
        model.setRowCount(0);
        HashMap<String, Player> players = parent.getPlayers();
        for (java.util.Map.Entry<String, Player> p : players.entrySet()) {
            Object[] list = new Object[]{p.getValue().getPseudo(), p.getValue().getKills(), p.getValue().getDeaths()};
            model.addRow(list);
        }
    }

    public void refreshMaps() {
        combo_map.removeAllItems();
        ArrayList<Map> maps = parent.getMaps();
        for (Map m : maps) {
            combo_map.addItem(m.getName());
        }
    }

    private void text_inputKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            text_console.append(">> " + text_input.getText() + "\n");
            parser.parse(text_input.getText());
            text_input.setText("");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Styve SIMONNEAU
        rootPanel = new JPanel();
        panel1 = new JPanel();
        label_name = new JLabel();
        label_max_players = new JLabel();
        label_max_ping = new JLabel();
        label_tcp = new JLabel();
        label_udp = new JLabel();
        field_name = new JTextField();
        field_max_players = new JTextField();
        field_max_ping = new JTextField();
        field_udp = new JTextField();
        field_tcp = new JTextField();
        check_ff = new JCheckBox();
        check_noblock = new JCheckBox();
        label18 = new JLabel();
        button_save = new JButton();
        scroll_console = new JScrollPane();
        text_console = new JTextArea();
        text_input = new JTextField();
        label_server = new JLabel();
        separator1 = new JSeparator();
        separator2 = new JSeparator();
        panel2 = new JPanel();
        label_map = new JLabel();
        label_mode = new JLabel();
        label_pts = new JLabel();
        label_timelimit = new JLabel();
        combo_map = new JComboBox();
        combo_mode = new JComboBox();
        field_pts = new JTextField();
        field_timelimit = new JTextField();
        button_start = new JButton();
        button_stop = new JButton();
        button_froce_change = new JButton();
        button1 = new JButton();
        label_server2 = new JLabel();
        panel3 = new JPanel();
        scroll_players = new JScrollPane();
        table_players = new JTable();
        label_uptime = new JLabel();
        label1 = new JLabel();
        label_server3 = new JLabel();
        hSpacer1 = new JPanel(null);
        hSpacer2 = new JPanel(null);
        hSpacer3 = new JPanel(null);
        hSpacer4 = new JPanel(null);
        hSpacer5 = new JPanel(null);
        hSpacer6 = new JPanel(null);

        //======== rootPanel ========
        {
            rootPanel.setMaximumSize(new Dimension(940, 580));
            rootPanel.setMinimumSize(new Dimension(1920, 900));
            rootPanel.setPreferredSize(new Dimension(940, 880));


            //======== panel1 ========
            {

                //---- label_name ----
                label_name.setText("game name");
                label_name.setLabelFor(field_name);

                //---- label_max_players ----
                label_max_players.setText("Max players");
                label_max_players.setLabelFor(field_max_players);

                //---- label_max_ping ----
                label_max_ping.setText("Max ping");
                label_max_ping.setLabelFor(field_max_ping);

                //---- label_tcp ----
                label_tcp.setText("TCP port");
                label_tcp.setLabelFor(field_tcp);

                //---- label_udp ----
                label_udp.setText("UDP port");
                label_udp.setLabelFor(field_udp);

                //---- check_ff ----
                check_ff.setText("Friendly fire");

                //---- check_noblock ----
                check_noblock.setText("Ally noblock");

                //---- label18 ----
                label18.setText("ms");

                //---- button_save ----
                button_save.setText("Save all changes");
                button_save.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_saveMouseClicked(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel1Layout.createParallelGroup()
                                                .addGroup(panel1Layout.createSequentialGroup()
                                                        .addGroup(panel1Layout.createParallelGroup()
                                                                .addGroup(panel1Layout.createSequentialGroup()
                                                                        .addComponent(label_name, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(field_name, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(panel1Layout.createSequentialGroup()
                                                                        .addGroup(panel1Layout.createParallelGroup()
                                                                                .addComponent(label_tcp, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(label_udp, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(panel1Layout.createParallelGroup()
                                                                                .addComponent(field_tcp, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(field_udp, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                                                                        .addGap(12, 12, 12)
                                                                        .addGroup(panel1Layout.createParallelGroup()
                                                                                .addComponent(check_noblock)
                                                                                .addComponent(check_ff)))
                                                                .addGroup(panel1Layout.createSequentialGroup()
                                                                        .addComponent(label_max_players, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(field_max_players, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(panel1Layout.createSequentialGroup()
                                                                        .addComponent(label_max_ping, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(field_max_ping, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(label18)))
                                                        .addGap(0, 9, Short.MAX_VALUE))
                                                .addComponent(button_save, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                                        .addContainerGap())
                );
                panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_name)
                                                .addComponent(field_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(field_max_players, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label_max_players))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(field_max_ping, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label_max_ping)
                                                .addComponent(label18))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_tcp)
                                                .addComponent(field_tcp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(check_ff))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_udp)
                                                .addComponent(field_udp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(check_noblock))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                        .addComponent(button_save)
                                        .addContainerGap())
                );
            }

            //======== scroll_console ========
            {

                //---- text_console ----
                text_console.setEditable(false);
                text_console.setBackground(new Color(36, 36, 36));
                text_console.setForeground(new Color(204, 204, 204));
                scroll_console.setViewportView(text_console);
            }

            //---- text_input ----
            text_input.setBackground(new Color(36, 36, 36));
            text_input.setForeground(Color.white);
            text_input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    text_inputKeyPressed(e);
                }
            });

            //---- label_server ----
            label_server.setText("Tiny Server");
            label_server.setFont(new Font("Calibri", Font.BOLD, 22));

            //---- separator1 ----
            separator1.setOrientation(SwingConstants.VERTICAL);
            separator1.setForeground(Color.white);

            //---- separator2 ----
            separator2.setOrientation(SwingConstants.VERTICAL);
            separator2.setForeground(Color.white);

            //======== panel2 ========
            {

                //---- label_map ----
                label_map.setText("Map");
                label_map.setFont(label_map.getFont().deriveFont(label_map.getFont().getSize() + 3f));
                label_map.setLabelFor(combo_map);

                //---- label_mode ----
                label_mode.setText("Mode");
                label_mode.setFont(label_mode.getFont().deriveFont(label_mode.getFont().getSize() + 3f));
                label_mode.setLabelFor(combo_mode);

                //---- label_pts ----
                label_pts.setText("Pts (or kills) limit");
                label_pts.setLabelFor(field_pts);

                //---- label_timelimit ----
                label_timelimit.setText("Time limit");
                label_timelimit.setLabelFor(field_timelimit);

                //---- combo_mode ----
                combo_mode.setModel(new DefaultComboBoxModel(new String[]{
                        "Free For All",
                        "Team Deathmatch",
                        "Capture The Flag",
                        "Domination"
                }));

                //---- button_start ----
                button_start.setText("Start");
                button_start.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_startMouseClicked(e);
                    }
                });

                //---- button_stop ----
                button_stop.setText("Stop");
                button_stop.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_stopMouseClicked(e);
                    }
                });

                //---- button_froce_change ----
                button_froce_change.setText("Force change map");

                //---- button1 ----
                button1.setIcon(new ImageIcon("C:\\Users\\Styve\\TinyTank\\ServerTinyTank\\src\\main\\resources\\refresh.png"));
                button1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_refreshMouseClicked(e);
                    }
                });

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel2Layout.createParallelGroup()
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                        .addGroup(panel2Layout.createParallelGroup()
                                                                .addComponent(button_start)
                                                                .addComponent(label_timelimit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(panel2Layout.createParallelGroup()
                                                                .addGroup(panel2Layout.createSequentialGroup()
                                                                        .addGap(0, 196, Short.MAX_VALUE)
                                                                        .addComponent(button1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                                                                        .addComponent(button_stop)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(button_froce_change, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                                                                .addComponent(field_timelimit, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                        .addComponent(label_mode)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(combo_mode, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                        .addComponent(label_map)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(combo_map, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(panel2Layout.createSequentialGroup()
                                                        .addComponent(label_pts)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(field_pts, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)))
                                        .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label_map)
                                                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(combo_map, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(label_mode)
                                                .addComponent(combo_mode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_pts)
                                                .addComponent(field_pts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_timelimit)
                                                .addComponent(field_timelimit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(button_start)
                                                .addComponent(button_stop)
                                                .addComponent(button_froce_change))
                                        .addContainerGap())
                );
            }

            //---- label_server2 ----
            label_server2.setText("game preferences");
            label_server2.setFont(new Font("Calibri", Font.BOLD, 22));

            //======== panel3 ========
            {

                //======== scroll_players ========
                {

                    //---- table_players ----
                    table_players.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table_players.setEnabled(false);
                    scroll_players.setViewportView(table_players);
                }

                //---- label_uptime ----
                label_uptime.setText("0:00:00:00");

                //---- label1 ----
                label1.setText("Uptime");

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel3Layout.createParallelGroup()
                                                .addGroup(panel3Layout.createSequentialGroup()
                                                        .addComponent(scroll_players, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                                                        .addContainerGap())
                                                .addGroup(panel3Layout.createSequentialGroup()
                                                        .addGap(0, 222, Short.MAX_VALUE)
                                                        .addComponent(label1)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(label_uptime))))
                );
                panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(scroll_players, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label_uptime)
                                                .addComponent(label1))
                                        .addContainerGap())
                );
            }

            //---- label_server3 ----
            label_server3.setText("Informations");
            label_server3.setFont(new Font("Calibri", Font.BOLD, 22));

            GroupLayout rootPanelLayout = new GroupLayout(rootPanel);
            rootPanel.setLayout(rootPanelLayout);
            rootPanelLayout.setHorizontalGroup(
                    rootPanelLayout.createParallelGroup()
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(rootPanelLayout.createParallelGroup()
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(hSpacer1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(label_server)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(hSpacer2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(rootPanelLayout.createParallelGroup()
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(separator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(hSpacer3, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(label_server2)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(hSpacer4, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(rootPanelLayout.createParallelGroup()
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(hSpacer5, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(label_server3)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(hSpacer6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                    .addContainerGap())))
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                    .addGroup(rootPanelLayout.createParallelGroup()
                                                            .addComponent(text_input)
                                                            .addComponent(scroll_console))
                                                    .addContainerGap())))
            );
            rootPanelLayout.setVerticalGroup(
                    rootPanelLayout.createParallelGroup()
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(hSpacer1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label_server)
                                            .addComponent(hSpacer2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label_server2)
                                            .addComponent(hSpacer4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hSpacer3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label_server3)
                                            .addComponent(hSpacer6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hSpacer5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(separator1, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                                            .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(separator2, GroupLayout.Alignment.TRAILING)
                                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(scroll_console, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(text_input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
            );
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
// Generated using JFormDesigner Evaluation license - Styve SIMONNEAU
private JPanel rootPanel;
private JPanel panel1;
private JLabel label_name;
private JLabel label_max_players;
private JLabel label_max_ping;
private JLabel label_tcp;
private JLabel label_udp;
private JTextField field_name;
private JTextField field_max_players;
private JTextField field_max_ping;
private JTextField field_udp;
private JTextField field_tcp;
private JCheckBox check_ff;
private JCheckBox check_noblock;
private JLabel label18;
private JButton button_save;
private JScrollPane scroll_console;
private JTextArea text_console;
private JTextField text_input;
private JLabel label_server;
private JSeparator separator1;
private JSeparator separator2;
private JPanel panel2;
private JLabel label_map;
private JLabel label_mode;
private JLabel label_pts;
private JLabel label_timelimit;
private JComboBox<String> combo_map;
private JComboBox combo_mode;
private JTextField field_pts;
private JTextField field_timelimit;
private JButton button_start;
private JButton button_stop;
private JButton button_froce_change;
private JButton button1;
private JLabel label_server2;
private JPanel panel3;
private JScrollPane scroll_players;
private JTable table_players;
private JLabel label_uptime;
private JLabel label1;
private JLabel label_server3;
private JPanel hSpacer1;
private JPanel hSpacer2;
private JPanel hSpacer3;
private JPanel hSpacer4;
private JPanel hSpacer5;
private JPanel hSpacer6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
