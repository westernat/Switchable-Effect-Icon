package org.mesdag.switchable_effect_icon;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IMobEffectInstance {
    void switchable_effect_icon$setEnabled(boolean enabled);

    boolean switchable_effect_icon$isEnabled();

    static IMobEffectInstance of(MobEffectInstance instance) {
        return (IMobEffectInstance) instance;
    }

    static boolean isSwitchableEffect(MobEffectInstance instance) {
        return instance.getEffect().isBeneficial();
    }

    static boolean hasEffect(Map<MobEffect, MobEffectInstance> activeEffects, MobEffect effect) {
        MobEffectInstance instance = activeEffects.get(effect);
        return instance != null && of(instance).switchable_effect_icon$isEnabled();
    }

    static @Nullable MobEffectInstance getEffect(Map<MobEffect, MobEffectInstance> activeEffects, MobEffect effect) {
        MobEffectInstance instance = activeEffects.get(effect);
        return instance == null || !of(instance).switchable_effect_icon$isEnabled() ? null : instance;
    }
}
