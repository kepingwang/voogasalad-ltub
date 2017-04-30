package gamedata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bus.EventBus;
import data.SerializableDeveloperData;
import data.SpriteMakerModel;
import gamecreation.level.LevelData;
import newengine.app.Game;
import newengine.events.SpriteModelEvent;
import newengine.sprite.Sprite;
import player.helpers.GameLoadException;
import utilities.XStreamHandler;

/**
 * @author tahiaemran, Keping
 *
 * class that creates a game from an XML file of translated GameData 
 *
 */
public class GameCreator {

	private XStreamHandler xstream = new XStreamHandler(); 
	
	
	public GameCreator() {
		//xstream = new XStream(new DomDriver());
	}
	
	public Game createGame(String gameFilePath) throws GameLoadException {
		return createGame(new File(gameFilePath));
	}
	public Game createGame(File gameFile) throws GameLoadException {
		try {
			System.out.println("In the try");
			// Read out the game 
			SerializableDeveloperData gameData = (SerializableDeveloperData) xstream.getObjectFromFile();//fromXML(gameFile);
			System.out.println("Got game data");
			//Process Levels
			List<LevelData> levels = gameData.getLevels();
			System.out.println(levels.get(0).getName());
			//Process Sprites 
			List<SpriteMakerModel> initialSpriteModels =  gameData.getSprites();
			System.out.println(initialSpriteModels);
			//TranslationController translator = new TranslationController(initialSpriteModels);
			List<Sprite> createdSprites = new ArrayList<Sprite>();
			for (SpriteMakerModel spriteToCreate : initialSpriteModels) {
				AuthDataTranslator translator = new AuthDataTranslator(spriteToCreate);
				createdSprites.add(translator.getSprite());
			}
			//translator.translate();
			//List<Sprite> createdSprites = (List<Sprite>) translator.getTranslated();  
			
			Game game = new Game(); 
			EventBus bus = game.getBus();
			bus.emit(new SpriteModelEvent(SpriteModelEvent.ADD, createdSprites));
			
			
//			// TODO: figure this part out 
//			bus.emit(new MainPlayerEvent(createdSprites.get(0).getComponent(Owner.TYPE).get().player()));
//			bus.emit(new ChangeLivesEvent(ChangeLivesEvent.SET, 
//					createdSprites.get(0).getComponent(Owner.TYPE).get().player(), 
//					gameData.getStartingLives()));
//			bus.emit(new ChangeWealthEvent(ChangeWealthEvent.CHANGE,
//					createdSprites.get(0).getComponent(Owner.TYPE).get().player(),
//					WealthType.GOLD, gameData.getStartingGold()));
//			
			return game;
		} catch (Exception e) {
			throw new GameLoadException("Fail to load game: "+gameFile);
		}
	}

}
