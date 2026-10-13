package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

/** Raises gamma while enabled and restores the player's previous setting on exit. */
public final class FullbrightModule extends Module {
    private double previousGamma = -1;
    public FullbrightModule() { super("Fullbright", "Brightens caves and dark areas.", ModuleCategory.RENDER); }
    @Override protected void onEnable(MinecraftClient client) {
        previousGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(16.0);
    }
    @Override protected void onDisable(MinecraftClient client) {
        if (previousGamma >= 0) client.options.getGamma().setValue(previousGamma);
    }
}
