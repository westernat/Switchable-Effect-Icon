package org.mesdag.switchable_effect_icon;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;

@EventBusSubscriber(modid = SwitchableEffectIcon.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class GameEvents {
    @SubscribeEvent
    public static void effectParticleModification(EffectParticleModificationEvent event) {
        if (event.isVisible() && !IMobEffectInstance.of(event.getEffect()).switchable_effect_icon$isEnabled()) {
            event.setVisible(false);
        }
    }
}
