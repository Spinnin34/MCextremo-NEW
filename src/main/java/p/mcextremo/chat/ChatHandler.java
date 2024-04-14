package p.mcextremo.chat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import p.mcextremo.MCextremo;
import p.mcextremo.events.vidas;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatHandler implements Listener {

    private vidas vidasManager;
    private MCextremo plugin;
    private Map<Character, String> fontMappings;

    public ChatHandler(vidas vidasManager, MCextremo plugin) {
        this.vidasManager = vidasManager;
        this.plugin = plugin;
        this.fontMappings = createFontMappings();
    }

    private Map<Character, String> createFontMappings() {
        // Define aquí tus mapeos de letra a letra con la fuente que deseas
        Map<Character, String> mappings = new HashMap<>();
        mappings.put('a', "ᴀ");
        mappings.put('b', "ʙ");
        mappings.put('c', "ᴄ");
        mappings.put('d', "ᴅ");
        mappings.put('e', "ᴇ");
        mappings.put('f', "ғ");
        mappings.put('g', "ɢ");
        mappings.put('h', "ʜ");
        mappings.put('i', "ɪ");
        mappings.put('j', "ᴊ");
        mappings.put('k', "ᴋ");
        mappings.put('l', "ʟ");
        mappings.put('m', "ᴍ");
        mappings.put('n', "ɴ");
        mappings.put('o', "ᴏ");
        mappings.put('p', "ᴘ");
        mappings.put('q', "ǫ");
        mappings.put('r', "ʀ");
        mappings.put('s', "s");
        mappings.put('t', "ᴛ");
        mappings.put('u', "ᴜ");
        mappings.put('v', "ᴠ");
        mappings.put('w', "ᴡ");
        mappings.put('x', "x");
        mappings.put('y', "ʏ");
        mappings.put('z', "ᴢ");

        mappings.put('A', "ᴀ");
        mappings.put('B', "ʙ");
        mappings.put('C', "ᴄ");
        mappings.put('D', "ᴅ");
        mappings.put('E', "ᴇ");
        mappings.put('F', "ғ");
        mappings.put('G', "ɢ");
        mappings.put('H', "ʜ");
        mappings.put('I', "ɪ");
        mappings.put('J', "ᴊ");
        mappings.put('K', "ᴋ");
        mappings.put('L', "ʟ");
        mappings.put('M', "ᴍ");
        mappings.put('N', "ɴ");
        mappings.put('O', "ᴏ");
        mappings.put('P', "ᴘ");
        mappings.put('Q', "ǫ");
        mappings.put('R', "ʀ");
        mappings.put('S', "s");
        mappings.put('T', "ᴛ");
        mappings.put('U', "ᴜ");
        mappings.put('V', "ᴠ");
        mappings.put('W', "ᴡ");
        mappings.put('X', "x");
        mappings.put('Y', "ʏ");
        mappings.put('Z', "ᴢ");
        mappings.put('%', " ");

        return mappings;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("chat.toggle.chat")) {

            Player jugador = event.getPlayer();
            int vidasRestantes = MCextremo.getVidasRestantes(jugador);

            String messageOriginal = event.getMessage();
            StringBuilder messageFormat = new StringBuilder("");

            if (config.getBoolean("chat.toggle.format")) {
                for (char letra : messageOriginal.toCharArray()) {
                    String letraFormateada = fontMappings.getOrDefault(letra, String.valueOf(letra));
                    messageFormat.append(letraFormateada);
                }
            } else {
                messageFormat.append(messageOriginal);
            }
            String chat = config.getString("chat.view");

            String messageEnd = chat.replace("%vidas%", String.valueOf(vidasRestantes)) + jugador.getName() + messageFormat.toString();

            event.setFormat(ChatColor.translateAlternateColorCodes('&', messageEnd));
        }
    }
}

