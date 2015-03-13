package com.lefrantguillaume.ui;

import com.lefrantguillaume.WindowController;
import com.lefrantguillaume.network.TinyServer;
import com.lefrantguillaume.utils.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Styve on 12/03/2015.
 */
public class ServerConfig extends JFrame {
    private TinyServer server;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private ScheduledFuture<?> t = null;

    public ServerConfig(TinyServer server) {
        this.server = server;
        initComponents();

        setContentPane(rootPanel);
        setBounds(200, 200, 800, 500);
        setMaximumSize(new Dimension(800, 500));
        setMinimumSize(new Dimension(800, 500));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        field_name.setText(Configuration.gameName);
        field_max_ping.setText(String.valueOf(Configuration.maxAllowedPing));
        field_max_players.setText(String.valueOf(Configuration.maxAllowedPlayers));
        field_tcp.setText(String.valueOf(Configuration.tcpPort));
        field_udp.setText(String.valueOf(Configuration.udpPort));
        field_pts.setText(String.valueOf(Configuration.ptsLimit));
        field_timelimit.setText(String.valueOf(Configuration.timeLimit));
        check_ff.setSelected(Configuration.friendlyFire);
        check_noblock.setSelected(Configuration.allyNoBlock);
        setVisible(true);
    }

    public void addToConsoleLog(String msg) {
        text_console.append(msg + "\n");
    }

