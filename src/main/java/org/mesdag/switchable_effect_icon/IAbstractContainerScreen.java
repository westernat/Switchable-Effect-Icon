package org.mesdag.switchable_effect_icon;

import net.neoforged.neoforge.common.util.TriState;

public interface IAbstractContainerScreen {
    default TriState switchable_effect_icon$onMouseClicked(double mouseX, double mouseY, int button) {
        return TriState.DEFAULT;
    }

    default TriState switchable_effect_icon$onMouseReleased(double mouseX, double mouseY, int button) {
        return TriState.DEFAULT;
    }
}
