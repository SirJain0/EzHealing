package com.sirjain.commands.heal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sirjain.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HealMaxCommand {

	// Helper method to register command.
	public static void register(
		CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
		CommandRegistryAccess commandRegistryAccess,
		CommandManager.RegistrationEnvironment registrationEnvironment
	) {
		serverCommandSourceCommandDispatcher
			.register(CommandManager.literal("heal")
			.then(CommandManager.literal("max")
			.executes(HealMaxCommand::healMax)));
	}

	// Runs the heal command with custom value command.
	private static int healMax(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();

		if (player == null)
			return -1;

		if (!((PlayerEntity) player).getAbilities().creativeMode) {
			int fullAmount = (int) (((PlayerEntity) player).getMaxHealth() - ((PlayerEntity) player).getHealth());
			((PlayerEntity) player).heal(fullAmount);
			context.getSource().sendFeedback(() -> Text.literal("Healed to max health!"), true);

			return 1;
		} else {
			context.getSource().sendFeedback(() -> Text.literal("Unable to executre: Only able to heal in survival!").formatted(Formatting.RED), false);
			return -1;
		}
	}
}