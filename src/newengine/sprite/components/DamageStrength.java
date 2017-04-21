package newengine.sprite.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bus.BusEventHandler;
import newengine.events.SpriteModelEvent;
import newengine.events.sprite.MoveEvent;
import newengine.player.Player;
import newengine.sprite.Sprite;
import newengine.sprite.component.Component;
import newengine.sprite.component.ComponentType;

public class DamageStrength extends Component {
	
	public static final ComponentType<DamageStrength> TYPE = new ComponentType<>(DamageStrength.class.getName());
	private int strength;


	public DamageStrength(int strength){
		this.strength = strength;
	}
	
	@Override
	public void afterAdded() {
		sprite.on(MoveEvent.STOP, (Serializable & BusEventHandler<MoveEvent>) (e) -> {
			Player owner = sprite.getComponent(Owner.TYPE).get().player();			
			Player anotherOwner = e.getTarget().getSprite().get().getComponent(Owner.TYPE).get().player();
			if (owner != anotherOwner) {
				sprite.getComponent(Position.TYPE).ifPresent((position) -> {
					if (position.isMoving() == false) {
						List<Sprite> spritesToRemove = new ArrayList<>();
						spritesToRemove.add(sprite);
						sprite.getComponent(GameBus.TYPE).ifPresent((gameBus) -> {
							gameBus.getGameBus().emit(new SpriteModelEvent(SpriteModelEvent.REMOVE, spritesToRemove));
						});		
					}
				});
						
			}
		});
	}
	
	@Override
	public ComponentType<? extends Component> getType() {
		return TYPE;
	}
	
	public int getStrength(){
		return strength;
	}

	@Override
	public Component clone() {
		DamageStrength clone = new DamageStrength(this.strength);
		return clone;
	}

}
