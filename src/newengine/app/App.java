package newengine.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.EventBus;
import commons.point.GamePoint;
import javafx.application.Application;
import javafx.stage.Stage;
import newengine.events.GameInitializationEvent;
import newengine.events.QueueEvent;
import newengine.events.SpriteModelEvent;
import newengine.events.sound.SoundEvent;
import newengine.events.sprite.MoveEvent;
import newengine.events.stats.ChangeLivesEvent;
import newengine.events.stats.ChangeWealthEvent;
import newengine.managers.conditions.GoldMinimumCondition;
import newengine.managers.conditions.NoLivesCondition;
import newengine.managers.conditions.SetEndConditionEvent;
import newengine.player.Player;
import newengine.skill.Skill;
import newengine.skill.SkillType;
import newengine.skill.skills.BuildSkill;
import newengine.skill.skills.FireProjectileSkill;
import newengine.skill.skills.MoveSkill;
import newengine.sprite.Sprite;
import newengine.sprite.components.Attacker;
import newengine.sprite.components.Collidable;
import newengine.sprite.components.Collidable.CollisionBoundType;
import newengine.sprite.components.EventQueue;
import newengine.sprite.components.GameBus;
import newengine.sprite.components.Health;
import newengine.sprite.components.Images;
import newengine.sprite.components.Owner;
import newengine.sprite.components.Position;
import newengine.sprite.components.Range;
import newengine.sprite.components.Selectable;
import newengine.sprite.components.Selectable.SelectionBoundType;
import newengine.sprite.components.SkillSet;
import newengine.sprite.components.SoundEffect;
import newengine.sprite.components.Speed;
import newengine.trigger.Trigger;
import newengine.utils.Target;
import newengine.utils.image.ImageSet;
import newengine.utils.image.LtubImage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		Game game = new Game();
		Player player1 = new Player("Player 1");
		Player player2 = new Player("Player 2");
		
		// building
		Sprite building = new Sprite();
		LtubImage buildingImage = new LtubImage("images/skills/build.png");
		ImageSet imageSetBuildSkill = new ImageSet(buildingImage);
		building.addComponent(new Images(imageSetBuildSkill));
		building.addComponent(new Selectable(SelectionBoundType.IMAGE));
		
		EventBus bus = game.getBus();
		
		// sprite 1
		Sprite sprite1 = new Sprite();
		LtubImage image1 = new LtubImage("images/characters/bahamut_left.png");
		ImageSet imageSet1 = new ImageSet(image1);
		Map<SkillType<? extends Skill>, Skill> skillMap = new HashMap<>();
		skillMap.put(MoveSkill.TYPE, new MoveSkill());
		skillMap.put(BuildSkill.TYPE, new BuildSkill(building));
		skillMap.put(FireProjectileSkill.TYPE, new FireProjectileSkill());
		sprite1.addComponent(new GameBus());
		sprite1.addComponent(new SkillSet(skillMap));
		sprite1.addComponent(new Owner(player1));
		sprite1.addComponent(new Position(new GamePoint(200, 100), 0));
		sprite1.addComponent(new SoundEffect("data/sounds/Psyessr4.wav"));
		sprite1.addComponent(new Images(imageSet1));
		sprite1.addComponent(new Speed(200));
		sprite1.addComponent(new Collidable(CollisionBoundType.IMAGE));
		sprite1.addComponent(new Selectable(SelectionBoundType.IMAGE));
		sprite1.addComponent(new Range(128));
		sprite1.addComponent(new Attacker());
		sprite1.addComponent(new Health(200));
		sprite1.addComponent(new EventQueue());
			
		// sprite 2
		Sprite sprite2 = new Sprite();
		LtubImage image2 = new LtubImage("images/characters/bahamut_right.png");
		ImageSet imageSet2 = new ImageSet(image2);
		Map<SkillType<? extends Skill>, Skill> skillMap2 = new HashMap<>();
		skillMap2.put(MoveSkill.TYPE, new MoveSkill());
		skillMap2.put(FireProjectileSkill.TYPE, new FireProjectileSkill());
		sprite2.addComponent(new GameBus());
		sprite2.addComponent(new SkillSet(skillMap2));
		sprite2.addComponent(new Owner(player2));
		sprite2.addComponent(new Position(new GamePoint(300, 250), 0));
		sprite2.addComponent(new SoundEffect("data/sounds/Psyessr4.wav"));
		sprite2.addComponent(new Images(imageSet2));
		sprite2.addComponent(new Speed(100));
		sprite2.addComponent(new Collidable(CollisionBoundType.IMAGE));
		sprite2.addComponent(new Selectable(SelectionBoundType.IMAGE));
		sprite2.addComponent(new EventQueue());
		sprite2.addComponent(new Attacker());
		sprite2.addComponent(new Health(60));
		
		List<Sprite> spritesToAdd = new ArrayList<>();
		spritesToAdd.add(sprite1);
		spritesToAdd.add(sprite2);
		
		
		bus.on(GameInitializationEvent.ANY, (e) -> {
			bus.emit(new SoundEvent(SoundEvent.BACKGROUND_MUSIC, "data/sounds/01-dark-covenant.mp3"));
			bus.emit(new SpriteModelEvent(SpriteModelEvent.ADD, spritesToAdd));
			bus.emit(new ChangeLivesEvent(ChangeLivesEvent.SET, Player.MAIN_PLAYER, 3));
			bus.emit(new ChangeWealthEvent(ChangeWealthEvent.CHANGE, player1, "gold", 100));
			bus.emit(new SetEndConditionEvent(SetEndConditionEvent.SETWIN, new GoldMinimumCondition(1000)));
			bus.emit(new SetEndConditionEvent(SetEndConditionEvent.SETLOSE, new NoLivesCondition()));
		});
		
		// Triggers
		for (Trigger trigger : (new TestTriggers()).getTriggers()) {
			game.addTrigger(trigger);
		}
		
		stage.setScene(game.getScene());
		game.start();
		stage.show();
		
		// TODO add method to generate path
		sprite1.emit(new QueueEvent(QueueEvent.ADD, new MoveEvent(MoveEvent.START_POSITION, sprite1, 
				new Target(new GamePoint(300,100)))));
		sprite1.emit(new QueueEvent(QueueEvent.ADD, new MoveEvent(MoveEvent.START_POSITION, sprite1, 
				new Target(new GamePoint(250,200)))));
		sprite1.emit(new QueueEvent(QueueEvent.ADD, new MoveEvent(MoveEvent.START_POSITION, sprite1, 
				new Target(new GamePoint(100,150)))));
		sprite1.emit(new QueueEvent(QueueEvent.ADD, new MoveEvent(MoveEvent.START_POSITION, sprite1, 
				new Target(new GamePoint(500,300)))));
		sprite1.emit(new QueueEvent(QueueEvent.ADD, new MoveEvent(MoveEvent.START_POSITION, sprite1, 
				new Target(new GamePoint(400,200)))));
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
