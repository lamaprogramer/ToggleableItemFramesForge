package net.iamaprogrammer.toggleableitemframes.event;

import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.iamaprogrammer.toggleableitemframes.isInvisible.EntityInvisibleProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ToggleableItemFrames.MODID, value = Dist.CLIENT)
public class ToggleableItemFramesEventsClient {
    static Level ClientWorld;
    static ArrayList<ItemFrame> ClientEntities;

    static ArrayList<ItemFrame> ClientInvisibleFrames = new ArrayList<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent.PlayerTickEvent event) {
        ClientWorld = event.player.getLevel();

        //event.player.sendSystemMessage(Component.literal("start"));
        //event.player.sendSystemMessage(Component.literal("stage1"));
        ClientEntities = (ArrayList<ItemFrame>) ClientWorld.getEntities(EntityTypeTest.forClass(ItemFrame.class), new AABB(event.player.getX() - 10, event.player.getY() - 10, event.player.getZ() - 10, event.player.getX() + 10, event.player.getY() + 10, event.player.getZ() + 10), EntitySelector.ENTITY_STILL_ALIVE);
        //event.player.sendSystemMessage(Component.literal(String.valueOf(ClientEntities.size())));
        for (ItemFrame frame : ClientEntities) {
            frame.getCapability(EntityInvisibleProvider.ENTITY_INVISIBLE).ifPresent(isCurrentlyInvisible -> {
                if (isCurrentlyInvisible.getCurrentlyInvisible() || !Minecraft.getInstance().isLocalServer()) {
                    //event.player.sendSystemMessage(Component.literal("stage2"));
                    if ((event.player.isHolding(Items.ITEM_FRAME) || event.player.isHolding(Items.GLOW_ITEM_FRAME)) && Math.abs(event.player.getX() - frame.getX()) <= 9
                            && Math.abs(event.player.getY() - frame.getY()) <= 9
                            && Math.abs(event.player.getZ() - frame.getZ()) <= 9) {
                        //event.player.sendSystemMessage(Component.literal("stage3"));
                        frame.setInvisible(false);
                        ClientInvisibleFrames.add(frame);
                    } else {
                        frame.setInvisible(true);
                    }
                }
            });
        }
        ClientEntities.clear();
        ClientInvisibleFrames.clear();
    }
}