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
package io.github.jonathanxd.wreport.commands.wext.common;

import com.github.jonathanxd.wcommands.command.holder.CommandHolder;
import com.github.jonathanxd.wcommands.data.CommandData;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.ProvidedRequirement;

import org.spongepowered.api.service.permission.Subject;

import java.util.Optional;

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */
public final class SpongeRequirements {

    public static final Class<?> PERMISSION = Permission.class;

    public static final ProvidedRequirement PERMISSION_TESTER = new PermissionRequirement();

    public static final class PermissionRequirement implements ProvidedRequirement {

        @Override
        public boolean test(String data, Object[] parameters, CommandData<CommandHolder> commandData, InformationRegister informationRegister, Information<?> subject) {

            Optional<Object> optional = Optional.empty();

            if (!subject.isPresent()) {
                optional = informationRegister.getOptional(SpongeInfos.SENDER);
            } else {
                optional = subject.isPresent() ? Optional.of(subject.get()) : Optional.empty();
            }

            if (optional.isPresent()) {
                Object unknown = optional.get();

                if (unknown instanceof Subject) {
                    Subject subject1 = (Subject) unknown;
                    return subject1.hasPermission(data);
                }
            }

            return false;
        }

    }

    private final class Permission {
    }
}
