package org.b0redchungus.gadgets.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.b0redchungus.gadgets.GadgetsGizmos;

import java.util.function.Function;

public class ModItems {

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(GadgetsGizmos.MOD_ID, name), item);
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GadgetsGizmos.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    /*
    public static final Item DIGITAL_WATCH = registerItem("digital_watch",
            new Item(new Item.Settings().sword(ModToolMaterials.Gadget, 1, -2.8f)));
    */

    public static final Item DIGITAL_WATCH = register(
            "digital_watch",
            settings -> new HoeItem(ModToolMaterials.Gadget, 1f, 1f, settings),
            new Item.Settings()
    );

    public static final Item GPS_ITEM = register(
            "gps",
            settings -> new HoeItem(ModToolMaterials.Gadget, 1f, 1f, settings),
            new Item.Settings()
    );

    // public static final Item DIGITAL_WATCH = registerItem("digital_watch", new DigitalWatch(new DigitalWatch.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GadgetsGizmos.MOD_ID,"digital_watch")))));
    // public static final Item GPS_ITEM = registerItem("gps", new GPS(new GPS.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GadgetsGizmos.MOD_ID,"gps")))));
    public static final Item BATTERY = registerItem("battery", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(GadgetsGizmos.MOD_ID,"battery")))));

    /* Failures:
     * public static final Item DIGITAL_WATCH = registerItem("digital_watch", new DigitalWatch(new Item.Settings()));
     * public static final Item DIGITAL_WATCH = registerItem("digital_watch", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Minecraft_mod.MOD_ID,"digital_watch")))));
     * public static final Item DIGITAL_WATCH = registerItem("digital_watch", new DigitalWatch(new Item.Settings().group(ItemGroups.TOOLS)));
     * public static final Item DIGITAL_WATCH = registerItem("digital_watch", new DigitalWatch(new Item.Settings().group(ItemGroup.TOOLS)));
     */

    public static void registerModItems() {
        GadgetsGizmos.LOGGER.info("Registering Mod Items for " + GadgetsGizmos.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(DIGITAL_WATCH);
            entries.add(GPS_ITEM);
            entries.add(BATTERY);
        });
    }
}
