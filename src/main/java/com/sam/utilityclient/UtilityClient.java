package com.sam.utilityclient;

import com.sam.utilityclient.gui.ClickGuiScreen;
import com.sam.utilityclient.module.AutoClickModule;
import com.sam.utilityclient.module.FlyModule;
import com.sam.utilityclient.module.ModuleManager;
import com.sam.utilityclient.module.ReachModule;
import com.sam.utilityclient.module.SpeedModule;
import com.sam.utilityclient.module.AutoSprintModule;
import com.sam.utilityclient.module.AntiAfkModule;
import com.sam.utilityclient.module.AutoRespawnModule;
import com.sam.utilityclient.module.FastPlaceModule;
import com.sam.utilityclient.module.FullbrightModule;
import com.sam.utilityclient.module.AimAssistModule;
import com.sam.utilityclient.module.KillAuraModule;
import com.sam.utilityclient.module.HitboxesModule;
import com.sam.utilityclient.module.EspModule;
import com.sam.utilityclient.module.TracersModule;
import com.sam.utilityclient.render.EntityOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/** Client entrypoint and the single owner of module lifecycle events. */
public final class UtilityClient implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        MODULES.register(new FlyModule());
        MODULES.register(new SpeedModule());
        MODULES.register(new AutoSprintModule());
        MODULES.register(new ReachModule());
        MODULES.register(new AutoClickModule());
        MODULES.register(new AimAssistModule());
        MODULES.register(new KillAuraModule());
        MODULES.register(new HitboxesModule());
        MODULES.register(new FastPlaceModule());
        MODULES.register(new AutoRespawnModule());
        MODULES.register(new AntiAfkModule());
        MODULES.register(new FullbrightModule());
        MODULES.register(new EspModule());
        MODULES.register(new TracersModule());

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.utilityclient.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                Category.create(Identifier.of("utilityclient", "general"))));

        ClientTickEvents.END_CLIENT_TICK.register(this::onEndClientTick);
        WorldRenderEvents.AFTER_ENTITIES.register(EntityOverlayRenderer::render);
    }

    private void onEndClientTick(MinecraftClient client) {
        while (openGuiKey.wasPressed()) {
            client.setScreen(new ClickGuiScreen(client.currentScreen));
        }
        MODULES.tick(client);
    }
}
