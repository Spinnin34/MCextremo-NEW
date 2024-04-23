package p.mcextremo.commads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import p.mcextremo.MCextremo;
import p.mcextremo.events.vidas;
import p.mcextremo.mensaje.MessageUtils;

import java.util.List;

public class VidasCommand implements CommandExecutor {
    private final String PERMISSION = "mcextremo.user.vidas";

    private static vidas vidasManager;
    private MCextremo plugin;

    public VidasCommand(vidas vidasManager, MCextremo plugin) {
        this.plugin = plugin;
        this.vidasManager = vidasManager;
    }

    public static int getVidasRestantes(Player player) {
        return vidasManager.getVidasRestantes(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(config.getString("prefix" + " " + "notperms"));
            return true;
        }

        if (sender instanceof Player) {
            Player player1 = (Player) sender;
            int vidasRestantes = getVidasRestantes(player1);

            List<String> mensajes = config.getStringList("vidas.view.command");
            for (String mensaje : mensajes) {
                player1.sendMessage(MessageUtils.sendCenteredMessage(mensaje).replace("%vidas%", String.valueOf(vidasRestantes)));
            }
            return true;
        }
        return true;
    }

}
