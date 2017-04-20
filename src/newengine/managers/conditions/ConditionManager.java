package newengine.managers.conditions;

import bus.EventBus;
import newengine.model.PlayerStatsModel;
import newengine.model.SpriteModel;

public class ConditionManager {
	private EventBus bus;
	private SpriteModel spriteModel;
	private PlayerStatsModel playerStatsModel;
	private ICondition winning;
	private ICondition losing;

	public ConditionManager(EventBus bus, SpriteModel spriteModel, PlayerStatsModel playerStatsModel){
		this.bus = bus;
		this.spriteModel = spriteModel;
		this.playerStatsModel = playerStatsModel;
		initHandlers();
	} 
	
	private void initHandlers() {
		bus.on(SetEndConditionEvent.SETWIN, (e) -> {this.winning = setModels(e.getCondition());});
		bus.on(SetEndConditionEvent.SETLOSE, (e) -> {this.losing = setModels(e.getCondition());});
	}

	public void checkConditions() {
		checkWinningCondition();
		checkLosingCondition();
	}
	
	private void checkWinningCondition(){
		if(winning != null && winning.check()){
			//TODO Have game player to be set to subscribe to this event
			bus.emit(new EndConditionTriggeredEvent(EndConditionTriggeredEvent.WIN));
		}
	}
	
	private void checkLosingCondition(){
		if(losing != null && losing.check()){
			//TODO Have game player be set to subscribe to this event
			bus.emit(new EndConditionTriggeredEvent(EndConditionTriggeredEvent.LOSE));
		}
	}
	
	private ICondition setModels(ICondition condition) {
		condition.setPlayerStatsModel(playerStatsModel);
		condition.setSpriteModel(spriteModel);
		return condition;
	}

	
}
