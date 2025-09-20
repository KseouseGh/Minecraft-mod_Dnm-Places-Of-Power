package net.Kseouse.dnmplacesofpowermod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceOfPowerBlockEntity extends BlockEntity {
    private String name = "";

    public PlaceOfPowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistry.PLACE_OF_POWER_ENTITY.get(), pos, state);
    }

    public void setPlaceName(String name) {
        this.name = name;
        setChanged();
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return !name.isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Name", name);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.name = tag.getString("Name");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putString("Name", name);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}