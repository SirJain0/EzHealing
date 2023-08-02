package com.sirjain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sirjain.EzHealingMain;
import com.sirjain.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class HealMaxCommand {

	// Helper method to register command.
	public static void register(
		CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
		CommandRegistryAccess commandRegistryAccess,
		CommandManager.RegistrationEnvironment registrationEnvironment
	) {
		serverCommandSourceCommandDispatcher
			.register(CommandManager.literal("heal")
			.requires((source) -> source.hasPermissionLevel(2))
			.then(CommandManager.literal("max")
			.executes(HealMaxCommand::healMax)));
	}

	// Runs the heal command with custom value command.
	private static int healMax(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
		PlayerEntity player = ((PlayerEntity) playerData);

		if (player == null)
			return -1;

		float health = player.getHealth();
		float maxHealth = player.getMaxHealth();

		if (!player.getAbilities().creativeMode) {

			// Check: Is player already at max health?
			if (health == maxHealth) {
				EzHealingMain.sendMessage(context, false, "commands.heal.maxhealth", false);
			}

			// If not, heal to max health
			else {
				float difference = maxHealth - health;
				player.heal(difference);
				EzHealingMain.sendMessage(context, false, "commands.heal.success", true);
			}

			return 1;
		} else {
			EzHealingMain.sendMessage(context, true, "commands.heal.failure", false);
			return -1;
		}
	}
}