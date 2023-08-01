package com.sirjain;

import com.sirjain.commands.HealAmountCommand;
import com.sirjain.commands.HealMaxCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthControlMain implements ModInitializer {
	public static final String MOD_ID = "health_control";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Health Control!");
		CommandRegistrationCallback.EVENT.register(HealMaxCommand::register);
		CommandRegistrationCallback.EVENT.register(HealAmountCommand::register);
	}
}