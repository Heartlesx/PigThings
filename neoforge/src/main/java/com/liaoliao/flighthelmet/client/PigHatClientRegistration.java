package com.liaoliao.flighthelmet.client;

import com.liaoliao.flighthelmet.FlightHelmetMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = FlightHelmetMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class PigHatClientRegistration {
    @SubscribeEvent
    public static void register(RegisterClientExtensionsEvent event) {
        event.registerItem(new PigHatClientExtensions(), FlightHelmetMod.FLIGHT_HELMET.get());
    }

    private PigHatClientRegistration() {}
}
