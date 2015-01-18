package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerRespawn implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWScript.executeScript("nw_o0_respawn", objSelf);
	}
}
