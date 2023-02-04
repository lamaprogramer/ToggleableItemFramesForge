package net.iamaprogrammer.toggleableitemframes.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameRenderer.class)
public abstract class ModifyItemFrameRenderer<T extends ItemFrame> extends EntityRenderer<T> {


    protected ModifyItemFrameRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/decoration/ItemFrame;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;isInvisible()Z"))
    private void injected(T p_115076_, float p_115077_, float p_115078_, PoseStack p_115079_, MultiBufferSource p_115080_, int p_115081_, CallbackInfo ci) {
        if (!Minecraft.getInstance().isLocalServer() && Minecraft.getInstance().player != null) {
            if (p_115076_.level.isClientSide() && !(Minecraft.getInstance().player.isHolding(Items.ITEM_FRAME) || Minecraft.getInstance().player.isHolding(Items.GLOW_ITEM_FRAME))) {
                p_115076_.setInvisible(true);
            }
        }
    }

}

