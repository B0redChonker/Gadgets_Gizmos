package org.b0redchungus.gadgets.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.b0redchungus.gadgets.GadgetsGizmos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public class GPS extends Item {

    private static long lastDamageTime = 0;
    private static final long DAMAGE_INTERVAL = 200;  // 10 seconds (in ticks, 20 ticks = 1 second)

    public GPS(Settings settings) {
        super(settings);
    }

    public static void checkPosition() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld) {
                // Loop through all players on the server
                for (ServerPlayerEntity player : world.getPlayers()) {
                    ItemStack currentItem = player.getMainHandStack();
                    // If the player is holding the GPS, and is not broken, update the position
                    if (currentItem.getItem() == ModItems.GPS_ITEM) {
                        if (currentItem.getDamage() < currentItem.getMaxDamage()) {
                            updatePosition(player);
                            long currentTime = world.getTime();
                            if (currentTime - lastDamageTime >= DAMAGE_INTERVAL) {
                                damageGPS(currentItem);
                                lastDamageTime = currentTime;
                            }
                        } else {
                            player.sendMessage(Text.literal("Your GPS is broken. Please repair it.").formatted(Formatting.RED), true);
                        }
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

    private static void updatePosition(ServerPlayerEntity player) {
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
