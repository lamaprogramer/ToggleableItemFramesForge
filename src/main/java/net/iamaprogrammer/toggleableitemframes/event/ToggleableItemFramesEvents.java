package net.iamaprogrammer.toggleableitemframes.event;

import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ToggleableItemFramesEvents {
    @Mod.EventBusSubscriber(modid = ToggleableItemFrames.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onUseEntity(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof ItemFrame && event.getEntity() != null && !event.getSide().isClient()) {
                if (event.getEntity().getMainHandItem().getItem() == Items.AIR && event.getEntity().isCrouching() && event.getHand() == InteractionHand.MAIN_HAND) {
                    event.getEntity().sendSystemMessage(Component.literal(String.valueOf(event.getTarget().isInvisible())));

                    event.getTarget().setInvisible(!event.getTarget().isInvisible());

                    ((ItemFrame) event.getTarget()).setRotation(((ItemFrame) event.getTarget()).getRotation() - 1);
                    event.getTarget().playSound(((ItemFrame) event.getTarget()).getRotateItemSound());
                }
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {

        }
    }
}
