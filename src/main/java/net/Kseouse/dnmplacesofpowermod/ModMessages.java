package net.Kseouse.dnmplacesofpowermod;
import net.Kseouse.dnmplacesofpowermod.network.NamePlaceOfPowerPacket;
import net.Kseouse.dnmplacesofpowermod.network.StartTeleportationPacket;
import net.Kseouse.dnmplacesofpowermod.network.TeleportPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.Supplier;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(DnmplacesofpowerMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(packetId++, SetPlaceNamePacket.class, SetPlaceNamePacket::encode, SetPlaceNamePacket::decode, SetPlaceNamePacket::handle);
        INSTANCE.messageBuilder(NamePlaceOfPowerPacket.class, packetId++)
                .decoder(NamePlaceOfPowerPacket::new)
                .encoder(NamePlaceOfPowerPacket::toBytes)
                .consumerMainThread(NamePlaceOfPowerPacket::handle)
                .add();
        INSTANCE.registerMessage(packetId++, StartTeleportationPacket.class,
                StartTeleportationPacket::toBytes,
                StartTeleportationPacket::new,
                StartTeleportationPacket::handle);
        INSTANCE.registerMessage(packetId++, TeleportPacket.class,
                TeleportPacket::toBytes,
                TeleportPacket::new,
                TeleportPacket::handle);
    }

    public static class SetPlaceNamePacket {
        private final BlockPos pos;
        private final String name;

        public SetPlaceNamePacket(BlockPos pos, String name) {
            this.pos = pos;
            this.name = name;
        }

        public static void encode(SetPlaceNamePacket msg, FriendlyByteBuf buf) {
            buf.writeBlockPos(msg.pos);
            buf.writeUtf(msg.name, 32);
        }

        public static SetPlaceNamePacket decode(FriendlyByteBuf buf) {
            return new SetPlaceNamePacket(buf.readBlockPos(), buf.readUtf(32));
        }

        public static void handle(SetPlaceNamePacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerLevel level = ctx.get().getSender().serverLevel();
                BlockEntity blockEntity = level.getBlockEntity(msg.pos);
                if (blockEntity instanceof PlaceOfPowerBlockEntity placeOfPower) {
                    placeOfPower.setPlaceName(msg.name);
                    placeOfPower.setChanged();
                    level.sendBlockUpdated(msg.pos, level.getBlockState(msg.pos), level.getBlockState(msg.pos), 3);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}