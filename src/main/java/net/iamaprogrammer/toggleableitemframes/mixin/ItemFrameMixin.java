package net.iamaprogrammer.toggleableitemframes.mixin;

import net.iamaprogrammer.toggleableitemframes.util.IItemFrameMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin implements IItemFrameMixin {
    private boolean isCurrentlyInvisible = false;

    @Override
    public boolean getCurrentlyInvisible() {
        return isCurrentlyInvisible;
    }
    @Override
    public void setCurrentlyInvisible(boolean bool) {
        isCurrentlyInvisible = bool;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void addCustomNbt(CompoundTag p_31808_, CallbackInfo ci) {
        p_31808_.putBoolean("isCurrentlyInvisible", isCurrentlyInvisible);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readCustomNbt(CompoundTag p_31795_, CallbackInfo ci) {
        if(p_31795_.contains("isCurrentlyInvisible")) {
            isCurrentlyInvisible = p_31795_.getBoolean("isCurrentlyInvisible");
        }
    }
}
