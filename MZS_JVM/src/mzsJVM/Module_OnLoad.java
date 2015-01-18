package mzsJVM;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.SchedulerListener;

public class Module_OnLoad implements SchedulerListener {
	public void postFlushQueues(int remainingTokens) {}
	public void missedToken(NWObject objSelf, String token) {}
	public void context(NWObject objSelf) {}
	
	public void event(NWObject objSelf, String event) {
		NWScript.executeScript("aps_onload", objSelf);
	}
}
