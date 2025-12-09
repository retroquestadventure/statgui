package Retro.statgui;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;


/* Klasse benötigt "CommandExecutor"
   da die ja ein Command ( hier "/stats")
   ausgeführt wird
 */

public class StatsCommand implements CommandExecutor {

    // Neue Klasse "statsListener" als Klassen Variable
    private final StatsListener statsListener;

    /* Konstruktor der StatsCommand Klasse mit den notwendigen
        Initialisierungen.
    */
    public StatsCommand(StatsListener statsListener) {
        this.statsListener = statsListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /* Klappt nur, falls der Command von einem Spieler getätigt wurde,
           sonst gibt doe Funktion "false" wieder und bricht ab.
         */
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        // Gui initialisieren und Name in der Gui vergeben
        Inventory statsGUI = Bukkit.createInventory(null, 9, ChatColor.GREEN  + "Deine Stats");

        // Beispiel-Statistiken

        // Kills von Mobs vom Spieler
        int kills = player.getStatistic(Statistic.MOB_KILLS);

        // Wie viele Blöcke wurden vom Spieler platziert?
        int blocksPlaced = 0;

        // Wie viele Blöcke wurden abgebaut?
        int blocksMined = 0;

        // Durchsuche alle Blöcke im Enum "Material"
        for (Material material : Material.values()) {
            if (material.isBlock()) {
                // Falls das Material ein Block ist, wird gewählt wie oft es genutzt wurde.
                // Mit nutzen ist dann hier das Platzieren des Blocks gemeint
                blocksPlaced += player.getStatistic(Statistic.USE_ITEM, material);
                blocksMined += player.getStatistic(Statistic.MINE_BLOCK, material);
            }
        }

        // Wie oft ist der Spieler gestorben?
        int deaths = player.getStatistic(Statistic.DEATHS);

        // Wie oft ist der Spieler gesprungen?
        int jumped = player.getStatistic(Statistic.JUMP);

        // Wie hoch ist der verursachte Schaden des Spielers
        /* Statistic.DAMAGE_DEALT misst den verursachten Schaden in Zehnteln von halben Herzen,
           daher muss der Wert noch durch 10 geteilt werden.
         */
        int damageRaw  = player.getStatistic(Statistic.DAMAGE_DEALT);
        double damageHearts = damageRaw / 10.0;
        // Anzeige als Kommazahl
        String damageDisplay = String.format("%.2f", damageHearts);

        // Wie oft hat der Spieler das Spiel verlassen ( quit )
        int quits = player.getStatistic(Statistic.LEAVE_GAME);

        //int nachrichten = player.getStatistic(Statistic)

        // Spielzeit des Spielers
        int ticksPlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        // Ein Tick entspricht 20 Sekunden
        long secondsPlayed = ticksPlayed / 20;
        long minutesPlayed = secondsPlayed / 60;
        long hoursPlayed = minutesPlayed / 60;
        long remainingMinutes = minutesPlayed % 60;

        String playtimeDisplay = hoursPlayed + "h " + remainingMinutes + "m";

        //wie oft wurde der Schwarzmarkt besucht
        /*
            Hier wird gezählt wie oft der Spieler den Text "blablabla"
            vom Schwarzmarkthändler nutzt

            TODO: IN DB Speichern und Abrufbar machen

         */
        int blackMarketVisited = statsListener.blackMarketVisited;


        // Gesamtzahl Votes ( aus VotingPlugin )

        // vote api nutzen
        int countAllVotes =  0;

        // GUI füllen mit Items
        /* 0,1,2,3,4,5 ist der Platz in der GUi wo die Daten und die Grafik dargestellt werden.
            0 = Erster Platz
         */
        statsGUI.setItem(0, createItem(Material.GOLDEN_SWORD, "Mob-Kills", "Kills: " + kills));
        statsGUI.setItem(1, createItem(Material.STONE, "Gesetzte Blöcke", "Blöcke: " + blocksPlaced));
        statsGUI.setItem(2, createItem(Material.TOTEM_OF_UNDYING, "Tode", "Tode: " + deaths));
        statsGUI.setItem(3, createItem(Material.DIAMOND_BOOTS, "Sprünge", "Sprünge: " + jumped));
        statsGUI.setItem(4, createItem(Material.DIAMOND_SWORD, "Verursachter Schaden", "Verursachter Schaden: " + damageDisplay));
        statsGUI.setItem(5, createItem(Material.BARRIER, "Spiel verlassen", "Spiel verlassen: " + quits));
        statsGUI.setItem(6, createItem(Material.NETHERITE_PICKAXE, "Blöcke abgebaut", "Blöcke abgebaut: " + blocksMined));
        statsGUI.setItem(7, createItem(Material.CLOCK, "Spielzeit", "Gesamte Spielzeit(Farmwelt +  Freebuild + Citbuild) ist zusammen: " + playtimeDisplay));
        statsGUI.setItem(8, createItem(Material.CLOCK, "Schwarzmarkt", "Schwarzmarkt besucht: " + blackMarketVisited));


        // Leere Slots mit Glas füllen (optional, nur visuell)
        for (int i = 0; i < statsGUI.getSize(); i++) {
            if (statsGUI.getItem(i) == null) {
                statsGUI.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, "", ""));
            }
        }

        // GUI öffnen
        player.openInventory(statsGUI);
        return true;
    }

    // Funktion zur Darstellung der Items in der Gui
    private ItemStack createItem(Material mat, String name, String loreLine) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + name);
            meta.setLore(Collections.singletonList(ChatColor.GRAY + loreLine));
            item.setItemMeta(meta);
        }
        return item;
    }
}
