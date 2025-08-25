package org.mesdag.switchable_effect_icon.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mesdag.switchable_effect_icon.IAbstractContainerScreen;
import org.mesdag.switchable_effect_icon.IMobEffectInstance;
import org.mesdag.switchable_effect_icon.SwitchEffectEnabledPackedC2S;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin implements IAbstractContainerScreen {
    @Unique
    private boolean switchable_effect_icon$mouseClicked = false;

    @Override
    public void switchable_effect_icon$onMouseClicked() {
        this.switchable_effect_icon$mouseClicked = true;
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", shift = At.Shift.AFTER))
    private void switchEnabled(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci, @Local(ordinal = 0) MobEffectInstance instance) {
        if (switchable_effect_icon$mouseClicked && IMobEffectInstance.isSwitchableEffect(instance)) {
            this.switchable_effect_icon$mouseClicked = false;
            IMobEffectInstance i = IMobEffectInstance.of(instance);
            boolean switched = !i.switchable_effect_icon$isEnabled();
            i.switchable_effect_icon$setEnabled(switched);
            PacketDistributor.sendToServer(new SwitchEffectEnabledPackedC2S(instance.getEffect(), switched));
        }
    }

    @WrapOperation(method = "renderIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(IIIIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
    private void makeTranslucent(GuiGraphics guiGraphics, int x, int y, int blitOffset, int width, int height, TextureAtlasSprite sprite, Operation<Void> original, @Local MobEffectInstance instance) {
        if (IMobEffectInstance.of(instance).switchable_effect_icon$isEnabled()) {
            original.call(guiGraphics, x, y, blitOffset, width, height, sprite);
        } else {
            RenderSystem.enableBlend();
            guiGraphics.setColor(1, 1, 1, 0.5F);
            original.call(guiGraphics, x, y, blitOffset, width, height, sprite);
            guiGraphics.setColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
        }
    }
}
