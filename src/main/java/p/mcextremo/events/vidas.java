package p.mcextremo.events;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import p.mcextremo.MCextremo;
import p.mcextremo.mensaje.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class vidas implements Listener {

    private Map<UUID, Integer> vidas;
    private MCextremo plugin;
    private File configFile;
    private YamlConfiguration config;

    public vidas(MCextremo plugin) {
        this.plugin = plugin;
        this.vidas = new HashMap<>();
        this.configFile = new File(MCextremo.getPlugin(MCextremo.class).getDataFolder(), "vidas.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        if (config.contains(uuid.toString())) {
            int vidasGuardadas = config.getInt(uuid.toString());
            vidas.put(uuid, vidasGuardadas);
        } else {
            vidas.put(uuid, 3);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        FileConfiguration config2 = plugin.getConfig();

        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        int vidasRestantes = vidas.getOrDefault(uuid, 3);

        if (vidasRestantes > 0) {
            vidasRestantes--;
            vidas.put(uuid, vidasRestantes);

            List<String> mensajes = config2.getStringList("vidas.player.text");
            for (String mensaje : mensajes) {
                player.sendMessage(MessageUtils.sendCenteredMessage(mensaje.replace("%vidas%", String.valueOf(vidasRestantes))));
            }

            guardarVidas(uuid, vidasRestantes);


            // Mensaje global
            List<String> mensajesG = config2.getStringList("vidas.global.text");
            for (String mensajeG : mensajesG) {
                Bukkit.broadcastMessage(MessageUtils.sendCenteredMessage(mensajeG.replace("%vidas%", String.valueOf(vidasRestantes)).replace("%player%", player.getName())));
            }
        } else {
            // Lógica del baneo ejecutada sincrónicamente en el hilo principal
            String playerName = player.getName();
            String banCommand = "ban " + playerName;
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), banCommand);


            List<String> mensajes = config2.getStringList("vidas.deleted.text");
            for (String mensaje : mensajes) {
                player.sendMessage(MessageUtils.sendCenteredMessage(mensaje));
            }


            List<String> mensajesG = config2.getStringList("vidas.global.text");
            for (String mensajeG : mensajesG) {
                Bukkit.broadcastMessage(MessageUtils.sendCenteredMessage(mensajeG.replace("%vidas%", String.valueOf(vidasRestantes)).replace("%player%", player.getName())));
            }
        }
    }



    private void guardarVidas(UUID uuid, int vidasRestantes) {
        // Manejar la concurrencia utilizando BukkitScheduler
        Bukkit.getScheduler().runTaskAsynchronously(MCextremo.getPlugin(MCextremo.class), () -> {
            config.set(uuid.toString(), vidasRestantes);

            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public int getVidasRestantes(Player player) {
        UUID uuid = player.getUniqueId();
        return vidas.getOrDefault(uuid, 3);
    }
}

