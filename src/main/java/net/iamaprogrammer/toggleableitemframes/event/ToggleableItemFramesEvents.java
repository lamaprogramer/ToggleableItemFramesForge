package net.iamaprogrammer.toggleableitemframes.event;

import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.iamaprogrammer.toggleableitemframes.util.IItemFrameMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

public class ToggleableItemFramesEvents {
    @Mod.EventBusSubscriber(modid = ToggleableItemFrames.MODID)
    public static class ForgeEvents {
        static ServerLevel world;
        static ArrayList<ItemFrame> entities;
        static IItemFrameMixin invisibleFrame;
        static ArrayList<ItemFrame> invisibleFrames = new ArrayList<>();

        @SubscribeEvent
        public static void onUseEntity(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof ItemFrame && event.getEntity() != null && !event.getSide().isClient()) {
                if (event.getEntity().getMainHandItem().getItem() == Items.AIR && event.getEntity().isCrouching() && event.getHand() == InteractionHand.MAIN_HAND) {
                    IItemFrameMixin frame = (IItemFrameMixin)event.getTarget();
                    frame.setCurrentlyInvisible(!frame.getCurrentlyInvisible());
                    //event.getEntity().sendSystemMessage(Component.literal(String.valueOf(event.getTarget().isInvisible())));

                    event.getTarget().setInvisible(!event.getTarget().isInvisible());

                    ((ItemFrame) event.getTarget()).setRotation(((ItemFrame) event.getTarget()).getRotation() - 1);
                    event.getTarget().playSound(((ItemFrame) event.getTarget()).getRotateItemSound());
                }
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                world = player.getLevel();
                entities = (ArrayList<ItemFrame>) world.getEntities(EntityTypeTest.forClass(ItemFrame.class), new AABB(player.getX() - 10, player.getY() - 10, player.getZ() - 10, player.getX() + 10, player.getY() + 10, player.getZ() + 10), EntitySelector.ENTITY_STILL_ALIVE);
                for (ItemFrame frame : entities) {
                    invisibleFrame = (IItemFrameMixin) frame;
                    if (invisibleFrame.getCurrentlyInvisible()) {
                        if ((player.isHolding(Items.ITEM_FRAME) || player.isHolding(Items.GLOW_ITEM_FRAME)) && Math.abs(player.getX() - frame.getX()) <= 9
                                && Math.abs(player.getY() - frame.getY()) <= 9
                                && Math.abs(player.getZ() - frame.getZ()) <= 9) {
                            frame.setInvisible(false);
                            invisibleFrames.add(frame);
                        } else {
                            frame.setInvisible(true);
                        }
                    }
                }
                entities.clear();
                invisibleFrames.clear();
            }
        }
    }
}
