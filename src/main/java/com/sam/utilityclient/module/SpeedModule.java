package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class SpeedModule extends Module {
    private double multiplier = 1.5;
    public SpeedModule() { super("Speed", "Boosts ground movement speed.", ModuleCategory.MOVEMENT); }
    public double multiplier() { return multiplier; }
    public void setMultiplier(double value) { multiplier = Math.clamp(value, 1.0, 3.0); }

    @Override public void tick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || !player.isOnGround() || !player.input.hasForwardMovement()) return;
        Vec3d velocity = player.getVelocity();
        player.setVelocity(velocity.x * multiplier, velocity.y, velocity.z * multiplier);
    }
}
