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
package io.github.jonathanxd.wreport.commands.wext;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;

import java.util.List;

import io.github.jonathanxd.wreport.commands.wext.suggestions.AppendType;

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */
public interface ArgumentSuggestion {

    /**
     * Get argument suggestions
     *
     * @param spec            Argument
     * @param input           Input arguments
     * @param lastSuggestions Clone List of last suggestions, modifying this list will not affect
     *                        original list
     * @param yourSuggestions Your suggestions, this argument is a instance of {@link
     *                        java.util.ArrayList}, you need to add your suggestions to this list
     * @return A {@link AppendType} or null. Null return will be considered {@link
     * AppendType#APPEND}.
     * @see AppendType
     */
    AppendType getSuggestions(ArgumentSpec<?, ?> spec, String[] input, List<String> lastSuggestions, List<String> yourSuggestions);

}
