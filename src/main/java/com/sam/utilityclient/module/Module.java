package com.sam.utilityclient.module;

import net.minecraft.client.MinecraftClient;

/** A GUI-toggleable client module. */
public abstract class Module {
    private final String name;
    private final String description;
    private boolean enabled;

    protected Module(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public final String name() { return name; }
    public final String description() { return description; }
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
