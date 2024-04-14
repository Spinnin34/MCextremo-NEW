package p.mcextremo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import p.mcextremo.chat.ChatHandler;
import p.mcextremo.commads.VidasCommand;
import p.mcextremo.events.SistemaPvP;
import p.mcextremo.events.vidas;
import p.mcextremo.hard.EventManager;
import p.mcextremo.mensaje.MessageUtils;
import p.mcextremo.papi.variables;
import p.mcextremo.spectator.SpectatorListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class MCextremo extends JavaPlugin {
    private static vidas vidasManager;
    public String latestversion;
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    private EventManager eventManager;

    private boolean pvpActivo = false;
    private ChatHandler chatHandler;

    private static long cooldownEndTime = 0;

    public static long getCooldownEndTime() {
        return cooldownEndTime;
    }

    public static void setCooldownEndTime(long endTime) {
        cooldownEndTime = endTime;
    }


    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File ajustesFile = new File(getDataFolder(), "config.yml");
        if (!ajustesFile.exists()) {
            saveResource("config.yml", false);
        }





        this.vidasManager = new vidas(this);
        chatHandler = new ChatHandler(new vidas(this), this);
        this.saveDefaultConfig();
        reloadConfig();
        updateChecker();

        eventManager = new EventManager(this);
        getServer().getPluginManager().registerEvents(new SpectatorListener(this), this);
        getServer().getPluginManager().registerEvents(vidasManager, this);
        this.getServer().getPluginManager().registerEvents(new SistemaPvP(this, 3600), this);
        getServer().getPluginManager().registerEvents(chatHandler, this);
        getCommand("vidas").setExecutor(new VidasCommand(vidasManager, this));


        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new variables(this).register();
        } else {
            getLogger().warning("PlaceholderAPI no encontrado. Algunos placeholders no funcionarán.");
        }

        // Plugin startup logic

    }

    public boolean isPvPActivo() {
        return pvpActivo;
    }

    public void setPvPActivo(boolean nuevoEstadoPvP) {
        pvpActivo = nuevoEstadoPvP;
    }
    public EventManager getEventManager() {
        return eventManager;
    }


    public static int getVidasRestantes(Player player) {
        return vidasManager.getVidasRestantes(player);
    }

    public void updateChecker(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=114819").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (latestversion.length() <= 7) {
                if(!version.equals(latestversion)){
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§x§F§B§0§0§0§0§lM§x§D§C§0§0§0§0§lC §x§B§C§0§0§0§0§lE§x§9§D§0§0§0§0§lX§x§7§E§0§0§0§0§lT§x§5§E§0§0§0§0§lR§x§3§F§0§0§0§0§lE§x§1§F§0§0§0§0§lM§x§0§0§0§0§0§0§lO"));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§7Nueva version de Plugin descargala:"));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§chttps://karmancos.42web.io/download/mcextremo.html"));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§fFrom Team Karmancos Studio"));
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
            Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§x§F§B§0§0§0§0§lM§x§D§C§0§0§0§0§lC §x§B§C§0§0§0§0§lE§x§9§D§0§0§0§0§lX§x§7§E§0§0§0§0§lT§x§5§E§0§0§0§0§lR§x§3§F§0§0§0§0§lE§x§1§F§0§0§0§0§lM§x§0§0§0§0§0§0§lO"));
            Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"Error while checking update.");
            Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage("§fFrom Team Karmancos Studio"));
            Bukkit.getConsoleSender().sendMessage(MessageUtils.sendCenteredMessage(" "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"Error while checking update.");
        }
    }
}
