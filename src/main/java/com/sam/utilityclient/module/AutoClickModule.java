package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Repeats the normal client attack action at a configurable rate. */
public final class AutoClickModule extends Module {
    private int clicksPerSecond = 10;
    private long nextClickNanos;
    private boolean releaseAttackNextTick;

    public AutoClickModule() { super("Auto Click", "Repeats normal attack input while held.", ModuleCategory.COMBAT); }
    public int clicksPerSecond() { return clicksPerSecond; }
    public void setClicksPerSecond(int clicksPerSecond) { this.clicksPerSecond = Math.clamp(clicksPerSecond, 1, 20); }

    @Override
    public void tick(MinecraftClient client) {
        if (releaseAttackNextTick) {
            client.options.attackKey.setPressed(false);
            releaseAttackNextTick = false;
        }
        boolean holdingAttack = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT)
                == GLFW.GLFW_PRESS;
        if (client.player == null || client.currentScreen != null || !holdingAttack) return;
        long now = System.nanoTime();
        if (now >= nextClickNanos) {
            // Pulse the same key binding used by Minecraft's ordinary attack handling.
            client.options.attackKey.setPressed(true);
            releaseAttackNextTick = true;
            nextClickNanos = now + 1_000_000_000L / clicksPerSecond;
        }
    }

    @Override
    protected void onEnable(MinecraftClient client) { nextClickNanos = 0L; }
}
