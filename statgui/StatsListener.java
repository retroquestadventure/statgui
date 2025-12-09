

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* Neue Klasse "StatsListener"
    implementiert Listener die auf bestimmte Events reagieren können.
    Hier für das Reagieren auf das Beenden des Spiels ( quit )
    und das klicken auf das Inventar
 */

public class StatsListener implements Listener {
    /* EventHandler der auf das Klicken auf das Inventar reagieren kann.
        Falls die GUI geöffnet wurde, wird jeder Zugriff gesperrt.
        (Verhindert, dass der Spieler sich die GUI items z.B. ins Inventar legt)
    */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Überprüfe, ob das betroffene Inventar die GUI ist
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Deine Stats")) {
            event.setCancelled(true); // verhindert jegliche Interaktion
        }
    }

    private final Map<UUID, Integer> spielerHalloZaehler = new HashMap<>();
    public  int blackMarketVisited;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        if (message.contains("Pssst! Verrate niemals die Geschäfte")) {
            UUID uuid = player.getUniqueId();
            int count = spielerHalloZaehler.getOrDefault(uuid, 0) + 1;
            spielerHalloZaehler.put(uuid, count);
            blackMarketVisited = count;
        }
    }
}
