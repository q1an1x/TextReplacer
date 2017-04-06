package taylcd;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.plugin.PluginBase;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TextReplacer extends PluginBase implements Listener {

    private Map<String, Object> list;

    @Override
    public void onLoad() {

        File file = new File(getDataFolder(), "");
        if(!file.isDirectory()) if(!file.mkdir()) getLogger().notice("Unable to create data folder.");

        this.saveDefaultConfig();
        this.reloadConfig();

        this.list = this.getConfig().getAll();

    }

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDataPacketSend(DataPacketSendEvent event) {

        DataPacket pk = event.getPacket();
        if(pk instanceof TextPacket) {

            String name = event.getPlayer().getName();
            Calendar time = Calendar.getInstance();

            HashMap<String, String> variables = new HashMap<String, String>() {
                {
                    put("{PLAYER}", name);
		            put("{LEVEL}", event.getPlayer().getLevel().getName());
                }
            };

            for(Map.Entry<String, Object> entry : this.list.entrySet()) {

                String search = entry.getKey();
                String replacement = entry.getValue().toString();

                for(Map.Entry<String, String> variable : variables.entrySet()) {

                    search = search.replace(variable.getKey(), variable.getValue());
                    replacement = replacement.replace(variable.getKey(), variable.getValue());

                }

                ((TextPacket) pk).message = ((TextPacket) pk).message.replace(search, replacement);

            }

        }

    }

}
