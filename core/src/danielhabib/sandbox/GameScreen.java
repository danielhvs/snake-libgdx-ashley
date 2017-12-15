package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ArrayMap;

import aurelienribon.tweenengine.Tween;
import danielhabib.factory.AEntityBuilder;
import danielhabib.factory.CharBuilder;
import danielhabib.factory.World;
import danielhabib.sandbox.components.ClickComponent;
import danielhabib.sandbox.components.ClickComponent.Event;
import danielhabib.sandbox.components.GameTextComponent;
import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CharSelectSystem;
import danielhabib.sandbox.systems.DevSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.RotationSystem;
import danielhabib.sandbox.systems.SelectedLabelSystem;
import danielhabib.sandbox.systems.TimeoutSystem;
import danielhabib.sandbox.tween.ActorAcessor;
import danielhabib.sandbox.tween.GameTweens;
import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class GameScreen extends AbstractScreen {

	private PooledEngine engine;
	private SpriteBatch gameBatch;
	private World world;
	private int level;
	private Entity clickEntity;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		Assets.loop(Assets.backgroundSound);
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		} else {
			float offset = 32 * RenderingSystem.PIXELS_TO_METER;
			float offsetScl = 1.25f;
			float offsetScl2 = 1 / offsetScl;
			if (Gdx.input.isKeyJustPressed(Keys.W)) {
				engine.getSystem(DevSystem.class).incY(offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.A)) {
				engine.getSystem(DevSystem.class).incX(-offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.S)) {
				engine.getSystem(DevSystem.class).incY(-offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
				engine.getSystem(DevSystem.class).incX(offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
				engine.getSystem(DevSystem.class).incSclX(offsetScl);
			} else if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				engine.getSystem(DevSystem.class).incSclX(offsetScl2);
			} else if (Gdx.input.isKeyJustPressed(Keys.P)) {
				engine.getSystem(DevSystem.class).zoomIn();
			} else if (Gdx.input.isKeyJustPressed(Keys.L)) {
				engine.getSystem(DevSystem.class).zoomOut();
			}
		}

		engine.update(delta);
		super.render(delta);
	}

	@Override
	public void buildStage() {
		engine = new PooledEngine();

		TiledMap map = new TmxMapLoader().load("map" + level + ".tmx");
		String levelText = map.getProperties().get("text").toString();
		ArrayMap<String, AEntityBuilder> builders;
		builders = new ArrayMap<String, AEntityBuilder>();
		builders.put("transparent", new CharBuilder(engine));

		world = new World(builders, map);
		gameBatch = new SpriteBatch();

		engine.addSystem(new RenderingSystem(gameBatch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new RotationSystem());
		engine.addSystem(new CharSelectSystem());
		engine.addSystem(new SelectedLabelSystem());
		engine.addSystem(new TimeoutSystem());
		engine.addSystem(new DevSystem());
		world.create();

		Table inGameMenu = inGameMenu();
		addActor(inGameMenu);
		Table inGameText = inGameText(levelText);
		addActor(inGameText);

		Tween.registerAccessor(Actor.class, new ActorAcessor());
		GameTweens.fadeIn(inGameMenu, tweenManager);
		GameTweens.fadeIn(inGameText, tweenManager);
		ImmutableArray<Entity> labelEntities = engine.getEntitiesFor(Family.all(LabelComponent.class).get());
		for (Entity labelEntity : labelEntities) {
			GameTweens.fadeIn(labelEntity.getComponent(LabelComponent.class).label, tweenManager);
		}
	}

	private Table inGameText(String text) {
		Table table = new Table();
		int height = Gdx.graphics.getHeight() / 5;
		Label textLabel = UIFactory.newLabel();
		textLabel.setText(text.replace("\\n", "\n"));
		table.setBounds(0, 0, Gdx.graphics.getWidth(), height);
		table.center();
		Assets.font.getData().setLineHeight(Assets.font.getData().capHeight);
		table.add(textLabel).expand();
		Entity entity = engine.createEntity();
		GameTextComponent gameTextComponent = new GameTextComponent();
		gameTextComponent.label = textLabel;
		entity.add(gameTextComponent);
		engine.addEntity(entity);
		return table;
	}

	private Table inGameMenu() {
		Table table = new Table();
		Label label = UIFactory.newLabel();
		label.setText("Level " + level);
		table.setBounds(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight() / 10);
		table.left();
		TextButton backButton = ButtonFactory.newButton(" < ");
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		table.add(backButton).expandX();
		table.add(label).expandX();
		TextButton levelButton = ButtonFactory.newButton(" [ ] ");
		levelButton.addListener(UIFactory.createListener(ScreenEnum.LEVEL_SELECT));
		table.add(levelButton).expandX();
		return table;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			float x = Gdx.input.getX();
			float y = Gdx.graphics.getHeight() - Gdx.input.getY();
			clickEntity = engine.createEntity();
			ClickComponent click = engine.createComponent(ClickComponent.class);
			click.x = x;
			click.y = y;
			click.event = Event.DOWN;
			clickEntity.add(click);
			engine.addEntity(clickEntity);
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			float x = Gdx.input.getX();
			float y = Gdx.graphics.getHeight() - Gdx.input.getY();
			clickEntity = engine.createEntity();
			ClickComponent click = engine.createComponent(ClickComponent.class);
			click.x = x;
			click.y = y;
			click.event = Event.UP;
			clickEntity.add(click);
			engine.addEntity(clickEntity);
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float x = Gdx.input.getX();
		float y = Gdx.graphics.getHeight() - Gdx.input.getY();
		clickEntity = engine.createEntity();
		ClickComponent click = engine.createComponent(ClickComponent.class);
		click.x = x;
		click.y = y;
		click.event = Event.DRAG;
		clickEntity.add(click);
		engine.addEntity(clickEntity);
		return super.touchDragged(screenX, screenY, pointer);
	}

}
