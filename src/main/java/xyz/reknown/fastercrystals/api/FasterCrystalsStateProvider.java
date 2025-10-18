package xyz.reknown.fastercrystals.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface FasterCrystalsStateProvider {

    /**
     * @param player the player to check
     * @return true if enabled, false otherwise
     */
    boolean isEnabled(@NotNull Player player);

    /**
     * @param player the player whose state will be updated
     * @param enabled true to enable, false to disable
     */
    void setState(@NotNull Player player, boolean enabled);

    /**
     * @param player the player whose state will be toggled
     * @return the new state after toggling
     */
    default boolean toggleState(@NotNull Player player) {
        boolean newState = !isEnabled(player);
        setState(player, newState);
        return newState;
    }

}