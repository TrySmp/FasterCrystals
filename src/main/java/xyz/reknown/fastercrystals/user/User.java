package xyz.reknown.fastercrystals.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.reknown.fastercrystals.api.FasterCrystalsAPI;
import xyz.reknown.fastercrystals.enums.AnimPackets;

@Getter
public class User {
    private final Player player;
    @Setter private AnimPackets lastPacket;
    @Setter private boolean ignoreAnim;

    public User(Player player) {
        this.player = player;
    }

    public boolean isFasterCrystals() {
        return FasterCrystalsAPI.getInstance().isFastCrystalsEnabled(player);
    }
}