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
