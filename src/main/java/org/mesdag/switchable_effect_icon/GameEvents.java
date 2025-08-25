package org.mesdag.switchable_effect_icon;

import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SwitchableEffectIcon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GameEvents {
    @SubscribeEvent
    public static void potionColorCalculation(PotionColorCalculationEvent event) {
        if (event.getColor() > 0 && event.getEffects().stream().noneMatch(instance -> IMobEffectInstance.of(instance).switchable_effect_icon$isEnabled())) {
            event.setColor(0);
        }
    }
}
