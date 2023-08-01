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
			.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
			.executes((ctx) -> healAmount(ctx, IntegerArgumentType.getInteger(ctx, "amount")))));
	}

	// Runs the heal command with custom value command.
	private static int healAmount(CommandContext<ServerCommandSource> context, int amount) throws CommandSyntaxException {
		IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();

		if (player == null)
			return -1;

		if (!((PlayerEntity) player).getAbilities().creativeMode) {
			if (!(((PlayerEntity) player).getHealth() + amount > 20)) {
				((PlayerEntity) player).heal(amount);
				context.getSource().sendFeedback(() -> Text.translatable("commands.heal.amount.success"), true);
			} else {
				int fullAmount = (int) (((PlayerEntity) player).getMaxHealth() - ((PlayerEntity) player).getHealth());
				((PlayerEntity) player).heal(fullAmount);
				context.getSource().sendFeedback(() -> Text.translatable("commands.heal.generic.success"), true);
			}

			return 1;
		} else {
			context.getSource().sendFeedback(() -> Text.translatable("commands.heal.generic.failure").formatted(Formatting.RED), false);
			return -1;
		}
	}
}