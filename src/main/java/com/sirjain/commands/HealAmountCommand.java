package com.sirjain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sirjain.EzHealingMain;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

public class HealAmountCommand {

	// Helper method to register command.
	public static void register(
		CommandDispatcher<ServerCommandSource> dispatcher,
		CommandRegistryAccess commandRegistryAccess,
		CommandManager.RegistrationEnvironment registrationEnvironment
	) {
		dispatcher
			.register(CommandManager.literal("heal")
			.requires((source) -> source.hasPermissionLevel(2))
			.then(CommandManager.argument("target", EntityArgumentType.entity())
			.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
			.executes((ctx) -> healAmount(ctx, IntegerArgumentType.getInteger(ctx, "amount"), EntityArgumentType.getEntity(ctx, "target"))))));
	}

	// Runs the heal command with custom value command.
	private static int healAmount(CommandContext<ServerCommandSource> context, int healAmount, Entity target) throws CommandSyntaxException {
		if (target == null)
			return -1;

		LivingEntity entity = ((LivingEntity) target);

		float health = entity.getHealth();
		float maxHealth = entity.getMaxHealth();

		if (entity instanceof PlayerEntity player && (player.getAbilities().creativeMode || player.isSpectator())) {
			EzHealingMain.sendMessage(context, true, "commands.heal.failure", false);
			return -1;
		}

		// Check: Is target fully healed?
		if (health == maxHealth) {
			EzHealingMain.sendMessage(context, false, "commands.heal.maxhealth", false);
		}

		// Check: Is target healing itself beyond his max health? If so, heal to max health only
		else if (health + healAmount > maxHealth) {
			float healNeededForMax = maxHealth - health;
			entity.heal(healNeededForMax);
			EzHealingMain.sendMessage(context, false, "commands.heal.amount.success", true);
		}

		// If everything else is fine, execute
		else {
			entity.heal(healAmount);
			EzHealingMain.sendMessage(context, false, "commands.heal.amount.success", true);
		}

		return 1;
	}
}