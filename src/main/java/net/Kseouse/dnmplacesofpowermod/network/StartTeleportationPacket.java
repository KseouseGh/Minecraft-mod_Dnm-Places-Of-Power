package net.Kseouse.dnmplacesofpowermod.network;
import net.Kseouse.dnmplacesofpowermod.capability.InvulnerabilityStorage;
import net.Kseouse.dnmplacesofpowermod.client.TeleportationScreen;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class StartTeleportationPacket {
    private final BlockPos pos;

    public StartTeleportationPacket(BlockPos pos) {
        this.pos = pos;
    }

    public StartTeleportationPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null) {
                player.setPose(Pose.SITTING);
                player.setPos(pos.getX() + 2, pos.getY(), pos.getZ());
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                player.getCapability(InvulnerabilityStorage.INVULNERABILITY).ifPresent(cap -> cap.setInvulnerable(true));
                mc.setScreen(new TeleportationScreen(pos));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}