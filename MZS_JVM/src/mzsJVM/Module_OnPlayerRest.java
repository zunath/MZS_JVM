package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerRest implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWScript.executeScript("x2_mod_def_rest", objSelf);
	}
}
