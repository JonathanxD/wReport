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

/**
 * Created by jonathan on 03/04/16.
 */

import com.github.jonathanxd.iutils.object.TypeInfo;

/**
 * Represents an 'Action'
 *
 * Actions is called when a report is closed!
 *
 * Actions uses WCommand API to call Methods via Reflection.
 *
 * Provided Information:
 *
 * Game {@link org.spongepowered.api.Game}
 *
 * All Services Registered in {@link org.spongepowered.api.service.ServiceManager}
 *
 * Collection of {@link org.spongepowered.api.entity.living.player.User} tagged as
 * 'affectedPlayers'
 *
 * An {@link org.spongepowered.api.entity.living.player.User} tagged as 'causer' (ex: judge of
 * {@link io.github.jonathanxd.wreport.reports.CloseReportData})
 *
 * An {@link org.spongepowered.api.text.channel.MessageReceiver} tagged as 'messager'
 *
 * A {@link io.github.jonathanxd.wreport.reports.Report} tagged as 'report'.
 *
 * A {@link String} tagged as 'description'
 */
public interface Action {

    String getName();

    TypeInfo<? extends Action> getReference();

    enum State {
        OK,
        FAIL
    }
}
