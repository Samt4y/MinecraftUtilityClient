package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/** Client movement flight. Multiplayer servers may still correct unapproved movement. */
public final class FlyModule extends Module {
    private double speed = 1.0;

    public FlyModule() { super("Fly", "Directional free-flight with vertical controls.", ModuleCategory.MOVEMENT); }
    public double speed() { return speed; }
    public void setSpeed(double speed) { this.speed = Math.clamp(speed, 0.1, 5.0); }

    @Override
    public void tick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        player.getAbilities().flying = true;
        player.getAbilities().setFlySpeed((float) (0.05 * speed));
        player.setNoGravity(true);
        double forward = (client.options.forwardKey.isPressed() ? 1 : 0) - (client.options.backKey.isPressed() ? 1 : 0);
        double sideways = (client.options.rightKey.isPressed() ? 1 : 0) - (client.options.leftKey.isPressed() ? 1 : 0);
        double yaw = Math.toRadians(player.getYaw());
        double length = Math.max(1.0, Math.sqrt(forward * forward + sideways * sideways));
        double horizontalSpeed = 0.18 * speed;
        // Minecraft yaw 0 faces south, therefore right movement points toward negative X.
        double x = (-Math.sin(yaw) * forward - Math.cos(yaw) * sideways) / length * horizontalSpeed;
        double z = (Math.cos(yaw) * forward - Math.sin(yaw) * sideways) / length * horizontalSpeed;
        double y = ((client.options.jumpKey.isPressed() ? 1 : 0) - (client.options.sneakKey.isPressed() ? 1 : 0)) * horizontalSpeed;
        player.setVelocity(new Vec3d(x, y, z));
    }

    @Override
    protected void onDisable(MinecraftClient client) {
        if (client.player != null) {
            client.player.setNoGravity(false);
            client.player.getAbilities().flying = false;
            client.player.getAbilities().setFlySpeed(0.05f);
        }
    }
}
