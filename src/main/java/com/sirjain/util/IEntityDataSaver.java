package com.sirjain.util;

import net.minecraft.nbt.NbtCompound;

// Used to help get amd edit persistent data of player
public interface IEntityDataSaver {
	NbtCompound getPersistentData();
}