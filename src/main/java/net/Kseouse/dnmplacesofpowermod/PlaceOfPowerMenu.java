package net.Kseouse.dnmplacesofpowermod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

public class PlaceOfPowerMenu extends AbstractContainerMenu {
    public static final MenuType<PlaceOfPowerMenu> TYPE = IForgeMenuType.create((IContainerFactory<PlaceOfPowerMenu>) (windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        String name = data.readUtf();
        return new PlaceOfPowerMenu(windowId, pos, name);
    });

    private final BlockPos pos;
    private final String name;

    public PlaceOfPowerMenu(int windowId, BlockPos pos, String name) {
        super(TYPE, windowId);
        this.pos = pos;
        this.name = name;
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // Ничего не перемещаем, так как нет слотов
    }
}