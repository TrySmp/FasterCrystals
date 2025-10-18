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

package xyz.reknown.fastercrystals;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.reknown.fastercrystals.api.FasterCrystalsAPI;
import xyz.reknown.fastercrystals.commands.FasterCrystalsCommand;
import xyz.reknown.fastercrystals.enums.AnimPackets;
import xyz.reknown.fastercrystals.listeners.bukkit.*;
import xyz.reknown.fastercrystals.listeners.packet.AnimationListener;
import xyz.reknown.fastercrystals.listeners.packet.InteractEntityListener;
import xyz.reknown.fastercrystals.listeners.packet.LastPacketListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class FasterCrystals extends JavaPlugin {

    private List<SimplePacketListenerAbstract> listeners;

    private final Map<UUID, AnimPackets> lastPacket = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> ignoreAnim = new ConcurrentHashMap<>();
    private final Map<Integer, EnderCrystal> crystalIds = new ConcurrentHashMap<>();

    private static final Set<Material> AIR_TYPES = Set.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new FasterCrystalsAPI();
        new FasterCrystalsCommand(this, "fastercrystals");

        getServer().getPluginManager().registerEvents(new EntityRemoveFromWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldUnloadListener(this), this);

        (this.listeners = List.of(
                new AnimationListener(this),
                new InteractEntityListener(this),
                new LastPacketListener(this)
        )).forEach(listener -> PacketEvents.getAPI().getEventManager().registerListener(listener));

        getLogger().info("FasterCrystals enabled with " + FasterCrystalsAPI.getInstance().getStateProvider().getClass().getSimpleName());
    }

    @Override
    public void onDisable() {
        listeners.forEach(listener -> PacketEvents.getAPI().getEventManager().unregisterListener(listener));
    }

    public void spawnCrystal(Location loc, Player player, ItemStack item) {
        Location clonedLoc = loc.clone().subtract(0.5, 0.0, 0.5);
        if (!AIR_TYPES.contains(clonedLoc.getBlock().getType())) return;

        clonedLoc.add(0.5, 1.0, 0.5);
        List<Entity> nearbyEntities = new ArrayList<>(clonedLoc.getWorld().getNearbyEntities(clonedLoc, 0.5, 1, 0.5, entity -> !(entity instanceof Player p) || p.getGameMode() != GameMode.SPECTATOR));

        if (nearbyEntities.isEmpty()) {
            loc.getWorld().spawn(clonedLoc.subtract(0.0, 1.0, 0.0), EnderCrystal.class, entity -> entity.setShowingBottom(false));

            if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

}