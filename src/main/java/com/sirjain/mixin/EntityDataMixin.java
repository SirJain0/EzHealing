package com.sirjain.mixin;

import com.sirjain.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataMixin implements IEntityDataSaver {
	private NbtCompound persistentData;

	@Override
	public NbtCompound getPersistentData() {
		if (this.persistentData == null)
			this.persistentData = new NbtCompound();

		return persistentData;
	}

	@Inject(method = "writeNbt", at = @At("HEAD"))
	protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
		if (this.persistentData != null)
			nbt.put("healh_control.custom_data", persistentData);
	}

	@Inject(method = "readNbt", at = @At("HEAD"))
	protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
		if (nbt.contains("healh_control.custom_data", 10))
			this.persistentData = nbt.getCompound("healh_control.custom_data");
	}
}