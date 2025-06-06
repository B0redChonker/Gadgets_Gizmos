package org.b0redchungus.gadgets;

import net.fabricmc.api.ModInitializer;
import org.b0redchungus.gadgets.item.DigitalWatch;
import org.b0redchungus.gadgets.item.GPS;
import org.b0redchungus.gadgets.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GadgetsGizmos implements ModInitializer {
	public static final String MOD_ID = "gadgets";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		DigitalWatch.checkTime();
		GPS.checkPosition();
	}
}
