package com.liaoliao.flighthelmet;

import com.liaoliao.flighthelmet.network.ModNetwork;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(FlightHelmetMod.MOD_ID)
public final class FlightHelmetMod {
    public static final String MOD_ID = "pigthings";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final ForgeConfigSpec SERVER_CONFIG_SPEC;
    public static final ForgeConfigSpec.IntValue SEARCH_RANGE_X;
    public static final ForgeConfigSpec.IntValue SEARCH_RANGE_Y;
    public static final ForgeConfigSpec.IntValue SEARCH_RANGE_Z;
    public static final RegistryObject<Item> FLIGHT_HELMET;
    public static final RegistryObject<Item> VEIN_MINING_PICKAXE;
    public static final RegistryObject<Item> NICE_RING;
    public static final RegistryObject<Item> NICE_PICKAXE;
    public static final RegistryObject<Item> PIG_INGOT;
    public static final RegistryObject<Item> CARROT_SABER;

    public FlightHelmetMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        modBus.addListener(this::addCreativeTabContents);
        ModNetwork.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SERVER_CONFIG_SPEC);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(FLIGHT_HELMET);
            event.accept(VEIN_MINING_PICKAXE);
            event.accept(NICE_RING);
            event.accept(NICE_PICKAXE);
        } else if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.accept(CARROT_SABER);
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(PIG_INGOT);
        }
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("container_search");
        SEARCH_RANGE_X = builder.comment("Container search range along the X axis from the player's feet.")
                .defineInRange("range_x", 24, 1, 64);
        SEARCH_RANGE_Y = builder.comment("Container search range along the Y axis from the player's feet.")
                .defineInRange("range_y", 24, 1, 64);
        SEARCH_RANGE_Z = builder.comment("Container search range along the Z axis from the player's feet.")
                .defineInRange("range_z", 24, 1, 64);
        builder.pop();
        SERVER_CONFIG_SPEC = builder.build();
        FLIGHT_HELMET = ITEMS.register("nice_helmet", () -> new FlightHelmetItem(new Item.Properties().stacksTo(1)));
        VEIN_MINING_PICKAXE = ITEMS.register("vein_mining_pickaxe",
                () -> new VeinMiningPickaxeItem(new Item.Properties().stacksTo(1)));
        NICE_RING = ITEMS.register("nice_ring", () -> new NiceRingItem(new Item.Properties().stacksTo(1)));
        NICE_PICKAXE = ITEMS.register("nice_pickaxe", () -> new NicePickaxeItem(new Item.Properties().stacksTo(1)));
        PIG_INGOT = ITEMS.register("pig_ingot", () -> new Item(new Item.Properties()));
        CARROT_SABER = ITEMS.register("carrot_saber",
                () -> new net.minecraft.world.item.SwordItem(net.minecraft.world.item.Tiers.DIAMOND, 3, -2.4F, new Item.Properties()));
    }
}
