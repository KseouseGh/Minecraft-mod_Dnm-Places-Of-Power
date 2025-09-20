package net.Kseouse.dnmplacesofpowermod;
import net.Kseouse.dnmplacesofpowermod.capability.InvulnerabilityStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import net.Kseouse.dnmplacesofpowermod.client.TeleportationScreen;

public class ServerEventHandler {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == ModRegistry.PLACE_OF_POWER.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getState().getBlock() == ModRegistry.PLACE_OF_POWER.get() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
            if (blockEntity instanceof PlaceOfPowerBlockEntity placeOfPower && !placeOfPower.hasName()) {
                NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Name the Place of Power");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                        return new PlaceOfPowerMenu(windowId, event.getPos(), placeOfPower.getName());
                    }
                }, buf -> {
                    buf.writeBlockPos(event.getPos());
                    buf.writeUtf(placeOfPower.getName());
                });
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(InvulnerabilityStorage.INVULNERABILITY).ifPresent(cap -> {
                if (cap.isInvulnerable()) {
                    event.setCanceled(true);
                }
            });
        }
    }
}