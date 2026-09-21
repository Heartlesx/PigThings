package com.liaoliao.flighthelmet;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/** Preserves the recovered 1.4.5 helmet behavior; adds only the client model hooks. */
public final class FlightHelmetItem extends ArmorItem {
    private static final int PROTECTION_LEVEL = 10;

    public FlightHelmetItem(Item.Properties properties) {
        super(PigThingsArmorMaterial.INSTANCE, Type.HELMET, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) { return true; }

    @Override
    public boolean isDamageable(ItemStack stack) { return false; }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        ensureProtection(stack);
        return stack;
    }

    public static void ensureProtection(ItemStack stack) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, stack) != PROTECTION_LEVEL) {
            stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, PROTECTION_LEVEL);
        }
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new com.liaoliao.flighthelmet.client.PigHatClientExtensions());
    }

    @Override
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
            net.minecraft.world.entity.EquipmentSlot slot, String type) {
        return "pigthings:textures/item/pig_straw_hat.png";
    }
}
