package org.b0redchungus.gadgets.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.b0redchungus.gadgets.GadgetsGizmos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public class DigitalWatch extends Item {

    private static long lastDamageTime = 0;
    private static final long DAMAGE_INTERVAL = 200;  // 10 seconds (in ticks, 20 ticks = 1 second)

    public DigitalWatch(Settings settings) {
        super(settings); // super(settings.maxDamage(1000)); // Doesn't change the max damage, always 500.
    }

    public static void checkTime() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld) {
                // Loop through all players on the server
                for (ServerPlayerEntity player : world.getPlayers()) {
                    ItemStack currentItem = player.getMainHandStack();

                    if (currentItem.getItem() == ModItems.DIGITAL_WATCH) {
                        if (currentItem.getDamage() < currentItem.getMaxDamage()) {
                            updateTime(world, player);
                            long currentTime = world.getTime();
                            if (currentTime - lastDamageTime >= DAMAGE_INTERVAL) {
                                damageWatch(currentItem);
                                lastDamageTime = currentTime;
                            }
                        } else {
                            player.sendMessage(Text.literal("Your Digital Watch is broken. Please repair it.").formatted(Formatting.RED), true);
                        }
                    }
                }
            }
        });
    }

    private static void damageWatch(ItemStack currentItem) {
        int currentDamage = currentItem.getDamage();
        int maxDamage = currentItem.getMaxDamage();

        if (currentDamage < maxDamage) {
            currentItem.setDamage(currentDamage + 1);
            GadgetsGizmos.LOGGER.debug("Digital Watch durability reduced to {}", currentItem.getDamage());
        }
    }

    private static void updateTime(ServerWorld world, ServerPlayerEntity player) {
        long timeOfDay = world.getTimeOfDay();

        int hours = (int) ((timeOfDay / 1000) + 6) % 24;
        int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);

        // Format the time as hh:mm
        String timeText = String.format("%02d:%02d", hours, minutes);

        player.sendMessage(Text.literal("Time: " + timeText).formatted(Formatting.GREEN), true);
        GadgetsGizmos.LOGGER.info("Time was checked with a Digital Watch: {}", timeText);
    }
}
