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
package io.github.jonathanxd.wreport.config;

import com.google.common.reflect.TypeToken;

import io.github.jonathanxd.wreport.reports.ReportType;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 01/04/16.
 */
public class ReportTypeSerializer implements TypeSerializer<ReportType> {

    static final String REPORT_TYPE_NODE = "reportType";
    static final String REPORT_TYPE_NAME_NODE = "name";
    static final String REPORT_TYPE_CLASS_NODE = "class";

    @Override
    public ReportType deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        ConfigurationNode node = value.getNode(REPORT_TYPE_NODE);

        String name = node.getNode(REPORT_TYPE_NAME_NODE).getValue(TypeToken.of(String.class));

        try{
            String className = node.getNode(REPORT_TYPE_CLASS_NODE).getValue(TypeToken.of(String.class));

            Class<?> clazz = Class.forName(className);

            return (ReportType) clazz.getDeclaredField(name).get(null);

        }catch (Exception e) {
            throw new ObjectMappingException(e);
        }
    }

    @Override
    public void serialize(TypeToken<?> type, ReportType obj, ConfigurationNode value) throws ObjectMappingException {
        ConfigurationNode node = value.getNode(REPORT_TYPE_NODE);

        if(obj instanceof Enum<?>) {
            Enum<?> enumeration = (Enum<?>) obj;

            node.getNode(REPORT_TYPE_NAME_NODE).setValue(enumeration.name());
            node.getNode(REPORT_TYPE_CLASS_NODE).setValue(enumeration.getDeclaringClass().getName());
        } else {
            throw new ObjectMappingException("Only enumerations are supported yet!");
        }
    }
}
