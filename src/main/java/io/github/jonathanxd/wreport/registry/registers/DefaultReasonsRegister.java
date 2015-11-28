package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;

import io.github.jonathanxd.wreport.wReport;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.reasons.Aggression;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

public class DefaultReasonsRegister implements Register {

	@Override
	public boolean register(Object plugin, Game game) {
		wReport wReportPlugin = wReport.wReportPlugin();
		
		IReasonRegister reasonRegister = wReportPlugin.reasonRegister();
		
		Reason aggressionReason = new Aggression();
		
		reasonRegister.register(aggressionReason);
		
		return true;
	}
	
	

}
