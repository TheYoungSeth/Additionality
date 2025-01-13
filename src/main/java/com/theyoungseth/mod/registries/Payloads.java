package com.theyoungseth.mod.registries;

import com.theyoungseth.mod.Additionality;
import com.theyoungseth.mod.items.PortableJukebox;
import com.theyoungseth.mod.network.PickupDropJukeboxPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Additionality.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Payloads {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                PickupDropJukeboxPayload.TYPE,
                PickupDropJukeboxPayload.STREAM_CODEC,
                new MainThreadPayloadHandler<>(
                        PortableJukebox.ClientPayloadHandler::handleDataOnMain
                )
        );
    }
}
