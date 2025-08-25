package org.mesdag.switchable_effect_icon;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = SwitchableEffectIcon.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModEvents {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SwitchEffectEnabledPackedC2S.TYPE, SwitchEffectEnabledPackedC2S.STREAM_CODEC, SwitchEffectEnabledPackedC2S::handle);
    }
}
