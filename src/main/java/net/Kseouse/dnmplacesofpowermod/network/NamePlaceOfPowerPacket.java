package net.Kseouse.dnmplacesofpowermod.network;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.Kseouse.dnmplacesofpowermod.PlaceOfPowerBlockEntity;
import java.util.function.Supplier;

public class NamePlaceOfPowerPacket {
    private final BlockPos pos;
    private final String name;

    public NamePlaceOfPowerPacket(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    public NamePlaceOfPowerPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.name = buf.readUtf(32);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(name, 32);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel level = ctx.get().getSender().serverLevel();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlaceOfPowerBlockEntity placeOfPower) {
                placeOfPower.setPlaceName(name);
                placeOfPower.setChanged();
                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}