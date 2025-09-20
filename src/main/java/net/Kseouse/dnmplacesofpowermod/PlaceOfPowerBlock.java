package net.Kseouse.dnmplacesofpowermod;
import net.Kseouse.dnmplacesofpowermod.capability.PlacesOfPowerStorage;
import net.Kseouse.dnmplacesofpowermod.network.StartTeleportationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

public class PlaceOfPowerBlock extends Block implements EntityBlock {
    public PlaceOfPowerBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1.0F, 3600000.0F).noLootTable());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlaceOfPowerBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlaceOfPowerBlockEntity placeOfPower) {
                System.out.println("Block used at " + pos + ", name: '" + placeOfPower.getName() + "', hasName: " + placeOfPower.hasName());
                // Восстановить HP, снять эффекты, зарядить фляги!
                System.out.println("Restoring health for player: " + player.getName().getString());
                player.setHealth(player.getMaxHealth());
                System.out.println("Removing effects for player: " + player.getName().getString());
                player.removeAllEffects();
                rechargeFlasks(player);// Добавление места силы в Capability!
                player.getCapability(PlacesOfPowerStorage.PLACES_OF_POWER).ifPresent(cap -> {
                    cap.addPlaceOfPower(pos, placeOfPower.getName(), level.dimension());
                });// Запустить анимацию и открыть GUI телепортации
                ModMessages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new StartTeleportationPacket(pos));
                return InteractionResult.CONSUME;
            } else {
                System.out.println("No PlaceOfPowerBlockEntity at " + pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void rechargeFlasks(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModRegistry.HEALING_FLASK.get()) {
                stack.getOrCreateTag().putInt("Charges", 3);
            }
        }
    }
}