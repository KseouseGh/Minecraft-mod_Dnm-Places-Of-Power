package net.Kseouse.dnmplacesofpowermod.capability;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class PlacesOfPowerCapability implements IPlacesOfPowerCapability {
    private final List<PlaceOfPowerEntry> placesOfPower = new ArrayList<>();

    @Override
    public void addPlaceOfPower(BlockPos pos, String name, ResourceKey<Level> dimension) {
        for (PlaceOfPowerEntry entry : placesOfPower) {
            if (entry.pos.equals(pos) && entry.dimension.equals(dimension)) {
                return;
            }
        }
        placesOfPower.add(new PlaceOfPowerEntry(pos, name, dimension));
    }

    @Override
    public List<PlaceOfPowerEntry> getPlacesOfPower(ResourceKey<Level> dimension) {
        List<PlaceOfPowerEntry> filtered = new ArrayList<>();
        for (PlaceOfPowerEntry entry : placesOfPower) {
            if (entry.dimension.equals(dimension)) {
                filtered.add(entry);
            }
        }
        return filtered;
    }
}