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
package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;

import io.github.jonathanxd.wreport.registry.DefaultRegister;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.wReport;

public class ServiceRegister implements DefaultRegister {

    @Override
    public boolean register(Object plugin, Game game) {

        wReport wReportPlugin = wReport.wReportPlugin();

        IReasonRegister reasonRregister = wReportPlugin.reasonRegister();
        IReportManager reportManager = wReportPlugin.getReportManager();

        game.getServiceManager().setProvider(plugin, IReasonRegister.class, reasonRregister);
        game.getServiceManager().setProvider(plugin, IReportManager.class, reportManager);

        return true;
    }

    @Override
    public String getName() {
        return "ServiceRegister";
    }
}
