package xyz.reknown.fastercrystals.api.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.reknown.fastercrystals.api.FasterCrystalsStateProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultStateProvider implements FasterCrystalsStateProvider {

    private final Map<UUID, Boolean> playerStates = new ConcurrentHashMap<>();

    @Override
    public boolean isEnabled(@NotNull Player player) {
        return playerStates.getOrDefault(player.getUniqueId(), false);
    }

    @Override
    public void setState(@NotNull Player player, boolean enabled) {
        if (enabled) {
            playerStates.put(player.getUniqueId(), true);
        } else {
            playerStates.remove(player.getUniqueId());
        }
    }

}
