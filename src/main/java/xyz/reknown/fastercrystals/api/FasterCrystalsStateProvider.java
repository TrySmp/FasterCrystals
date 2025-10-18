package xyz.reknown.fastercrystals.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface FasterCrystalsStateProvider {

    /**
     * @param player the player to check
     * @return true if enabled, false otherwise
     */
    boolean isEnabled(@NotNull Player player);

}