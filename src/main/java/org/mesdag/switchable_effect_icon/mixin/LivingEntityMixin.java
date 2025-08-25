package org.mesdag.switchable_effect_icon.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.mesdag.switchable_effect_icon.IMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();

    @WrapWithCondition(method = "onEffectUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"))
    private boolean shouldAdd(MobEffect effect, LivingEntity attributeinstance, AttributeMap entry, int i, @Local(argsOnly = true) MobEffectInstance instance) {
        return IMobEffectInstance.of(instance).switchable_effect_icon$isEnabled();
    }

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    private void hasEffect(MobEffect effect, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(IMobEffectInstance.hasEffect(getActiveEffectsMap(), effect));
    }

    @Inject(method = "getEffect", at = @At("HEAD"), cancellable = true)
    private void getEffect(MobEffect effect, CallbackInfoReturnable<MobEffectInstance> cir) {
        cir.setReturnValue(IMobEffectInstance.getEffect(getActiveEffectsMap(), effect));
    }
}
