package net.iamaprogrammer.toggleableitemframes.isInvisible;

import net.minecraft.nbt.CompoundTag;

public class EntityInvisible {

    private boolean isCurrentlyInvisible;

    public boolean getCurrentlyInvisible() {
        return isCurrentlyInvisible;
    }

    public void setCurrentlyInvisible(boolean setInv) {
        this.isCurrentlyInvisible = setInv;
    }


    public void copyFrom(EntityInvisible source) {
        this.isCurrentlyInvisible = source.isCurrentlyInvisible;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("isCurrentlyInvisible", isCurrentlyInvisible);
    }

    public void loadNBTData(CompoundTag nbt) {
        isCurrentlyInvisible = nbt.getBoolean("isCurrentlyInvisible");
    }
}
