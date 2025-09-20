package net.Kseouse.dnmplacesofpowermod;
import com.mojang.logging.LogUtils;
import net.Kseouse.dnmplacesofpowermod.capability.CapabilityHandler;
import net.Kseouse.dnmplacesofpowermod.capability.IPlacesOfPowerCapability;
import net.Kseouse.dnmplacesofpowermod.capability.PlacesOfPowerStorage;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.Kseouse.dnmplacesofpowermod.client.PlaceOfPowerScreen;
import net.Kseouse.dnmplacesofpowermod.PlaceOfPowerMenu;
import net.Kseouse.dnmplacesofpowermod.capability.PlacesOfPowerCapability;

@Mod(DnmplacesofpowerMod.MOD_ID)
@Mod.EventBusSubscriber(modid = DnmplacesofpowerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DnmplacesofpowerMod {
    public static final String MOD_ID = "dnmplacesofpowermod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DnmplacesofpowerMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ModRegistry.register(modEventBus);
        ModMessages.register();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
        LOGGER.info("DnmplacesofpowerMod setup complete!");
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            HealingFlaskItem.registerItemProperties();
            MenuScreens.register(ModRegistry.PLACE_OF_POWER_MENU.get(), PlaceOfPowerScreen::new);
        });
    }
}