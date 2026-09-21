package com.liaoliao.flighthelmet.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public final class PigHatClientExtensions implements IClientItemExtensions {
    @Override
    public Model getGenericArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        return slot == EquipmentSlot.HEAD ? new PigHatArmorModel(original) : original;
    }
}
