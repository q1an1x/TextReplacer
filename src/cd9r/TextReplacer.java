package cd9r;

/*
 * Changing the way we play MC:PE
 */

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.plugin.PluginBase;

import java.util.Map;

public class TextReplacer extends PluginBase implements Listener {

    private Map<String, Object> list;

    @Override
    public void onLoad() {

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

            for(Map.Entry<String, Object> entry : this.list.entrySet()) {

                String search = entry.getKey().replace("{PLAYER}", name);
                String replacement = entry.getValue().toString().replace("{PLAYER}", name);

                ((TextPacket) pk).message = ((TextPacket) pk).message.replace(search, replacement);

            }

        }

    }

}
