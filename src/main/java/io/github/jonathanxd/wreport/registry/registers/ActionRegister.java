package io.github.jonathanxd.wreport.registry.registers;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;
import com.github.jonathanxd.wcommands.ticket.RegistrationTicket;

import org.spongepowered.api.Game;

import io.github.jonathanxd.wreport.actions.Action;

/**
 * Created by jonathan on 03/04/16.
 */
public class ActionRegister extends AbstractMapRegister<Reference<?>, Action> {
    private final ReflectionCommandProcessor reflectionCommandProcessor;

    public ActionRegister(ReflectionCommandProcessor reflectionCommandProcessor) {
        this.reflectionCommandProcessor = reflectionCommandProcessor;
    }

    @Override
    public boolean register(Object plugin, Game game, Reference<?> key, Action obj) {

        if(super.register(plugin, game, key, obj)) {
            reflectionCommandProcessor.getRegister(RegistrationTicket.empty(plugin)).addCommands(obj);

            return true;
        }

        return false;
    }
}
