package org.mesdag.switchable_effect_icon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;

@EventBusSubscriber(modid = SwitchableEffectIcon.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class GameClientEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherEffectScreenTooltips(GatherEffectScreenTooltipsEvent event) {
        if (!IMobEffectInstance.of(event.getEffectInstance()).switchable_effect_icon$isEnabled()) {
            event.getTooltip().add(Component.translatable("tooltip.switchable_effect_icon.disabled").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
