package com.sam.utilityclient.mixin;

import com.sam.utilityclient.UtilityClient;
import com.sam.utilityclient.module.HitboxesModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void utilityclient$expandedHitbox(CallbackInfoReturnable<Box> cir) {
        Entity self = (Entity) (Object) this;
        HitboxesModule hitboxes = UtilityClient.MODULES.get(HitboxesModule.class);
        if (hitboxes.enabled() && self instanceof LivingEntity && self != MinecraftClient.getInstance().player) {
            cir.setReturnValue(cir.getReturnValue().expand(hitboxes.size()));
        }
    }
}
