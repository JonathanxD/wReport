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
package io.github.jonathanxd.wreport;

/**
 * Created by jonathan on 02/04/16.
 */
public interface LoadStateFetcher {

    LoadState getState();

    default void waitFor(LoadState state) {
        while(getState().ordinal() < state.ordinal()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
    }

    enum LoadState {
        LOADING,
        LOADED
    }
}
