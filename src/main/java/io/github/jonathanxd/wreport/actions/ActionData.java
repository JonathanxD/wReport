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
package io.github.jonathanxd.wreport.actions;

import com.github.jonathanxd.iutils.object.TypeInfo;

import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.reports.Report;

/**
 * Created by jonathan on 03/04/16.
 */
public class ActionData {

    private final TypeInfo<? extends Action> reference;
    private final User causer;
    private final Collection<User> affectedUsers;
    private final Collection<String> actionArguments;
    /**
     * Reflection accessed from {@link io.github.jonathanxd.wreport.config.ReportSerializer}
      */

    private final Report report;

    public ActionData(TypeInfo<? extends Action> reference, User causer, Collection<User> affectedUsers, Collection<String> actionArguments, Report report) {
        this.reference = reference;
        this.causer = causer;
        this.affectedUsers = affectedUsers;
        this.actionArguments = actionArguments;
        this.report = report;
    }

    public Collection<String> getActionArguments() {
        return actionArguments;
    }

    public Collection<User> getAffectedUsers() {
        return affectedUsers;
    }

    public TypeInfo<? extends Action> getReference() {
        return reference;
    }

    public Optional<User> getCauser() {
        return Optional.ofNullable(causer);
    }

    public Report getReport() {
        return report;
    }
}
