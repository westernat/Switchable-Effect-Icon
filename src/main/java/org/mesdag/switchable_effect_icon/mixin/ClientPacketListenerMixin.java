package org.mesdag.switchable_effect_icon.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.switchable_effect_icon.IClientboundUpdateMobEffectPacket;
import org.mesdag.switchable_effect_icon.IMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @ModifyExpressionValue(method = "handleUpdateMobEffect", at = @At(value = "NEW", target = "(Lnet/minecraft/core/Holder;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private MobEffectInstance modify(MobEffectInstance original, @Local(argsOnly = true) ClientboundUpdateMobEffectPacket packet) {
        IMobEffectInstance.of(original).switchable_effect_icon$setEnabled(IClientboundUpdateMobEffectPacket.of(packet).switchable_effect_icon$isEnabled());
        return original;
    }
}
