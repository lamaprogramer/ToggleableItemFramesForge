package net.iamaprogrammer.toggleableitemframes.isInvisible;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityInvisibleProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<EntityInvisible> ENTITY_INVISIBLE = CapabilityManager.get(new CapabilityToken<EntityInvisible>() { });

    private EntityInvisible isCurrentlyInvisible = null;
    private final LazyOptional<EntityInvisible> optional = LazyOptional.of(this::createEntityInvisible);

    private EntityInvisible createEntityInvisible() {
        if(this.isCurrentlyInvisible == null) {
            this.isCurrentlyInvisible = new EntityInvisible();
        }

        return this.isCurrentlyInvisible;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ENTITY_INVISIBLE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEntityInvisible().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createEntityInvisible().loadNBTData(nbt);
    }
}
