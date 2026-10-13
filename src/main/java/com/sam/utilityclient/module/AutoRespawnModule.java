package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

public final class AutoRespawnModule extends Module {
    public AutoRespawnModule() { super("Auto Respawn", "Immediately requests respawn after death.", ModuleCategory.PLAYER); }
    @Override public void tick(MinecraftClient client) {
        if (client.player != null && client.player.isDead()) client.player.requestRespawn();
    }
}
