package io.github.jonathanxd.wreport.reports;

public interface ReportType {
	public static enum Normal implements ReportType {
		VOTE_KICK_AUTO, PLAYER_REPORT, BUG_REPORT,
	}
	
	public static enum System implements ReportType {
		SYSTEM_REPORT_AUTO, SYSTEM_REPORT_PLAYER, SYSTEM_REPORT_ANONYMOUS;
	}	
}
