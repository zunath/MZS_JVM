package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class Module_OnPlayerEquipItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWScript.executeScript("x2_mod_def_equ", objSelf);
	}
}
