package com.drocic.drocicswordmod;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = DrocicSwordMod.MODID, name = DrocicSwordMod.NAME, version = DrocicSwordMod.VERSION)
public class DrocicSwordMod {
    public static final String MODID = "sword";
    public static final String NAME = "Drocic's Custom Sword";
    public static final String VERSION = "1.0";
    private static Logger logger;
    public static Item mySword;
    public static Item.ToolMaterial myToolmaterial;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        myToolmaterial = EnumHelper.addToolMaterial("Drocic", 4, 100000, 20.0F, 100.0F, 30);

        mySword = new CustomSword();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
