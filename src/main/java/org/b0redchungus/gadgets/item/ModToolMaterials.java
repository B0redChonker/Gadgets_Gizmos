package org.b0redchungus.gadgets.item;

import net.minecraft.item.ToolMaterial;
import org.b0redchungus.gadgets.util.ModTags;

public class ModToolMaterials {
    public static final ToolMaterial Gadget = new ToolMaterial(
            ModTags.Blocks.NO_BLOCK_DROPS,
            455,
            5.0F,
            1.5F,
            22,
            ModTags.Items.BATTERY_REPAIR
    );
}
