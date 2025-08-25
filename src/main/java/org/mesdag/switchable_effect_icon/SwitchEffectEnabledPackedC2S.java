package org.mesdag.switchable_effect_icon;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public record SwitchEffectEnabledPackedC2S(ResourceLocation effect, boolean enabled) {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(SwitchableEffectIcon.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, SwitchEffectEnabledPackedC2S.class, SwitchEffectEnabledPackedC2S::encode, SwitchEffectEnabledPackedC2S::decode, SwitchEffectEnabledPackedC2S::handle);
    }

    public static void sendToServer(MobEffect effect, boolean enabled) {
        CHANNEL.sendToServer(new SwitchEffectEnabledPackedC2S(ForgeRegistries.MOB_EFFECTS.getKey(effect), enabled));
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(effect);
        friendlyByteBuf.writeBoolean(enabled);
    }

    public static SwitchEffectEnabledPackedC2S decode(FriendlyByteBuf friendlyByteBuf) {
        return new SwitchEffectEnabledPackedC2S(friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                MobEffectInstance instance = player.getActiveEffectsMap().get(ForgeRegistries.MOB_EFFECTS.getValue(effect));
                if (instance != null && IMobEffectInstance.isSwitchableEffect(instance)) {
                    IMobEffectInstance.of(instance).switchable_effect_icon$setEnabled(enabled);
                    MobEffect mobEffect = instance.getEffect();
                    if (enabled) {
                        mobEffect.addAttributeModifiers(player, player.getAttributes(), instance.getAmplifier());
                    } else {
                        mobEffect.removeAttributeModifiers(player, player.getAttributes(), instance.getAmplifier());
                    }
                    player.effectsDirty = true;
                }
            }
        });
        context.setPacketHandled(true);
    }
}
