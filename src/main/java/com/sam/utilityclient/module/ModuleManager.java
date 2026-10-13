package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void register(Module module) { modules.add(module); }
    public List<Module> all() { return List.copyOf(modules); }

    public <T extends Module> T get(Class<T> type) {
        return modules.stream().filter(type::isInstance).map(type::cast).findFirst().orElseThrow();
    }

    public void tick(MinecraftClient client) {
        for (Module module : modules) if (module.enabled()) module.tick(client);
    }
}
