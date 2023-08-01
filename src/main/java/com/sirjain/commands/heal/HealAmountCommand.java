//package com.sirjain.commands.heal;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.IntegerArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.sirjain.util.IEntityDataSaver;
//import net.minecraft.command.CommandRegistryAccess;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.server.command.CommandManager;
//import net.minecraft.server.command.ServerCommandSource;
//import net.minecraft.text.Text;
//
//public class HealAmountCommand {
//
//	// Helper method to register command.
//	public static void register(
//		CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
//		CommandRegistryAccess commandRegistryAccess,
//		CommandManager.RegistrationEnvironment registrationEnvironment
//	) {
//		serverCommandSourceCommandDispatcher
//			.register(CommandManager.literal("heal")
//			.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
//			.executes((ctx) -> healAmount(, IntegerArgumentType.getInteger(ctx, "amount")))));
//	}
//
//	// Runs the heal command with custom value command.
//	private static int healAmount(CommandContext<ServerCommandSource> context, int amount) throws CommandSyntaxException {
//		IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
//
//		if (player == null)
//			return -1;
//
//		if (!((PlayerEntity) player).getAbilities().creativeMode) {
//			if (!(((PlayerEntity) player).getHealth() + amount > 20)) {
//				((PlayerEntity) player).heal(amount);
//			} else {
//				int fullAmount = (int) (((PlayerEntity) player).getMaxHealth() - ((PlayerEntity) player).getHealth());
//				((PlayerEntity) player).heal(fullAmount);
//			}
//
//			return 1;
//		} else {
//			context.getSource().sendFeedback(() -> Text.literal("Cannot heal player when in creative mode!"), false);
//			return -1;
//		}
//	}
//}