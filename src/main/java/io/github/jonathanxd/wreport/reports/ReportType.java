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
package io.github.jonathanxd.wreport.reports;

public interface ReportType {
    String getName();

    public static enum Normal implements ReportType {
        VOTE_KICK_AUTO {
            @Override
            public String getName() {
                return "VOTE_KICK_AUTO";
            }
        },

        PLAYER_REPORT {
            @Override
            public String getName() {
                return "PLAYER_REPORT";
            }
        },

        BUG_REPORT {
            @Override
            public String getName() {
                return "BUG_REPORT";
            }
        },
    }

    public static enum System implements ReportType {
        SYSTEM_REPORT_AUTO {
            @Override
            public String getName() {
                return "SYSTEM_REPORT_AUTO";
            }
        },

        SYSTEM_REPORT_PLAYER {
            @Override
            public String getName() {
                return "SYSTEM_REPORT_PLAYER";
            }
        },

        SYSTEM_REPORT_ANONYMOUS {
            @Override
            public String getName() {
                return "SYSTEM_REPORT_ANONYMOUS";
            }
        };
    }
}
