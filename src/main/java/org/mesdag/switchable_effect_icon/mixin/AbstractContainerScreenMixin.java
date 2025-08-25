package org.mesdag.switchable_effect_icon.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.neoforge.common.util.TriState;
import org.mesdag.switchable_effect_icon.IAbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements IAbstractContainerScreen {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        TriState triState = switchable_effect_icon$onMouseClicked(mouseX, mouseY, button);
        if (!triState.isDefault()) cir.setReturnValue(triState.isTrue());
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        TriState triState = switchable_effect_icon$onMouseReleased(mouseX, mouseY, button);
        if (!triState.isDefault()) cir.setReturnValue(triState.isTrue());
    }
}
