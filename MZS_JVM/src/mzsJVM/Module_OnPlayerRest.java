package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerRest implements SchedulerListener {
	public void postFlushQueues(int remainingTokens) {}
	public void missedToken(NWObject objSelf, String token) {}
	public void context(NWObject objSelf) {}
	
	public void event(NWObject objSelf, String event) {
		NWScript.executeScript("x2_mod_def_rest", objSelf);
	}
}
