package net.Kseouse.dnmplacesofpowermod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DnmplacesofpowerMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    private static BlockPos namingBlockPos = null;

    public static void setNamingBlockPos(BlockPos pos) {
        namingBlockPos = pos;
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof AnvilScreen anvilScreen && namingBlockPos != null) {
            String name = anvilScreen.getMenu().getSlot(0).getItem().getHoverName().getString();
            if (!name.isEmpty()) {
                ModMessages.INSTANCE.sendToServer(new ModMessages.SetPlaceNamePacket(namingBlockPos, name));
            }
            namingBlockPos = null;
        }
    }
}