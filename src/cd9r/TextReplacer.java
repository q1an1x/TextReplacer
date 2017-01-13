package cd9r;

/*
 * Make the world a better place.
 */

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.plugin.PluginBase;

import java.util.Calendar;
import java.util.HashMap;
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
            Calendar time = Calendar.getInstance();

            HashMap<String, String> variables = new HashMap<String, String>() {
                {
                    put("{PLAYER}", name);
                    put("{YEAR}", String.valueOf(time.get(Calendar.YEAR)));
                    put("{MONTH}", String.valueOf(time.get(Calendar.MONTH) + 1));
                    put("{DAY}", String.valueOf(time.get(Calendar.DAY_OF_MONTH)));
                    put("{HOUR}", String.valueOf(time.get(Calendar.HOUR_OF_DAY)));
                    put("{MINUTE}", String.valueOf(time.get(Calendar.MINUTE)));
                    put("{SECOND}", String.valueOf(time.get(Calendar.SECOND)));
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
