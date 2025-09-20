package net.Kseouse.dnmplacesofpowermod.client;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.Kseouse.dnmplacesofpowermod.ModMessages;
import net.Kseouse.dnmplacesofpowermod.network.NamePlaceOfPowerPacket;
import net.Kseouse.dnmplacesofpowermod.PlaceOfPowerMenu;
import net.minecraft.world.entity.player.Inventory;

public class PlaceOfPowerScreen extends Screen implements MenuAccess<PlaceOfPowerMenu> {
    private final PlaceOfPowerMenu menu;
    private EditBox nameField;

    public PlaceOfPowerScreen(PlaceOfPowerMenu menu, Inventory inventory, Component title) {
        super(title);
        this.menu = menu;
    }

    @Override
    public PlaceOfPowerMenu getMenu() {
        return menu;
    }

    @Override
    protected void init() {
        super.init();
        int startX = (this.width - 160) / 2;
        int startY = (this.height - 80) / 2;
        this.nameField = new EditBox(this.font, startX, startY + 20, 160, 20, Component.literal("Name"));
        this.nameField.setValue(this.menu.getName() != null ? this.menu.getName() : "");
        this.nameField.setMaxLength(32);
        this.addRenderableWidget(this.nameField);
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            ModMessages.INSTANCE.sendToServer(new NamePlaceOfPowerPacket(this.menu.getPos(), this.nameField.getValue()));
            this.minecraft.setScreen(null);
        }).bounds(startX, startY + 50, 160, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}