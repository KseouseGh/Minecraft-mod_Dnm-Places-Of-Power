package net.Kseouse.dnmplacesofpowermod;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.BlockItem;

public class ModRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DnmplacesofpowerMod.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DnmplacesofpowerMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DnmplacesofpowerMod.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DnmplacesofpowerMod.MOD_ID);

    public static final RegistryObject<Item> HEALING_FLASK = ITEMS.register("healing_flask",
            () -> new HealingFlaskItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Block> PLACE_OF_POWER = BLOCKS.register("place_of_power",
            () -> new PlaceOfPowerBlock());
    public static final RegistryObject<Item> PLACE_OF_POWER_ITEM = ITEMS.register("place_of_power",
            () -> new BlockItem(PLACE_OF_POWER.get(), new Item.Properties()));
    public static final RegistryObject<BlockEntityType<PlaceOfPowerBlockEntity>> PLACE_OF_POWER_ENTITY = BLOCK_ENTITIES.register("place_of_power",
            () -> BlockEntityType.Builder.of(PlaceOfPowerBlockEntity::new, PLACE_OF_POWER.get()).build(null));
    public static final RegistryObject<MenuType<PlaceOfPowerMenu>> PLACE_OF_POWER_MENU = MENUS.register("place_of_power_menu",
            () -> PlaceOfPowerMenu.TYPE);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENUS.register(eventBus);
    }
}