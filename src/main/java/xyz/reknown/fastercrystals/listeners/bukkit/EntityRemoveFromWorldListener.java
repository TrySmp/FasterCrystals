/*
 * Copyright (C) 2023-2025 Jyguy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>. 
 */

package xyz.reknown.fastercrystals.listeners.bukkit;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.reknown.fastercrystals.FasterCrystals;

@RequiredArgsConstructor
public class EntityRemoveFromWorldListener implements Listener {

    private final FasterCrystals plugin;

    @EventHandler
    public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        if (event.getEntityType() != EntityType.END_CRYSTAL) return;
        FoliaScheduler.getEntityScheduler().runDelayed(event.getEntity(), plugin, task -> plugin.getCrystalIds().remove(event.getEntity().getEntityId()), null, 40L);
    }

}
