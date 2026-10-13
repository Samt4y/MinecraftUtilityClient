package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;

public final class AimAssistModule extends Module {
    private double range = 4.5;
    public AimAssistModule() { super("Aim Assist", "Aims at the nearest target while attacking.", ModuleCategory.COMBAT); }
    public double range() { return range; }
    public void setRange(double value) { range = Math.clamp(value, 2.0, 6.0); }
    @Override public void tick(MinecraftClient client) {
        if (client.currentScreen != null || GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_PRESS) return;
        LivingEntity target = Targeting.closestTarget(client, range);
        if (target != null) Targeting.face(client, target);
    }
}
