package net.Kseouse.dnmplacesofpowermod.network;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class TeleportPacket {
    private final BlockPos pos;

    public TeleportPacket(BlockPos pos) {
        this.pos = pos;
    }

    public TeleportPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.setPose(Pose.STANDING);
                player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}