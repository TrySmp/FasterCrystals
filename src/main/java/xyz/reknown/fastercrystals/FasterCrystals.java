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
import lombok.Setter;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.reknown.fastercrystals.api.FasterCrystalsStateProvider;
import xyz.reknown.fastercrystals.enums.AnimPackets;
import xyz.reknown.fastercrystals.listeners.bukkit.EntityRemoveFromWorldListener;
import xyz.reknown.fastercrystals.listeners.bukkit.EntitySpawnListener;
import xyz.reknown.fastercrystals.listeners.bukkit.WorldUnloadListener;
import xyz.reknown.fastercrystals.listeners.packet.AnimationListener;
import xyz.reknown.fastercrystals.listeners.packet.InteractEntityListener;
import xyz.reknown.fastercrystals.listeners.packet.LastPacketListener;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class FasterCrystals extends JavaPlugin {

    @Setter
    private static FasterCrystalsStateProvider stateProvider = null;

    private final Map<UUID, AnimPackets> lastPacket = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> ignoreAnim = new ConcurrentHashMap<>();
    private final Map<Integer, EnderCrystal> crystalIds = new ConcurrentHashMap<>();

    private List<SimplePacketListenerAbstract> listeners;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EntityRemoveFromWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldUnloadListener(this), this);

        (this.listeners = List.of(
                new AnimationListener(this),
                new InteractEntityListener(this),
                new LastPacketListener(this)
        )).forEach(listener -> PacketEvents.getAPI().getEventManager().registerListener(listener));
    }

    @Override
    public void onDisable() {
        listeners.forEach(listener -> PacketEvents.getAPI().getEventManager().unregisterListener(listener));
    }

    public boolean isEnabled(Player player) {
        if (stateProvider == null) return false;
        return stateProvider.isEnabled(player);
    }

}