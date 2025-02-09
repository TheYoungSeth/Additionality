package com.theyoungseth.mod.network;

import com.theyoungseth.mod.Additionality;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PickupDropJukeboxPayload(int event) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PickupDropJukeboxPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Additionality.MODID, "jukebox_payload"));

    public static final StreamCodec<ByteBuf, PickupDropJukeboxPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PickupDropJukeboxPayload::event,
            PickupDropJukeboxPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
