package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

/** Makes a tiny periodic rotation so the local client remains active. */
public final class AntiAfkModule extends Module {
    private int ticks;
    public AntiAfkModule() { super("Anti AFK", "Periodically makes a small camera movement.", ModuleCategory.PLAYER); }
    @Override public void tick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) return;
        if (++ticks >= 20 * 45) {
            client.player.setYaw(client.player.getYaw() + 0.15f);
            ticks = 0;
        }
    }
}
