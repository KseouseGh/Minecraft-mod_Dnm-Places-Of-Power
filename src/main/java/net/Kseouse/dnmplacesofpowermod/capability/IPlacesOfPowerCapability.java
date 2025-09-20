package net.Kseouse.dnmplacesofpowermod.capability;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import java.util.List;

public interface IPlacesOfPowerCapability {
    void addPlaceOfPower(BlockPos pos, String name, ResourceKey<Level> dimension);

    List<PlaceOfPowerEntry> getPlacesOfPower(ResourceKey<Level> dimension);

    class PlaceOfPowerEntry {
        public final BlockPos pos;
        public final String name;
        public final ResourceKey<Level> dimension;

        public PlaceOfPowerEntry(BlockPos pos, String name, ResourceKey<Level> dimension) {
            this.pos = pos;
            this.name = name;
            this.dimension = dimension;
        }
    }
}