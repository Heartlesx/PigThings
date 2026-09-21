package com.liaoliao.flighthelmet;

import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public final class FlightHelmetItem extends ItemArmor {
    public static final int PROTECTION_LEVEL = 10;

    public FlightHelmetItem(ArmorMaterial material) {
        super(material, 3, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName(FlightHelmetMod.MOD_ID, "nice_helmet");
        this.setTranslationKey(FlightHelmetMod.MOD_ID + ".nice_helmet");
    }

    @Override
    @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(net.minecraft.entity.EntityLivingBase entity,
            ItemStack stack, EntityEquipmentSlot slot, net.minecraft.client.model.ModelBiped original) {
        return slot == EntityEquipmentSlot.HEAD
                ? com.liaoliao.flighthelmet.client.PigHatArmorModel.INSTANCE : original;
    }

    @Override
    public String getArmorTexture(ItemStack stack, net.minecraft.entity.Entity entity,
            EntityEquipmentSlot slot, String type) {
        return "pigthings:textures/item/pig_straw_hat.png";
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ensureProtection(stack);
            ensureUnbreakable(stack);
            items.add(stack);
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        ensureUnbreakable(stack);
    }

    /**
     * 1.12.2 的耐久判定是 ItemStack.isItemStackDamageable()（getMaxDamage(stack) > 0 且没有 Unbreakable 标签），
     * 用 Unbreakable 标签实现原版本的「头盔不可损坏」。
     */
    public static void ensureUnbreakable(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        if (!tag.getBoolean("Unbreakable")) {
            tag.setBoolean("Unbreakable", true);
        }
        if (tag.hasKey("Damage")) {
            tag.removeTag("Damage");
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    /**
     * Keeps the fixed Protection X enchantment on the stack. Unlike 1.21 no registry lookup is
     * needed because enchantments are still plain objects in 1.12.2.
     */
    public static void ensureProtection(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (!Integer.valueOf(PROTECTION_LEVEL).equals(enchantments.get(Enchantments.PROTECTION))) {
            enchantments.put(Enchantments.PROTECTION, PROTECTION_LEVEL);
            EnchantmentHelper.setEnchantments(enchantments, stack);
        }
    }
}
