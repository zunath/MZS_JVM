package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class Module_OnClientLeave implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject pc = NWScript.getExitingObject();
		NWScript.exportSingleCharacter(pc);

		NWScript.executeScript("fky_chat_clexit", objSelf);
	}
}
