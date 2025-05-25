package org.b0redchungus.gadgets.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.b0redchungus.gadgets.GadgetsGizmos;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NO_BLOCK_DROPS = createTag("no_block_drops");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(GadgetsGizmos.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BATTERY_REPAIR = createTag("battery_repair");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(GadgetsGizmos.MOD_ID, name));
        }
    }
}
