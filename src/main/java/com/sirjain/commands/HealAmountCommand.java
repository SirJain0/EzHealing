package com.sirjain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sirjain.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HealAmountCommand {

	// Helper method to register command.
	public static void register(
		CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
		CommandRegistryAccess commandRegistryAccess,
		CommandManager.RegistrationEnvironment registrationEnvironment
	) {
		serverCommandSourceCommandDispatcher
			.register(CommandManager.literal("heal")
			.requires((source) -> source.hasPermissionLevel(2))
			.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
			.executes((ctx) -> healAmount(ctx, IntegerArgumentType.getInteger(ctx, "amount")))));
	}

	// Runs the heal command with custom value command.
	private static int healAmount(CommandContext<ServerCommandSource> context, int healAmount) throws CommandSyntaxException {
		IEntityDataSaver playerData = (IEntityDataSaver) context.getSource().getPlayer();
		PlayerEntity player = ((PlayerEntity) playerData);

		if (player == null)
			return -1;

		float health = player.getHealth();
		float maxHealth = player.getMaxHealth();

		if (!player.getAbilities().creativeMode) {

			// Check: Is player fully healed?
			if (health == maxHealth) {
				sendMessage(context, false, "commands.heal.maxhealth", false);
			}

			// Check: Is player healing itself beyond his max health? If so, heal to max health only
			else if (health + healAmount > maxHealth) {
				float healNeededForMax = maxHealth - health;
				player.heal(healNeededForMax);
				sendMessage(context, false, "commands.heal.amount.success", true);
			}

			// If everything else is fine, execute
			else {
				player.heal(healAmount);
				sendMessage(context, false, "commands.heal.amount.success", true);
			}

			return 1;
		} else {
			sendMessage(context, true, "commands.heal.failure", false);
			return -1;
		}
	}

	public static void sendMessage(CommandContext<ServerCommandSource> context, boolean error, String key, boolean broadcast) {
		context.getSource().sendFeedback(() -> error
			? Text.translatable(key).formatted(Formatting.RED)
			: Text.translatable(key),
			broadcast
		);
	}
}