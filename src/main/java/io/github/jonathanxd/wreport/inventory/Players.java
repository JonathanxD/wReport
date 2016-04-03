/*
 *      wReport - An Sponge plugin to report bad players and start a vote kick. <https://github.com/JonathanxD/io.github.jonathanxd.wreport.wReport/>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
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
