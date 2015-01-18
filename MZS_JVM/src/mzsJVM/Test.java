package mzsJVM;

import mzsJVM.NWNX.NWNX_Areas;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.SchedulerListener;

public class Test implements IScriptEventHandler {

	@Override
	public void runScript(NWObject objSelf) {
		NWObject pc = NWScript.getLastUsedBy();

		NWNX_Areas.DestroyArea(NWScript.getArea(pc));
	}
}