    private void button_startMouseClicked(MouseEvent e) {
        if (button_start.getText().equals("Start server")) {
            WindowController.addConsoleMsg("Starting server...");
            button_start.setText("Restart server");
        } else {
            WindowController.addConsoleMsg("Restarting server...");
        }
        if (!this.server.start()) {
            button_start.setText("Start server");
            JOptionPane.showMessageDialog(this, "There is an error somewhere.. Please chck all the fields !", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

    private void button_saveMouseClicked(MouseEvent e) {
        Configuration.udpPort = Integer.valueOf(field_udp.getText());
        Configuration.tcpPort = Integer.valueOf(field_tcp.getText());
        Configuration.maxAllowedPing = Integer.valueOf(field_max_ping.getText());
        Configuration.maxAllowedPlayers = Integer.valueOf(field_max_players.getText());
        Configuration.ptsLimit = Integer.valueOf(field_pts.getText());
        Configuration.timeLimit = Integer.valueOf(field_timelimit.getText());
        Configuration.gameName = field_name.getText();
        Configuration.friendlyFire = check_ff.isSelected();
        Configuration.allyNoBlock = check_noblock.isSelected();
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
        scroll_console = new JScrollPane();
        text_console = new JTextArea();
        text_input = new JTextField();
        label_server = new JLabel();
        separator1 = new JSeparator();
        combo_map = new JComboBox();
        label_uptime = new JLabel();
        label_map = new JLabel();
        button_start = new JButton();
        button_froce_change = new JButton();
        scroll_players = new JScrollPane();
        table_players = new JTable();
        separator2 = new JSeparator();
        label_mode = new JLabel();
        combo_mode = new JComboBox();
        label_pts = new JLabel();
        field_pts = new JTextField();
        label_timelimit = new JLabel();
        field_timelimit = new JTextField();
        button_save = new JButton();

        //======== rootPanel ========
        {
            rootPanel.setMaximumSize(new Dimension(650, 480));
            rootPanel.setMinimumSize(new Dimension(650, 480));
            rootPanel.setPreferredSize(new Dimension(650, 480));

            // JFormDesigner evaluation mark
            rootPanel.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), rootPanel.getBorder())); rootPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


            //======== panel1 ========
            {

                //---- label_name ----
                label_name.setText("Game name");
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

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
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
                            .addContainerGap(15, Short.MAX_VALUE))
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
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

            //---- label_server ----
            label_server.setText("Tiny Server");
            label_server.setFont(new Font("Calibri", Font.BOLD, 22));

            //---- separator1 ----
            separator1.setOrientation(SwingConstants.VERTICAL);
            separator1.setForeground(Color.white);

            //---- label_uptime ----
            label_uptime.setText("0:00:00:00");

            //---- label_map ----
            label_map.setText("Map");
            label_map.setFont(label_map.getFont().deriveFont(label_map.getFont().getSize() + 3f));
            label_map.setLabelFor(combo_map);

            //---- button_start ----
            button_start.setText("Start server");
            button_start.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button_startMouseClicked(e);
                }
            });

            //---- button_froce_change ----
            button_froce_change.setText("Force change map");

            //======== scroll_players ========
            {
                scroll_players.setViewportView(table_players);
            }

            //---- separator2 ----
            separator2.setOrientation(SwingConstants.VERTICAL);
            separator2.setForeground(Color.white);

            //---- label_mode ----
            label_mode.setText("Mode");
            label_mode.setFont(label_mode.getFont().deriveFont(label_mode.getFont().getSize() + 3f));
            label_mode.setLabelFor(combo_mode);

            //---- combo_mode ----
            combo_mode.setModel(new DefaultComboBoxModel(new String[] {
                "Free For All",
                "Team Deathmatch",
                "Capture The Flag",
                "Domination"
            }));

            //---- label_pts ----
            label_pts.setText("Pts (or kills) limit");
            label_pts.setLabelFor(field_pts);

            //---- label_timelimit ----
            label_timelimit.setText("Time limit");
            label_timelimit.setLabelFor(field_timelimit);

            //---- button_save ----
            button_save.setText("Save all changes");
            button_save.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button_saveMouseClicked(e);
                }
            });

            GroupLayout rootPanelLayout = new GroupLayout(rootPanel);
            rootPanel.setLayout(rootPanelLayout);
            rootPanelLayout.setHorizontalGroup(
                rootPanelLayout.createParallelGroup()
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(rootPanelLayout.createParallelGroup()
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(rootPanelLayout.createParallelGroup()
                                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(rootPanelLayout.createSequentialGroup()
                                        .addGap(92, 92, 92)
                                        .addComponent(label_server)))
                                .addGap(12, 12, 12)
                                .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(rootPanelLayout.createParallelGroup()
                                    .addGroup(rootPanelLayout.createSequentialGroup()
                                        .addComponent(label_map)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(combo_map))
                                    .addGroup(GroupLayout.Alignment.TRAILING, rootPanelLayout.createSequentialGroup()
                                        .addGroup(rootPanelLayout.createParallelGroup()
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                    .addComponent(label_mode)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(combo_mode, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                .addGroup(rootPanelLayout.createParallelGroup()
                                                        .addComponent(label_pts)
                                                        .addComponent(label_timelimit))
                                                .addGap(18, 18, 18)
                                                .addGroup(rootPanelLayout.createParallelGroup()
                                                        .addComponent(field_pts, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                                        .addComponent(field_timelimit, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                                            .addComponent(button_save, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(separator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scroll_players, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(rootPanelLayout.createSequentialGroup()
                                        .addComponent(button_start)
                                        .addGap(12, 12, 12)
                                        .addComponent(button_froce_change)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(label_uptime)
                                        .addGap(0, 23, Short.MAX_VALUE))))
                            .addComponent(scroll_console, GroupLayout.Alignment.TRAILING)
                            .addComponent(text_input, GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
            );
            rootPanelLayout.setVerticalGroup(
                rootPanelLayout.createParallelGroup()
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                    .addComponent(label_server)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label_map)
                                            .addComponent(combo_map, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(rootPanelLayout.createParallelGroup()
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                    .addGap(18, 18, 18)
                                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(separator2)
                                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                                    .addComponent(scroll_players, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                                                    .addGap(0, 0, Short.MAX_VALUE))))
                                            .addGroup(rootPanelLayout.createSequentialGroup()
                                                    .addGap(7, 7, 7)
                                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(combo_mode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label_mode))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label_pts)
                                                            .addComponent(field_pts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label_timelimit)
                                                            .addComponent(field_timelimit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(button_save)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(rootPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(button_froce_change)
                                            .addComponent(label_uptime)
                                            .addComponent(button_start)))
                            .addComponent(separator1))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scroll_console, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    private JScrollPane scroll_console;
    private JTextArea text_console;
    private JTextField text_input;
    private JLabel label_server;
    private JSeparator separator1;
    private JComboBox combo_map;
    private JLabel label_uptime;
    private JLabel label_map;
    private JButton button_start;
    private JButton button_froce_change;
    private JScrollPane scroll_players;
    private JTable table_players;
    private JSeparator separator2;
    private JLabel label_mode;
    private JComboBox combo_mode;
    private JLabel label_pts;
    private JTextField field_pts;
    private JLabel label_timelimit;
    private JTextField field_timelimit;
    private JButton button_save;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
