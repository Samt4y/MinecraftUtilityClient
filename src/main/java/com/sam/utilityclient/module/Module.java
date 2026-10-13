package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

/** A GUI-toggleable client module. */
public abstract class Module {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;

    protected Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final String name() { return name; }
    public final String description() { return description; }
    public final ModuleCategory category() { return category; }
    public final boolean enabled() { return enabled; }

    public final void toggle(MinecraftClient client) {
        setEnabled(client, !enabled);
    }

    public final void setEnabled(MinecraftClient client, boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(client); else onDisable(client);
    }

    protected void onEnable(MinecraftClient client) { }
    protected void onDisable(MinecraftClient client) { }
    public void tick(MinecraftClient client) { }
}
