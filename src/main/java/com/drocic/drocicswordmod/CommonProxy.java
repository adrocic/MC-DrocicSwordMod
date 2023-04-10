package com.drocic.drocicswordmod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = "sword")
// this subscribes this class to the event bus during loading of the game to register our sword
public class CommonProxy {
    // now we need to actualy write the code that will do that
    private static boolean itemAdded = false;
    public static boolean hasMinedBlockRecently = false;
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

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();

        // check if the player is holding an item that can mine blocks (e.g. a pickaxe)
        if (player.getHeldItemMainhand().canHarvestBlock(Blocks.STONE.getDefaultState())) {
            System.out.println("DEBUG: player is holding an item that can mine blocks");

            // get the position of the block that was broken
            BlockPos blockPos = event.getPos();
            IBlockState blockState = event.getState();

            // get the coordinates of the 3x3x3 area around the broken block
            int posX = blockPos.getX();
            int posY = blockPos.getY();
            int posZ = blockPos.getZ();
            int range = 3; // range of 3 block in each direction from the broken block

            // destroy blocks in a 3x3x3 area around the broken block
            for (int x = posX - range; x <= posX + range; x++) {
                for (int y = posY - range; y <= posY + range; y++) {
                    for (int z = posZ - range; z <= posZ + range; z++) {
                        BlockPos ourBlockPos = new BlockPos(x, y, z);
                        IBlockState ourBlockState = player.world.getBlockState(ourBlockPos);
                        if (ourBlockState.getBlockHardness(player.world, ourBlockPos) != -1.0f) { // check if the block can be mined
                            System.out.println("Block can be destroyed");
                            player.world.destroyBlock(ourBlockPos, true); // destroy the block instantly
                            System.out.println("Block destroyed");
                        }
                    }
                }
            }
        }
    }


}

