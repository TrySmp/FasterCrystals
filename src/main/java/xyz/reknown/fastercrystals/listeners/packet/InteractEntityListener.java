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

package xyz.reknown.fastercrystals.listeners.packet;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.reknown.fastercrystals.FasterCrystals;
import xyz.reknown.fastercrystals.api.FasterCrystalsAPI;

import java.util.Set;

@RequiredArgsConstructor
public class InteractEntityListener extends SimplePacketListenerAbstract {

    private final FasterCrystals plugin;
    private final Set<Material> ALLOWED_BLOCKS = Set.of(Material.OBSIDIAN, Material.BEDROCK);

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
        if (wrapper.getAction() != WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT) return;

        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        if (!(FasterCrystalsAPI.getInstance().isFastCrystalsEnabled(player))) return;

        ItemStack item;
        if (wrapper.getHand() == InteractionHand.MAIN_HAND) {
            item = player.getInventory().getItemInMainHand();
        } else {
            item = player.getInventory().getItemInOffHand();
        }

        if (item.getType() != Material.END_CRYSTAL) return;

        EnderCrystal entity = plugin.getCrystalIds().get(wrapper.getEntityId());
        if (entity == null) return;

        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        FoliaScheduler.getRegionScheduler().run(plugin, eyeLoc, task -> {
            Location blockLoc = entity.getLocation().subtract(0.5, 1.0, 0.5);

            RayTraceResult result = eyeLoc.getWorld().rayTraceBlocks(eyeLoc, direction, player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).getValue());
            if (result == null || !ALLOWED_BLOCKS.contains(result.getHitBlock().getType())) return;
            if (!result.getHitBlock().getLocation().equals(blockLoc)) return;

            plugin.spawnCrystal(entity.getLocation(), player, item);
        });
    }

}
