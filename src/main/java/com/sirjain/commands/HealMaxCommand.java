package com.sirjain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sirjain.EzHealingMain;
import com.sirjain.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

public class HealMaxCommand {

	// Helper method to register command.
	public static void register(
		CommandDispatcher<ServerCommandSource> dispatcher,
		CommandRegistryAccess commandRegistryAccess,
		CommandManager.RegistrationEnvironment registrationEnvironment
	) {
		dispatcher
			.register(CommandManager.literal("heal")
			.requires((source) -> source.hasPermissionLevel(2) && !Objects.requireNonNull(source.getEntity()).isSpectator())
			.then(CommandManager.argument("target", EntityArgumentType.entity())
			.executes((context) -> healMax(context, EntityArgumentType.getEntity(context, "target")))));
	}

	// Runs the heal command with custom value command.
	private static int healMax(CommandContext<ServerCommandSource> context, Entity target) throws CommandSyntaxException {
		if (!(target instanceof LivingEntity entity))
			return -1;

		float health = entity.getHealth();
		float maxHealth = entity.getMaxHealth();

		if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getAbilities().creativeMode && entity.isSpectator()) {
			EzHealingMain.sendMessage(context, true, "commands.heal.failure", false);
			return -1;
		}

		// Check: Is player already at max health?
		if (health == maxHealth) {
			EzHealingMain.sendMessage(context, false, "commands.heal.maxhealth", false);
		}

		// If not, heal to max health
		else {
			float difference = maxHealth - health;
			entity.heal(difference);
			EzHealingMain.sendMessage(context, false, "commands.heal.success", true);
		}

		return 1;
	}
}