package com.sam.utilityclient.render;

import com.sam.utilityclient.UtilityClient;
import com.sam.utilityclient.module.EspModule;
import com.sam.utilityclient.module.TracersModule;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/** Camera-relative ESP boxes and tracers rendered in Fabric's world render pass. */
public final class EntityOverlayRenderer {
    private EntityOverlayRenderer() { }
    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        EspModule esp = UtilityClient.MODULES.get(EspModule.class);
        TracersModule tracers = UtilityClient.MODULES.get(TracersModule.class);
        if (!esp.enabled() && !tracers.enabled()) return;
        Vec3d camera = client.gameRenderer.getCamera().getCameraPos();
        VertexConsumer vertices = context.consumers().getBuffer(RenderLayers.lines());
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == client.player || !entity.isAlive()) continue;
            double distance = client.player.distanceTo(entity);
            if (esp.enabled() && distance <= esp.range()) box(context, vertices, entity.getBoundingBox().offset(camera.negate()));
            if (tracers.enabled() && distance <= tracers.range()) {
                Vec3d target = new Vec3d(entity.getX(), entity.getY() + entity.getHeight() * .5, entity.getZ()).subtract(camera);
                line(context, vertices, 0, 0, 0, target.x, target.y, target.z, 255, 90, 90);
            }
        }
    }
    private static void box(WorldRenderContext c, VertexConsumer v, Box b) {
        double x1=b.minX,y1=b.minY,z1=b.minZ,x2=b.maxX,y2=b.maxY,z2=b.maxZ;
        line(c,v,x1,y1,z1,x2,y1,z1,80,210,255); line(c,v,x2,y1,z1,x2,y1,z2,80,210,255); line(c,v,x2,y1,z2,x1,y1,z2,80,210,255); line(c,v,x1,y1,z2,x1,y1,z1,80,210,255);
        line(c,v,x1,y2,z1,x2,y2,z1,80,210,255); line(c,v,x2,y2,z1,x2,y2,z2,80,210,255); line(c,v,x2,y2,z2,x1,y2,z2,80,210,255); line(c,v,x1,y2,z2,x1,y2,z1,80,210,255);
        line(c,v,x1,y1,z1,x1,y2,z1,80,210,255); line(c,v,x2,y1,z1,x2,y2,z1,80,210,255); line(c,v,x2,y1,z2,x2,y2,z2,80,210,255); line(c,v,x1,y1,z2,x1,y2,z2,80,210,255);
    }
    private static void line(WorldRenderContext c, VertexConsumer v, double x1,double y1,double z1,double x2,double y2,double z2,int r,int g,int b) {
        v.vertex(c.matrices().peek(),(float)x1,(float)y1,(float)z1).color(r,g,b,255).lineWidth(2f);
        v.vertex(c.matrices().peek(),(float)x2,(float)y2,(float)z2).color(r,g,b,255).lineWidth(2f);
    }
}
