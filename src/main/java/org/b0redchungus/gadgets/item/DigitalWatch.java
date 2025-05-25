package org.b0redchungus.gadgets.item;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.b0redchungus.gadgets.GadgetsGizmos;
import net.minecraft.client.MinecraftClient;

public class DigitalWatch extends Item {

    private static long lastDamageTime = 0;
    private static final long DAMAGE_INTERVAL = 100;  // 5 seconds (in ticks, 20 ticks = 1 second)

    public DigitalWatch(Settings settings) {
        super(settings);  // super(settings.maxDamage(1000)); // Doesn't change the max damage, always 500.
    }

    public static void checkTime() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                ClientPlayerEntity player = client.player;
                ItemStack currentItem = player.getMainHandStack();

                if (client.world != null) {
                    long currentTime = client.world.getTime();
                    if (currentItem.getItem() == ModItems.DIGITAL_WATCH && currentItem.getDamage() < currentItem.getMaxDamage()) {
                        updateTime(client, player);
                        if (currentTime - lastDamageTime >= DAMAGE_INTERVAL) {
                            damageWatch(currentItem);
                            lastDamageTime = currentTime;
                        }
                    } else if (currentItem.getItem() == ModItems.DIGITAL_WATCH && currentItem.getDamage() >= currentItem.getMaxDamage()) {
                        player.sendMessage(Text.literal("Your Digital Watch is broken. Please repair it.").formatted(Formatting.RED), true);
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

    private static void updateTime(MinecraftClient client, ClientPlayerEntity player) {
        if (client.world != null) {
            long timeOfDay = client.world.getTimeOfDay();

            int hours = (int) ((timeOfDay / 1000) + 6) % 24;
            int minutes = (int) ((timeOfDay % 1000) * 60 / 1000);

            // Format the time as hh:mm
            String timeText = String.format("%02d:%02d", hours, minutes);

            player.sendMessage(Text.literal("Time: " + timeText).formatted(Formatting.GREEN), true);
            GadgetsGizmos.LOGGER.info("Time was checked with a Digital Watch: {}", timeText);
        }
    }
}
