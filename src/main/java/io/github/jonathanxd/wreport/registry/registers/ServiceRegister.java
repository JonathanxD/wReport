package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;
import org.spongepowered.api.service.ProviderExistsException;

import io.github.jonathanxd.wreport.wReport;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.Register;

public class ServiceRegister implements Register {

	@Override
	public boolean register(Object plugin, Game game) {
		
		wReport wReportPlugin = wReport.wReportPlugin();
		
		IReasonRegister reasonRregister = wReportPlugin.reasonRegister();
		
		try {
			game.getServiceManager().setProvider(plugin, IReasonRegister.class, reasonRregister);
		} catch (ProviderExistsException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return "ServiceRegister";
	}
}
