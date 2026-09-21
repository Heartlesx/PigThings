package com.liaoliao.flighthelmet;

import com.liaoliao.flighthelmet.client.PigThingsKeys;
import com.liaoliao.flighthelmet.network.ModNetwork;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = FlightHelmetMod.MOD_ID, name = "PigThings", version = FlightHelmetMod.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)", dependencies = "required-after:baubles")
public final class FlightHelmetMod {
    public static final String MOD_ID = "pigthings";
    /** 与 gradle.properties 的 mod_version 保持一致：模组列表显示的版本号来自这里的 @Mod 注解。 */
    public static final String VERSION = "1.7.6";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Item FLIGHT_HELMET;
    public static Item NICE_RING;
    public static Item NICE_PICKAXE;
    public static Item PIG_INGOT;
    public static Item CARROT_SABER;

    public static int SEARCH_RANGE_X = 32;
    public static int SEARCH_RANGE_Y = 32;
    public static int SEARCH_RANGE_Z = 32;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();
        SEARCH_RANGE_X = configuration.getInt("range_x", "container_search", 32, 1, 64,
                "Container search range along the X axis from the player's feet.");
        SEARCH_RANGE_Y = configuration.getInt("range_y", "container_search", 32, 1, 64,
                "Container search range along the Y axis from the player's feet.");
        SEARCH_RANGE_Z = configuration.getInt("range_z", "container_search", 32, 1, 64,
                "Container search range along the Z axis from the player's feet.");
        configuration.save();

        ModNetwork.register();

        if (event.getSide().isClient()) {
            PigThingsKeys.register();
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static final class Registration {
        private Registration() {
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            // 与原版一致：耐久与附魔能力取钻石，头盔护甲值 10，贴图沿用钻石材质。
            ItemArmor.ArmorMaterial material = net.minecraftforge.common.util.EnumHelper.addArmorMaterial(
                    "PIGTHINGS_HELMET", "diamond", 33, new int[]{0, 0, 0, 10}, 10,
                    net.minecraft.init.SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
            FLIGHT_HELMET = new FlightHelmetItem(material);
            NICE_RING = new NiceRingItem();
            NICE_PICKAXE = new NicePickaxeItem();
            // 猪咪锭：普通材料，默认堆叠 64，放进「材料」创造标签页。
            PIG_INGOT = new Item()
                    .setRegistryName(MOD_ID, "pig_ingot")
                    .setTranslationKey(MOD_ID + ".pig_ingot")
                    .setCreativeTab(CreativeTabs.MATERIALS);
            CARROT_SABER = new net.minecraft.item.ItemSword(Item.ToolMaterial.DIAMOND)
                    .setRegistryName(MOD_ID, "carrot_saber")
                    .setTranslationKey(MOD_ID + ".carrot_saber")
                    .setCreativeTab(CreativeTabs.COMBAT);
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(FLIGHT_HELMET, NICE_RING, NICE_PICKAXE, PIG_INGOT, CARROT_SABER);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Side.CLIENT)
    public static final class ClientRegistration {
        private ClientRegistration() {
        }

        @SubscribeEvent
        public static void registerModels(net.minecraftforge.client.event.ModelRegistryEvent event) {
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(CARROT_SABER, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(MOD_ID + ":carrot_saber", "inventory"));
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(FLIGHT_HELMET, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(MOD_ID + ":nice_helmet", "inventory"));
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(NICE_RING, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(MOD_ID + ":nice_ring", "inventory"));
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(NICE_PICKAXE, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(MOD_ID + ":nice_pickaxe", "inventory"));
            net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(PIG_INGOT, 0,
                    new net.minecraft.client.renderer.block.model.ModelResourceLocation(MOD_ID + ":pig_ingot", "inventory"));
        }
    }
}
