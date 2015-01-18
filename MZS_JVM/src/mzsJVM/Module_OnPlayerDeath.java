package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerDeath implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWScript.executeScript("nw_o0_death", objSelf);
	}
}

