package org.b0redchungus.gadgets.item;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.b0redchungus.gadgets.GadgetsGizmos;

public class GPS extends Item {

    private static long lastDamageTime = 0;
    private static final long DAMAGE_INTERVAL = 100;  // 5 seconds (in ticks, 20 ticks = 1 second)

    public GPS(Settings settings) {
        super(settings);
    }

    public static void checkPosition() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                ClientPlayerEntity player = client.player;
                ItemStack currentItem = player.getMainHandStack();

                if (client.world != null) {
                    long currentTime = client.world.getTime();
                    // If the player is holding the GPS, and it isn't broken, update the position
                    if (currentItem.getItem() == ModItems.GPS_ITEM && currentItem.getDamage() < currentItem.getMaxDamage()) {
                        updatePosition(player);
                        if (currentTime - lastDamageTime >= DAMAGE_INTERVAL) {
                            damageGPS(currentItem);
                            lastDamageTime = currentTime;
                        }
                    } else if (currentItem.getItem() == ModItems.GPS_ITEM && currentItem.getDamage() >= currentItem.getMaxDamage()) {
                        player.sendMessage(Text.literal("Your GPS is broken. Please repair it.").formatted(Formatting.RED), true);
                    }
                }
            }
        });
    }

    private static void damageGPS(ItemStack currentItem) {
        int currentDamage = currentItem.getDamage();
        int maxDamage = currentItem.getMaxDamage();

        if (currentDamage < maxDamage) {
            currentItem.setDamage(currentDamage + 1);
            GadgetsGizmos.LOGGER.debug("GPS durability reduced to {}", currentItem.getDamage());
        }
    }

    private static void updatePosition(ClientPlayerEntity player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Format the coordinates to one decimal place
        String positionText = String.format("X: %.1f, Y: %.1f, Z: %.1f", x, y, z);

        // Send the position message to the player
        player.sendMessage(Text.literal("Position: " + positionText).formatted(Formatting.GREEN), true);

        GadgetsGizmos.LOGGER.info("Position was checked with a GPS: {}", positionText);
    }
}
