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

package net.minecraftforge.client.event;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.client.gui.screen.Screen;

/**
 * This event is called before any Screen will open. If you don't want this to
 * happen, cancel the event. If you want to override this Screen, simply set the
 * Screen variable to your own Screen.
 *
 * @author jk-5, Rikka0w0
 */
public class GuiOpenEvent extends Event {
	private Screen screen;

	public GuiOpenEvent(Screen screen) {
		this.setGui(screen);
	}

	public Screen getGui() {
		return screen;
	}

	public void setGui(Screen screen) {
		this.screen = screen;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
