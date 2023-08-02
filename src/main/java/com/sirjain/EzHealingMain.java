package com.sirjain;

import com.mojang.brigadier.context.CommandContext;
import com.sirjain.commands.HealAmountCommand;
import com.sirjain.commands.HealMaxCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EzHealingMain implements ModInitializer {
	public static final String MOD_ID = "health_control";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Health Control!");
		registerCommands();
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register(HealMaxCommand::register);
		CommandRegistrationCallback.EVENT.register(HealAmountCommand::register);
	}

	// Helper method to send chat messages
	public static void sendMessage(CommandContext<ServerCommandSource> context, boolean error, String key, boolean broadcast) {
		context.getSource().sendFeedback(error
			? Text.translatable(key).formatted(Formatting.RED)
			: Text.translatable(key),
			broadcast
		);
	}
}