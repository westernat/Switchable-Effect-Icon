package org.mesdag.switchable_effect_icon.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.switchable_effect_icon.IMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements IMobEffectInstance {
    @Unique
    private boolean switchable_effect_icon$enabled = true;

    @Override
    public void switchable_effect_icon$setEnabled(boolean enabled) {
        this.switchable_effect_icon$enabled = enabled;
    }

    @Override
    public boolean switchable_effect_icon$isEnabled() {
        return switchable_effect_icon$enabled;
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveExtra(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        tag.putBoolean("switchable_effect_icon:is_enabled", switchable_effect_icon$isEnabled());
    }

    @Inject(method = "load", at = @At("RETURN"))
    private static void loadExtra(CompoundTag nbt, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (nbt.contains("switchable_effect_icon:is_enabled")) {
            IMobEffectInstance.of(cir.getReturnValue()).switchable_effect_icon$setEnabled(nbt.getBoolean("switchable_effect_icon:is_enabled"));
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;isDurationEffectTick(II)Z"))
    private boolean skip(boolean original) {
        if (!switchable_effect_icon$isEnabled()) {
            return false;
        }
        return original;
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void merge(MobEffectInstance other, CallbackInfoReturnable<Boolean> cir) {
        if (!IMobEffectInstance.of(other).switchable_effect_icon$isEnabled()) {
            switchable_effect_icon$setEnabled(false);
        }
    }
}
