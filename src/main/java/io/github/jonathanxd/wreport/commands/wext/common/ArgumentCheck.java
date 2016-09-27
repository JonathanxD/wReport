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

import com.google.common.reflect.TypeToken;

import com.github.jonathanxd.iutils.object.TypeInfo;
import com.github.jonathanxd.iutils.object.TypeUtil;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import io.github.jonathanxd.wreport.commands.wext.common.id.CommonIDs;

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */
public class ArgumentCheck {

    @SuppressWarnings("unchecked")
    public static <T> boolean argumentCheck(ArgumentSpec<?, ?> spec, Class<? extends T> expected) {

        if (checkId(spec, expected) || checkInstance(spec, expected)) {
            return true;
        }

        Optional<TypeInfo<?>> data = ((ArgumentSpec) spec).getValueType();

        if (data.isPresent()) {
            TypeInfo dataReference = (TypeInfo) data.get();

            if (dataReference.compareToAssignable(TypeInfo.aEnd(expected)) == 0) {
                return true;
            }


        }

        return false;

    }

    private static boolean checkId(ArgumentSpec<?, ?> spec, Class<?> type) {
        Object specId = spec.getId();
        if (specId instanceof CommonIDs) {

            CommonIDs id = (CommonIDs) specId;
            if (type.isAssignableFrom(id.getType())) {
                return true;
            }

        }
        return false;
    }

    public static boolean checkInstance(ArgumentSpec<?, ?> spec, Class<?> type) {

        TypeToken<? extends ArgumentSpec> of = TypeToken.of(spec.getClass());

        for (TypeToken<?> second : of.getTypes()) {
            if (second.getRawType() == ArgumentSpec.class) {
                Type theType = second.getType();

                if (theType instanceof ParameterizedType) {

                    TypeInfo<?> reference = TypeUtil.toReference((ParameterizedType) theType);

                    if (reference.getRelated().length == 2) {
                        TypeInfo<?> ref = reference.getRelated()[1];

                        if (ref.compareToAssignable(TypeInfo.aEnd(type)) == 0) {
                            return true;
                        }
                    } else {
                        throw new RuntimeException("Needs revision...");
                    }

                }
            }
        }

        return false;
    }

}
