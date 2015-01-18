package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerDeath implements SchedulerListener {
	public void postFlushQueues(int remainingTokens) {}
	public void missedToken(NWObject objSelf, String token) {}
	public void context(NWObject objSelf) {}
	
	public void event(NWObject objSelf, String event) {
		NWScript.executeScript("nw_o0_death", objSelf);
	}
}
