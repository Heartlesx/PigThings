package com.liaoliao.flighthelmet;

import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class FlightHelmetItem extends ArmorItem {
    public static final int PROTECTION_LEVEL = 10;

    private static final Holder<ArmorMaterial> MATERIAL = Holder.direct(new ArmorMaterial(
            Map.of(ArmorItem.Type.HELMET, 10),
            10,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            () -> Ingredient.of(Items.DIAMOND),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("diamond"))),
            2.0F,
            0.0F));

    public FlightHelmetItem(Properties properties) {
        super(MATERIAL, Type.HELMET, properties);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
            net.minecraft.world.entity.EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath("pigthings", "textures/item/pig_straw_hat.png");
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    /**
     * Keeps the fixed Protection X enchantment on the stack. Requires a registry access because
     * enchantments are a datapack registry in 1.21 and can no longer be referenced directly.
     */
    public static void ensureProtection(ItemStack stack, RegistryAccess registries) {
        Holder<Enchantment> protection = registries
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.PROTECTION);
        if (EnchantmentHelper.getItemEnchantmentLevel(protection, stack) != PROTECTION_LEVEL) {
            stack.enchant(protection, PROTECTION_LEVEL);
        }
    }
}
