/*
 * Minecraft Forge, Patchwork Project
 * Copyright (c) 2016-2020, 2019-2020
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.patchworkmc.mixin.extension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Maps;
import net.minecraftforge.common.extensions.IForgeItem;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import com.patchworkmc.impl.extension.PatchworkItemSettingsExtensions;

@Mixin(Item.class)
public abstract class MixinItem implements IForgeItem {
	@Unique private Map<Object /* ToolType */, Integer> toolClasses;
	@Unique private boolean canRepair;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void onConstruct(Item.Settings settings, CallbackInfo info) {
		final PatchworkItemSettingsExtensions extension = (PatchworkItemSettingsExtensions) settings;

		canRepair = extension.canRepair();

		toolClasses = Maps.newHashMap();
		toolClasses.putAll(extension.getToolClasses());
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return canRepair && getItem().isDamageable();
	}

	@Override
	public Set<Object /* ToolType */> getToolTypes(ItemStack stack) {
		return toolClasses.keySet();
	}

	@Override
	public int getHarvestLevel(ItemStack stack, Object /* ToolType */ tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
		return toolClasses.getOrDefault(tool, -1);
	}

	@Shadow
	Map<Identifier, ItemPropertyGetter> propertyGetters;

	@Override
	public Map<Identifier, ItemPropertyGetter> patchwork_getPropertyGetters() {
		return propertyGetters;
	}

	@Unique Set<Identifier> cachedTags;
	@Unique int tagVersion;

	@Override
	public Set<Identifier> getTags() {
		if (cachedTags == null || tagVersion != ItemTagsAccessor.getLatestVersion()) {
			this.cachedTags = Collections.unmodifiableSet(new HashSet<>(ItemTags.getContainer().getTagsFor(getItem())));
			this.tagVersion = ItemTagsAccessor.getLatestVersion();
		}

		return this.cachedTags;
	}
}
