
import org.bukkit.plugin.java.JavaPlugin;

//private DataBase sql;

/* Neue Klasse "StatsGUIPlugin"
    muss von JavaPlugin erben
    da es ja ein JavaPlugin für Minecraft ist.
 */

public class StatsGUIPlugin extends JavaPlugin {

    // Zum Aktivieren des dann neuen "stats" Commands
    @Override
    public void onEnable() {

        /*sql = new SQLManager(this);
        sql.connect();
        sql.initializeTables();
        sql.startReconnectScheduler();*/


        // Klasse "StatsListener" erstellen
        StatsListener statsListener = new StatsListener();
        // Plugin Manager mit Event Klasse registrieren.
        getServer().getPluginManager().registerEvents(statsListener, this);
        // Klasse für den "stats" Command aufrufen
        getCommand("stats").setExecutor(new StatsCommand(statsListener));
    }
}
