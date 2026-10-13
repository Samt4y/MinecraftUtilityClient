package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Pulses use input while right mouse is held to make repeated placement responsive. */
public final class FastPlaceModule extends Module {
    private boolean releaseNextTick;
    public FastPlaceModule() { super("Fast Place", "Repeats use input while right-click is held.", ModuleCategory.WORLD); }
    @Override public void tick(MinecraftClient client) {
        if (releaseNextTick) { client.options.useKey.setPressed(false); releaseNextTick = false; }
        boolean holdingUse = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if (client.player == null || client.currentScreen != null || !holdingUse) return;
        client.options.useKey.setPressed(true);
        releaseNextTick = true;
    }
}
