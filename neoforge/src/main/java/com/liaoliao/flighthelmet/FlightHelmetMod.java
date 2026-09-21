package com.liaoliao.flighthelmet;

import com.liaoliao.flighthelmet.network.ModNetwork;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(FlightHelmetMod.MOD_ID)
public final class FlightHelmetMod {
    public static final String MOD_ID = "pigthings";
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final ModConfigSpec SERVER_CONFIG_SPEC;
    public static final ModConfigSpec.IntValue SEARCH_RANGE_X;
    public static final ModConfigSpec.IntValue SEARCH_RANGE_Y;
    public static final ModConfigSpec.IntValue SEARCH_RANGE_Z;
    public static final DeferredItem<Item> FLIGHT_HELMET;
    public static final DeferredItem<Item> NICE_RING;
    public static final DeferredItem<Item> NICE_PICKAXE;
    public static final DeferredItem<Item> PIG_INGOT;
    public static final DeferredItem<Item> CARROT_SABER;

    public FlightHelmetMod(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreativeTabContents);
        ModNetwork.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, SERVER_CONFIG_SPEC);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(FLIGHT_HELMET);
            event.accept(NICE_RING);
            event.accept(NICE_PICKAXE);
        } else if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.accept(CARROT_SABER);
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(PIG_INGOT);
        }
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("container_search");
        SEARCH_RANGE_X = builder.comment("Container search range along the X axis from the player's feet.")
                .defineInRange("range_x", 24, 1, 64);
        SEARCH_RANGE_Y = builder.comment("Container search range along the Y axis from the player's feet.")
                .defineInRange("range_y", 24, 1, 64);
        SEARCH_RANGE_Z = builder.comment("Container search range along the Z axis from the player's feet.")
                .defineInRange("range_z", 24, 1, 64);
        builder.pop();
        SERVER_CONFIG_SPEC = builder.build();
        FLIGHT_HELMET = ITEMS.register("nice_helmet",
                () -> new FlightHelmetItem(new Item.Properties().stacksTo(1)));
        NICE_RING = ITEMS.register("nice_ring",
                () -> new NiceRingItem(new Item.Properties().stacksTo(1)));
        NICE_PICKAXE = ITEMS.register("nice_pickaxe",
                () -> new NicePickaxeItem(new Item.Properties()
                        .attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 8.0F, 20.0F))
                        .component(DataComponents.UNBREAKABLE, new Unbreakable(true))));
        PIG_INGOT = ITEMS.register("pig_ingot", () -> new Item(new Item.Properties()));
        CARROT_SABER = ITEMS.register("carrot_saber",
                () -> new net.minecraft.world.item.SwordItem(Tiers.DIAMOND, new Item.Properties()
                        .attributes(net.minecraft.world.item.SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F))));
    }
}
