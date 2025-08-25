package org.mesdag.switchable_effect_icon;

import net.minecraft.core.Holder;
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
        return instance.getEffect().value().isBeneficial();
    }

    static boolean hasEffect(Map<Holder<MobEffect>, MobEffectInstance> activeEffects, Holder<MobEffect> effect) {
        MobEffectInstance instance = activeEffects.get(effect);
        return instance != null && of(instance).switchable_effect_icon$isEnabled();
    }

    static @Nullable MobEffectInstance getEffect(Map<Holder<MobEffect>, MobEffectInstance> activeEffects, Holder<MobEffect> effect) {
        MobEffectInstance instance = activeEffects.get(effect);
        return instance == null || !of(instance).switchable_effect_icon$isEnabled() ? null : instance;
    }
}
