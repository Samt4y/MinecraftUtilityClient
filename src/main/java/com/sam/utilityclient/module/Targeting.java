package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

final class Targeting {
    private Targeting() { }
    static LivingEntity closestTarget(MinecraftClient client, double range) {
        if (client.player == null || client.world == null) return null;
        Box area = client.player.getBoundingBox().expand(range);
        return client.world.getEntitiesByClass(LivingEntity.class, area,
                entity -> entity != client.player && entity.isAlive() && !(entity instanceof PlayerEntity player && player.isSpectator()))
                .stream().min(java.util.Comparator.comparingDouble(client.player::squaredDistanceTo)).orElse(null);
    }
    static void face(MinecraftClient client, LivingEntity target) {
        if (client.player == null) return;
        double dx = target.getX() - client.player.getX(), dz = target.getZ() - client.player.getZ();
        double dy = target.getY() + target.getHeight() * .55 - client.player.getEyeY();
        double flat = Math.sqrt(dx * dx + dz * dz);
        client.player.setYaw((float) (Math.toDegrees(Math.atan2(dz, dx)) - 90));
        client.player.setPitch((float) -Math.toDegrees(Math.atan2(dy, flat)));
    }
}
