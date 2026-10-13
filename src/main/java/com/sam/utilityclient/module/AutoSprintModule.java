package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

public final class AutoSprintModule extends Module {
    public AutoSprintModule() { super("Auto Sprint", "Sprints whenever you move forward.", ModuleCategory.MOVEMENT); }
    @Override public void tick(MinecraftClient client) {
        if (client.player != null && client.player.input.hasForwardMovement() && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }
    }
}
