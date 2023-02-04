package net.iamaprogrammer.toggleableitemframes.event;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.iamaprogrammer.toggleableitemframes.isInvisible.EntityInvisible;
import net.iamaprogrammer.toggleableitemframes.isInvisible.EntityInvisibleProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
        static ArrayList<ItemFrame> invisibleFrames = new ArrayList<>();

        static Level ClientWorld;
        static ArrayList<ItemFrame> ClientEntities;

        static ArrayList<ItemFrame> ClientInvisibleFrames = new ArrayList<>();
        
        

        @SubscribeEvent
        public static void onUseEntity(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof ItemFrame && !event.getEntity().isSpectator() && !event.getSide().isClient() && event.getLevel().getServer() != null && event.getEntity().getMainHandItem().isEmpty() && event.getEntity().isCrouching() && event.getHand().equals(InteractionHand.MAIN_HAND)) {
                if (!event.getLevel().getServer().isSingleplayer()) {

                    event.getTarget().setInvisible(!event.getTarget().isInvisible());
                    event.getTarget().getCapability(EntityInvisibleProvider.ENTITY_INVISIBLE).ifPresent(isCurrentlyInvisible -> {
                        isCurrentlyInvisible.setCurrentlyInvisible(event.getTarget().isInvisible());
                        //event.getEntity().sendSystemMessage(Component.literal("was changed to" + event.getTarget().isInvisible()));
                    });

                    ((ItemFrame) event.getTarget()).setRotation(((ItemFrame) event.getTarget()).getRotation() - 1);
                    if (((ItemFrame) event.getTarget()).getItem() == ItemStack.EMPTY) {
                        event.getTarget().playSound(((ItemFrame) event.getTarget()).getRotateItemSound());
                    }
                    //player.sendMessage(Text.literal("server"));

                } else {
                    event.getTarget().setInvisible(!event.getTarget().isInvisible());
                    event.getTarget().getCapability(EntityInvisibleProvider.ENTITY_INVISIBLE).ifPresent(isCurrentlyInvisible -> {
                        isCurrentlyInvisible.setCurrentlyInvisible(event.getTarget().isInvisible());
                        //event.getEntity().sendSystemMessage(Component.literal("was changed to" + event.getTarget().isInvisible()));
                    });

                    if (((ItemFrame) event.getTarget()).getItem() == ItemStack.EMPTY) {
                        event.getTarget().playSound(((ItemFrame) event.getTarget()).getRotateItemSound());
                    }

                    ((ItemFrame) event.getTarget()).setRotation(((ItemFrame) event.getTarget()).getRotation() - 1);
                    //player.sendMessage(Text.literal("server"));
                }
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (!event.getServer().isSingleplayer()) {
                for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                    world = player.getLevel();
                    entities = (ArrayList<ItemFrame>) world.getEntities(EntityTypeTest.forClass(ItemFrame.class), new AABB(player.getX() - 10, player.getY() - 10, player.getZ() - 10, player.getX() + 10, player.getY() + 10, player.getZ() + 10), EntitySelector.ENTITY_STILL_ALIVE);
                    for (ItemFrame frame : entities) {
                        frame.getCapability(EntityInvisibleProvider.ENTITY_INVISIBLE).ifPresent(isCurrentlyInvisible -> {
                            if (isCurrentlyInvisible.getCurrentlyInvisible()) {
                                if ((player.isHolding(Items.ITEM_FRAME) || player.isHolding(Items.GLOW_ITEM_FRAME)) && Math.abs(player.getX() - frame.getX()) <= 9
                                        && Math.abs(player.getY() - frame.getY()) <= 9
                                        && Math.abs(player.getZ() - frame.getZ()) <= 9) {
                                    frame.setInvisible(false);
                                    invisibleFrames.add(frame);
                                } else {
                                    frame.setInvisible(true);
                                }
                            }
                        });
                    }
                    entities.clear();
                    invisibleFrames.clear();
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof ItemFrame) {
                if(!event.getObject().getCapability(EntityInvisibleProvider.ENTITY_INVISIBLE).isPresent()) {
                    event.addCapability(new ResourceLocation(ToggleableItemFrames.MODID, "properties"), new EntityInvisibleProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(EntityInvisible.class);
        }

    }
}
