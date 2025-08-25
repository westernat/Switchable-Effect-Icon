package org.mesdag.switchable_effect_icon.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.switchable_effect_icon.IAbstractContainerScreen;
import org.mesdag.switchable_effect_icon.IMobEffectInstance;
import org.mesdag.switchable_effect_icon.SwitchEffectEnabledPackedC2S;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin implements IAbstractContainerScreen {
    @Unique
    private boolean switchable_effect_icon$mouseClicked = false;

    @Override
    public void switchable_effect_icon$onMouseClicked() {
        this.switchable_effect_icon$mouseClicked = true;
    }

    @ModifyExpressionValue(method = "renderEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private List<Component> gatherEffectScreenTooltips(List<Component> original, @Local(ordinal = 0) MobEffectInstance instance) {
        if (!IMobEffectInstance.of(instance).switchable_effect_icon$isEnabled()) {
            original = Lists.newArrayList(original);
            original.add(Component.translatable("tooltip.switchable_effect_icon.disabled").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (switchable_effect_icon$mouseClicked && IMobEffectInstance.isSwitchableEffect(instance)) {
            this.switchable_effect_icon$mouseClicked = false;
            IMobEffectInstance i = IMobEffectInstance.of(instance);
            boolean switched = !i.switchable_effect_icon$isEnabled();
            i.switchable_effect_icon$setEnabled(switched);
            SwitchEffectEnabledPackedC2S.sendToServer(instance.getEffect(), switched);
        }
        return original;
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
