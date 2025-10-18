/*
 * Copyright (C) 2023-2025 Jyguy
 *
 * FasterCrystals is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FasterCrystals is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package xyz.reknown.fastercrystals.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.reknown.fastercrystals.api.impl.DefaultStateProvider;

public class FasterCrystalsAPI {

    @Getter
    private static FasterCrystalsAPI instance;

    @Setter
    @Getter
    private FasterCrystalsStateProvider stateProvider = new DefaultStateProvider();

    public FasterCrystalsAPI() {
        FasterCrystalsAPI.instance = new FasterCrystalsAPI();
    }

    /**
     * Sets the FasterCrystals toggle state for a specific player.
     *
     * @param player the player whose toggle state will be updated
     * @param enabled true to enable fast crystals, false to disable
     */
    public void setFastCrystals(@NotNull Player player, boolean enabled) {
        stateProvider.setState(player, enabled);
    }

    /**
     * Checks if FasterCrystals is currently enabled for a specific player.
     *
     * @param player the player to check
     * @return true if enabled, false otherwise
     */
    public boolean isFastCrystalsEnabled(@NotNull Player player) {
        return stateProvider.isEnabled(player);
    }

    /**
     * Toggles the FasterCrystals state for the specified player.
     * If it was enabled, it will be disabled, and vice versa.
     *
     * @param player the player whose toggle state will be flipped
     * @return the new state after toggling
     */
    public boolean toggleFastCrystals(@NotNull Player player) {
        return stateProvider.toggleState(player);
    }

}