package com.sam.utilityclient.mixin;

import com.sam.utilityclient.UtilityClient;
import com.sam.utilityclient.module.ReachModule;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Applies the Reach module to the local client's crosshair targeting calculations. */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void utilityclient$blockReach(CallbackInfoReturnable<Double> cir) {
        ReachModule reach = UtilityClient.MODULES.get(ReachModule.class);
        if (reach.enabled()) cir.setReturnValue(reach.range());
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void utilityclient$entityReach(CallbackInfoReturnable<Double> cir) {
        ReachModule reach = UtilityClient.MODULES.get(ReachModule.class);
        if (reach.enabled()) cir.setReturnValue(reach.range());
    }
}
