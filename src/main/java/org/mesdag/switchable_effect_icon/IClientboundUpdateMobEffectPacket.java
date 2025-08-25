package org.mesdag.switchable_effect_icon;

import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;

public interface IClientboundUpdateMobEffectPacket {
    boolean switchable_effect_icon$isEnabled();

    static IClientboundUpdateMobEffectPacket of(ClientboundUpdateMobEffectPacket packet) {
        return (IClientboundUpdateMobEffectPacket) packet;
    }
}
