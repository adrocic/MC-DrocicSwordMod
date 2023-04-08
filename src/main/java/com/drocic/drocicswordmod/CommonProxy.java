package com.drocic.drocicswordmod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = "sword") // this subscribes this class to the event bus during loading of the game to register our sword
public class CommonProxy {
    // now we need to actualy write the code that will do that
    private static boolean itemAdded = false;
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(DrocicSwordMod.mySword);
    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(DrocicSwordMod.mySword);
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!itemAdded && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ItemStack itemStack = new ItemStack(DrocicSwordMod.mySword, 2); // Give 2 of the item
            try {
                player.inventory.addItemStackToInventory(itemStack);
                System.out.println("DrocicSwordMod: added item");
                itemAdded = true;
            } catch (RuntimeException e) {
                // Handle the exception here, e.g. display an error message to the user
                System.out.println("DrocicSwordMod: Item couldn't be added");
                e.printStackTrace();
            }
        }
    }
}
