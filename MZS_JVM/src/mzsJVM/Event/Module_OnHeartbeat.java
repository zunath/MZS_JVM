package mzsJVM.Event;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Weather;

@SuppressWarnings("unused")
public class Module_OnHeartbeat implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		SaveCharacters();
		if (NWScript.getLocalInt(NWObject.MODULE, "sWeathersChange") == NWScript.getTimeHour()) return;

		NWScript.deleteLocalInt(NWObject.MODULE, "sWeathersChange");
		NWScript.setLocalInt(NWObject.MODULE, "sWeathersChange", NWScript.getTimeHour());

		if (NWScript.random(100) <= 30) // A percentile roll. DEFAULT: 30%.
		{
			NWScript.setWeather(NWObject.MODULE, Weather.RAIN);
		}
		else
		{
			NWScript.setWeather(NWObject.MODULE, Weather.CLEAR);
		}
	}

	// Export all characters every minute.
	private void SaveCharacters()
	{
		int currentTick = NWScript.getLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK") + 1;

		if(currentTick >= 10)
		{
			NWScript.exportAllCharacters();
			currentTick = 0;
		}

		NWScript.setLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK", currentTick);
	}

}

