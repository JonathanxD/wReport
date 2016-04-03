/*
 *
 * 	wReport - An Sponge plugin to report bad players and start a vote kick.
 *     Copyright (C) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *
 * 	GNU GPLv3
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.jonathanxd.wreport.inventory;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SkullTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.translation.FixedTranslation;

import java.util.List;

/**
 * Created by jonathan on 01/04/16.
 */
public class Players {

    final Inventory inventory = CustomInventory.builder().name(new FixedTranslation("Players")).size(36).build();

    public Players(List<Player> playerList) {
        toFill(0, playerList);
    }

    public void toFill(int start, List<Player> playerList) {
        if(playerList.size() > 30) {
            fill(start, start+30, playerList);
        } else {
            fill(start, playerList.size(), playerList);
        }
    }

    public void fill(int start, int end, List<Player> playerList) {

        for(int x = start; x < end; ++x) {
            Player player = playerList.get(x);

            ItemStack skull = ItemStack.of(ItemTypes.SKULL, 1);

            skull.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER);

            skull.offer(Keys.DISPLAY_NAME, Text.of(player.getName()));

            inventory.offer(skull);
        }

        if(end < playerList.size()) {
            toFill(end, playerList);
        }
    }


}
