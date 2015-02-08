package mzsJVM.Event;
import mzsJVM.Entities.PlayerEntity;
import mzsJVM.GameObject.PlayerGO;
import mzsJVM.IScriptEventHandler;
import mzsJVM.Repository.PlayerRepository;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class Module_OnClientLeave implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWObject pc = NWScript.getExitingObject();
		NWScript.exportSingleCharacter(pc);
		SaveCharacter(pc);

		NWScript.executeScript("fky_chat_clexit", objSelf);
	}

	private void SaveCharacter(NWObject pc) {

		if(NWScript.getIsDM(pc)) return;

		PlayerGO gameObject = new PlayerGO(pc);
		PlayerRepository repo = new PlayerRepository();
		String uuid = gameObject.getUUID();
		NWLocation location = NWScript.getLocation(pc);

		PlayerEntity entity = repo.getByUUID(uuid);
        if(entity == null)
        {
            return;
        }

		entity.setCharacterName(NWScript.getName(pc, false));
		entity.setHitPoints(NWScript.getCurrentHitPoints(pc));
		entity.setLocationAreaTag(NWScript.getTag(NWScript.getArea(pc)));
		entity.setLocationOrientation(NWScript.getFacing(pc));
		entity.setLocationX(location.getX());
		entity.setLocationY(location.getY());
		entity.setLocationZ(location.getZ());

		repo.save(entity);
	}
}
