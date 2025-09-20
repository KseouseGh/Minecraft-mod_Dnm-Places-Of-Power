package net.Kseouse.dnmplacesofpowermod.client;
import net.Kseouse.dnmplacesofpowermod.DnmplacesofpowerMod;
import net.Kseouse.dnmplacesofpowermod.ModMessages;
import net.Kseouse.dnmplacesofpowermod.capability.PlacesOfPowerStorage;
import net.Kseouse.dnmplacesofpowermod.capability.IPlacesOfPowerCapability;
import net.Kseouse.dnmplacesofpowermod.network.TeleportPacket;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.List;
import net.Kseouse.dnmplacesofpowermod.capability.InvulnerabilityStorage;
import org.joml.Quaternionf;

public class TeleportationScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DnmplacesofpowerMod.MOD_ID, "textures/gui/place_of_power_menu.png");
    private final BlockPos blockPos;

    public TeleportationScreen(BlockPos blockPos) {
        super(Component.literal("Teleport to Place of Power"));
        this.blockPos = blockPos;
    }

    @Override
    protected void init() {
        super.init();
        Player player = this.minecraft.player;

        if (player != null) {
            player.getCapability(PlacesOfPowerStorage.PLACES_OF_POWER).ifPresent(cap -> {
                List<IPlacesOfPowerCapability.PlaceOfPowerEntry> places = cap.getPlacesOfPower(player.level().dimension());
                int startY = (this.height - places.size() * 20) / 2;

                for (int i = 0; i < places.size(); i++) {
                    IPlacesOfPowerCapability.PlaceOfPowerEntry entry = places.get(i);
                    this.addRenderableWidget(Button.builder(
                            Component.literal(entry.name),
                            button -> {
                                ModMessages.INSTANCE.sendToServer(new TeleportPacket(entry.pos));
                                this.minecraft.setScreen(null);
                            }
                    ).bounds(this.width / 2 + 10, startY + i * 20, 100, 20).build());
                }
            });
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        int startX = (this.width - 256) / 2;
        int startY = (this.height - 166) / 2;
        guiGraphics.blit(TEXTURE, startX, startY, 0, 0, 256, 166);
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, startY + 6, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
        if (this.minecraft.player != null) {
            this.minecraft.player.setPose(Pose.STANDING);
            this.minecraft.options.setCameraType(CameraType.FIRST_PERSON);
            this.minecraft.player.getCapability(InvulnerabilityStorage.INVULNERABILITY).ifPresent(cap -> cap.setInvulnerable(false));
        }
    }
}