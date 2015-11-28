package io.github.jonathanxd.wreport.reports.reasons;

import java.util.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class Agression implements Reason{

	@Override
	public Text reasonMessage() {
		// TODO Auto-generated method stub
		return Texts.builder("Agression.").color(TextColors.RED).build();
	}

	@Override
	public Optional<Object> apply() {
		
		return Reason.super.apply();
	}
}
