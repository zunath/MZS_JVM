package mzsJVM;

import mzsJVM.NWNX.NWNX_APS;
import org.nwnx.nwnx2.jvm.NWObject;

public class Module_OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWNX_APS.SQLInitialize();
	}
}
