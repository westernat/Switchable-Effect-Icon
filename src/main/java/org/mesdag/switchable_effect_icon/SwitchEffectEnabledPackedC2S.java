package org.mesdag.switchable_effect_icon;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SwitchEffectEnabledPackedC2S(Holder<MobEffect> effect, boolean enabled) implements CustomPacketPayload {
    public static final Type<SwitchEffectEnabledPackedC2S> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(
            SwitchableEffectIcon.MODID,
            "switch_effect_enabled"
    ));
    public static final StreamCodec<RegistryFriendlyByteBuf, SwitchEffectEnabledPackedC2S> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC, SwitchEffectEnabledPackedC2S::effect,
            ByteBufCodecs.BOOL, SwitchEffectEnabledPackedC2S::enabled,
            SwitchEffectEnabledPackedC2S::new
    );

    @Override
    public @NotNull Type<SwitchEffectEnabledPackedC2S> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                MobEffectInstance instance = player.getActiveEffectsMap().get(effect);
                if (instance != null && IMobEffectInstance.isSwitchableEffect(instance)) {
                    IMobEffectInstance.of(instance).switchable_effect_icon$setEnabled(enabled);
                    MobEffect mobEffect = instance.getEffect().value();
                    if (enabled) {
                        mobEffect.addAttributeModifiers(player.getAttributes(), instance.getAmplifier());
                    } else {
                        mobEffect.removeAttributeModifiers(player.getAttributes());
                    }
                    player.effectsDirty = true;
                }
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
