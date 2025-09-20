package net.Kseouse.dnmplacesofpowermod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import java.util.List;

public class HealingFlaskItem extends Item {
    private static final int MAX_CHARGES = 3;

    public HealingFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        int charges = tag.getInt("Charges");

        if (charges <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (entity instanceof Player player && !level.isClientSide) {

            CompoundTag tag = stack.getOrCreateTag();

            int charges = tag.getInt("Charges");

            if (charges > 0) {// Восстановить 2 HP!

                player.heal(2.0F);// Уменьшить заряд!
                tag.putInt("Charges", charges - 1);// Добавить кулдаун!
                player.getCooldowns().addCooldown(this, 40);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack getDefaultInstance() {

        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt("Charges", MAX_CHARGES);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        int charges = stack.getOrCreateTag().getInt("Charges");
        tooltip.add(Component.literal("Charges: " + charges + "/" + MAX_CHARGES));
    }

    public static void registerItemProperties() {

        ItemProperties.register(
                ModRegistry.HEALING_FLASK.get(),
                ResourceLocation.fromNamespaceAndPath(DnmplacesofpowerMod.MOD_ID, "charges"),
                (stack, level, entity, seed) -> {
                    int charges = stack.getOrCreateTag().getInt("Charges");

                    if (charges == 0) {
                        return 0.0F;
                    } else if (charges == 1) {
                        return 0.1F;
                    } else if (charges == 2) {
                        return 0.2F;
                    } else {
                        return 0.3F;
                    }
                }
        );
    }
}