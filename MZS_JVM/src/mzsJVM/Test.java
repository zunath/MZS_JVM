package mzsJVM;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.SchedulerListener;
import org.nwnx.nwnx2.jvm.constants.Talkvolume;

public class Test implements SchedulerListener {
	public void postFlushQueues(int remainingTokens) {}
	public void missedToken(NWObject objSelf, String token) {}
	public void context(NWObject objSelf) {}
	
	public void event(final NWObject objSelf, String event) {
		NWObject pc = NWScript.getLastUsedBy();

		NWScript.sendMessageToPC(pc, "Test script is firing");

		Scheduler.assign(pc, new Runnable() {
			@Override
			public void run() {

				NWObject bread = NWScript.getObjectByTag("testbread", 0);

				NWScript.speakString("Bread = " + NWScript.getName(bread, false), Talkvolume.SHOUT);

				NWScript.actionAttack(bread, false);
			}
		});
		Scheduler.flushQueues();

	}
}
