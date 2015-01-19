package mzsJVM;

import mzsJVM.NWNX.NWNX_Areas;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.SchedulerListener;

public class Test implements IScriptEventHandler {

	@Override
	public void runScript(NWObject objSelf) {
		NWObject pc = NWScript.getLastUsedBy();
		NWObject area = NWScript.getArea(pc);
		String resref = NWScript.getResRef(area);

		NWNX_Areas.LoadArea(resref);
	}
}
