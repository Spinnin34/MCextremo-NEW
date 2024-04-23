package p.mcextremo.events;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import p.mcextremo.MCextremo;
import p.mcextremo.mensaje.MessageUtils;

import java.util.List;

public class SistemaPvP implements Listener {


    private MCextremo plugin;
    private boolean pvpActivo;
    private int duracionPvP;
    private int tiempoRestante;
    public SistemaPvP(MCextremo plugin, int duracionPvP) {
        this.plugin = plugin;
        this.pvpActivo = false;
        this.duracionPvP = duracionPvP;
        this.tiempoRestante = duracionPvP;

        // Iniciar el sistema de PvP
        iniciarSistemaPvP();
    }
    private void iniciarSistemaPvP() {

        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("pvp.toggle")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (tiempoRestante > 0) {
                        tiempoRestante--;
                    } else {
                        pvpActivo = !pvpActivo;

                        if (pvpActivo) {
                            activarPvP();

                            plugin.setPvPActivo(true);

                            List<String> mensajes = config.getStringList("pvp.activate.text");
                            for (String mensaje : mensajes) {
                                sendMessageALL(MessageUtils.sendCenteredMessage(mensaje));
                            }

                            sendTitleALL(config.getString("prefix"), config.getString("toggle.activate"));
                        } else {
                            desactivarPvP();

                            plugin.setPvPActivo(false);

                            List<String> mensajes = config.getStringList("pvp.deactivated.text");

                            for (String mensaje : mensajes) {
                                sendMessageALL(MessageUtils.sendCenteredMessage(mensaje));
                            }

                            sendTitleALL(config.getString("prefix"), config.getString("toggle.deactivated"));
                        }

                        tiempoRestante = duracionPvP;
                    }
                }
            }.runTaskTimer(plugin, 0, 20); // Se ejecuta cada segundo
        }
    }

    private void activarPvP() {
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(true);
        }
    }

    private void desactivarPvP() {
        for (World world : Bukkit.getWorlds()) {
            world.setPVP(false);
        }
    }

    private void sendMessageALL(String mensaje) {
        Bukkit.broadcastMessage(mensaje);
        String.valueOf(pvpActivo);

    }

    private void sendTitleALL(String titulo, String subtitulo) {
        for (Player jugador : Bukkit.getOnlinePlayers()) {
            jugador.sendTitle(titulo, subtitulo, 10, 70, 20);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        String comando = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        if (comando.startsWith("/pvp on")) {
            if (player.hasPermission("mcextremo.pvp.on")) {

                List<String> mensajes = config.getStringList("pvp.activate.text");
                for (String mensaje : mensajes) {
                    sendMessageALL(MessageUtils.sendCenteredMessage(mensaje));
                }
                
                sendTitleALL(config.getString("prefix"), config.getString("toggle.activate"));

                pvpActivo = true;
                plugin.setPvPActivo(true);
                event.setCancelled(true);
            } else {
                player.sendMessage(config.getString("prefix" + " " + "notperms"));
            }
        } else if (comando.startsWith("/pvp off")) {
            if (player.hasPermission("mcextremo.pvp.off")) {
                
                
                List<String> mensajes = config.getStringList("pvp.deactivated.text");

                for (String mensaje : mensajes) {
                    sendMessageALL(MessageUtils.sendCenteredMessage(mensaje));
                }
                
                sendTitleALL(config.getString("prefix"), config.getString("toggle.deactivated"));

                pvpActivo = false;
                plugin.setPvPActivo(false);
                event.setCancelled(true);
            } else {
                player.sendMessage(config.getString("prefix" + " " + "notperms"));
            }
        }
    }
}



