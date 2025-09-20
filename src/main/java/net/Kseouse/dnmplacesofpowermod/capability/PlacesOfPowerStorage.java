package net.Kseouse.dnmplacesofpowermod.capability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PlacesOfPowerStorage {
    public static final Capability<IPlacesOfPowerCapability> PLACES_OF_POWER = CapabilityManager.get(new CapabilityToken<>() {});

    public static CompoundTag serializeNBT(IPlacesOfPowerCapability instance) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (IPlacesOfPowerCapability.PlaceOfPowerEntry entry : instance.getPlacesOfPower(null)) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("x", entry.pos.getX());
            entryTag.putInt("y", entry.pos.getY());
            entryTag.putInt("z", entry.pos.getZ());
            entryTag.putString("name", entry.name);
            entryTag.putString("dimension", entry.dimension.location().toString());
            list.add(entryTag);
        }
        tag.put("places", list);
        return tag;
    }

    public static void deserializeNBT(IPlacesOfPowerCapability instance, CompoundTag tag) {
        ListTag list = tag.getList("places", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag entryTag = (CompoundTag) t;
            BlockPos pos = new BlockPos(
                    entryTag.getInt("x"),
                    entryTag.getInt("y"),
                    entryTag.getInt("z")
            );
            String name = entryTag.getString("name");
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(entryTag.getString("dimension").split(":")[0], entryTag.getString("dimension").split(":")[1]));
            instance.addPlaceOfPower(pos, name, dimension);
        }
    }
}