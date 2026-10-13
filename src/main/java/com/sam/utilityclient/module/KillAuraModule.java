package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public final class KillAuraModule extends Module {
    private double range = 4.0;
    public KillAuraModule() { super("Kill Aura", "Attacks the closest nearby target.", ModuleCategory.COMBAT); }
    public double range() { return range; }
    public void setRange(double value) { range = Math.clamp(value, 2.0, 6.0); }
    @Override public void tick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null || client.player.getAttackCooldownProgress(0) < .92f) return;
        LivingEntity target = Targeting.closestTarget(client, range);
        if (target != null) { Targeting.face(client, target); client.interactionManager.attackEntity(client.player, target); client.player.swingHand(Hand.MAIN_HAND); }
    }
}
