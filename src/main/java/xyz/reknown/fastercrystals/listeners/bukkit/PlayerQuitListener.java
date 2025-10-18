package xyz.reknown.fastercrystals.listeners.bukkit;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.reknown.fastercrystals.FasterCrystals;

@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

    private final FasterCrystals plugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getLastPacket().remove(player.getUniqueId());
        plugin.getIgnoreAnim().remove(player.getUniqueId());
    }

}
