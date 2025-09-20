package net.Kseouse.dnmplacesofpowermod.capability;
import net.Kseouse.dnmplacesofpowermod.DnmplacesofpowerMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnmplacesofpowerMod.MOD_ID)
public class CapabilityHandler {
    public static final ResourceLocation PLACES_OF_POWER_CAP = ResourceLocation.fromNamespaceAndPath(DnmplacesofpowerMod.MOD_ID, "places_of_power");
    public static final ResourceLocation INVULNERABILITY_CAP = ResourceLocation.fromNamespaceAndPath(DnmplacesofpowerMod.MOD_ID, "invulnerability");

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IPlacesOfPowerCapability.class);
        event.register(IInvulnerabilityCapability.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PLACES_OF_POWER_CAP, new ICapabilityProvider() {
                private final IPlacesOfPowerCapability cap = new PlacesOfPowerCapability();
                private final LazyOptional<IPlacesOfPowerCapability> optional = LazyOptional.of(() -> cap);

                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                    return PlacesOfPowerStorage.PLACES_OF_POWER.orEmpty(cap, optional);
                }
            });
            event.addCapability(INVULNERABILITY_CAP, new ICapabilityProvider() {
                private final IInvulnerabilityCapability cap = new InvulnerabilityCapability();
                private final LazyOptional<IInvulnerabilityCapability> optional = LazyOptional.of(() -> cap);

                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                    return InvulnerabilityStorage.INVULNERABILITY.orEmpty(cap, optional);
                }
            });
        }
    }
}