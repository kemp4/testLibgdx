package pl.skempa.controller.app;


//import pl.skempa.controller.CameraController;
//import pl.skempa.controller.CameraControllerImpl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import pl.skempa.controller.input.MyInputProcessor;
import pl.skempa.model.Model;
import pl.skempa.model.MyModel;
import pl.skempa.view.MyView;
import pl.skempa.view.View;


public class MapThreeD extends ApplicationAdapter implements Controller{

	private static final float VELOCITY = 0.1f;
	View view;
	Model model;

	//CameraController cameraController;

	@Override
	public void create () {
		model = new MyModel();
		model.init();
		model.setSettings(new Settings());
		view = new MyView(this,model);
		view.init();
        initGUI();
		initInput();


	}
	private Stage stage;
	private Stage stage2;
	private Table table;
	TextButton guiButton;
	private void initGUI() {
		Skin skin;
		skin = new Skin(Gdx.files.internal("skins/gdx-holo/skin/uiskin.json"));

		final CheckBox checkBoxBuildings = new CheckBox("Budynki",skin);
		checkBoxBuildings.setPosition(0,0);
		checkBoxBuildings.setChecked(true);
		checkBoxBuildings.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				model.getSettings().displayBuildings=!model.getSettings().displayBuildings;
			}
		});
		final CheckBox checkBoxStreets = new CheckBox("Drogi i rzeki",skin);
		checkBoxStreets.setPosition(0,50);
		checkBoxStreets.setChecked(true);
		checkBoxStreets.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				model.getSettings().displayStreets=!model.getSettings().displayStreets;
			}
		});
		final CheckBox checkBoxTrees = new CheckBox("Drzewa i latarnie",skin);
		checkBoxTrees.setPosition(0,100);
		checkBoxTrees.setChecked(true);
		checkBoxTrees.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				model.getSettings().displayTrees=!model.getSettings().displayTrees;
			}
		});
		final CheckBox checkBoxPowerLines = new CheckBox("Linie energetyczne",skin);
		checkBoxPowerLines.setPosition(0,150);
		checkBoxPowerLines.setChecked(true);
		checkBoxPowerLines.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				model.getSettings().displayPower=!model.getSettings().displayPower;
			}
		});
		stage = new Stage();
		Table table = new Table();
		stage.addActor(table);
		table.setSize(260, 195);
		table.setPosition( 10,280);

		TextureRegionDrawable table1Region = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gray.png"))));

		BitmapFont buttonFont = skin.getFont("default-font");

		table.addActor(checkBoxBuildings);
		table.addActor(checkBoxPowerLines);
		table.addActor(checkBoxStreets);
		table.addActor(checkBoxTrees);
		table.setBackground(table1Region);

		Table table2 = new Table();
		table2.setBackground(table1Region);
		stage.addActor(table2);
		table2.setPosition(280,5);
		table2.setSize(350,470);
		table2.setDebug(true);


		final Label wybierzMape = new Label("Wybierz mape",skin);
		wybierzMape.setColor(1f,1f,1f,0.6f);
		wybierzMape.setPosition(10,440);
		final List mapList = new List(skin);
		mapList.setItems(readMapNamesArray());
		ScrollPane mapPane = new ScrollPane(mapList,skin);
		mapPane.setPosition(10,320);
		mapPane.setHeight(100);
		mapPane.setWidth(330);

		final Label wybierzHgt = new Label("Wybierz teren",skin);
		wybierzHgt.setPosition(10,290);
		wybierzHgt.setColor(1f,1f,1f,0.6f);
		final List hgtList = new List(skin);
		hgtList.setItems(readHgtNamesArray());
		ScrollPane hgtPane = new ScrollPane(hgtList,skin);
		hgtPane.setPosition(10,170);
		hgtPane.setHeight(100);
		hgtPane.setWidth(330);

		TextButton button2 = new TextButton("Generuj", skin);
		button2.setPosition(10,10);

		button2.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				model.getSettings().choosenHgt = hgtList.getSelected().toString();
				model.getSettings().choosenMap = mapList.getSelected().toString();
				System.out.println("generowanie mapy prosze czekac");
				model.getSettings().firts=true;
				return false;
			}
		});

		table2.top();
		table2.addActor(wybierzMape);
		table2.addActor(mapPane);
		table2.addActor(wybierzHgt);
		table2.addActor(hgtPane);
		table2.addActor(button2);

		guiButton = new TextButton("schowaj GUI", skin);


		guiButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				model.getSettings().displayGUI = !model.getSettings().displayGUI;
				return false;
			}
		});
		stage2 = new Stage();
		stage2.addActor(guiButton);
}

	private Array  readHgtNamesArray() {
		String dirName = "hgtFiles";
		FileHandle dirHandle = Gdx.files.internal(dirName);
		Array<String> hgtNames= new Array<>();
		for (FileHandle entry: dirHandle.list())
		{
			String name = entry.toString();
			String hgtName = name.substring(dirName.length()+1,name.length()-4);
			String extension = name.substring(name.length()-4,name.length());
			if (extension.equals(".hgt"))
				hgtNames.add(hgtName);
		}
		hgtNames.add("flat");
		return hgtNames;
	}

	private Array readMapNamesArray() {
		String dirName = "mapFiles";
		FileHandle dirHandle = Gdx.files.internal(dirName);
		Array<String> mapNames= new Array<>();
		for (FileHandle entry: dirHandle.list()) {
			String name = entry.toString();
			String mapName = name.substring(dirName.length()+1,name.length()-4);
			String extension = name.substring(name.length()-4,name.length());
			if (extension.equals(".pbf"))
				mapNames.add(mapName);
		}
		return mapNames;
	}

	private void initInput() {
		MyInputProcessor inputProcessor = new MyInputProcessor(model);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureDetector gestureDetector = new GestureDetector(inputProcessor);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(inputProcessor);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(stage2);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}


	@Override
	public void render () {
		String text= model.getSettings().displayGUI ? "schowaj GUI":"pokaz GUI";
		guiButton.setText(text);
		checkKeys();
		model.update();
		view.render();

        //GUI
		//Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0.1f);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(model.getSettings().displayGUI==true) {
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		}
		stage2.act(Gdx.graphics.getDeltaTime());
		stage2.draw();
	}

	private void checkKeys() {
		Vector3 trans = new Vector3(0,0,0);
		if (Gdx.input.isKeyPressed (Input.Keys.LEFT)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(VELOCITY).rotate(Vector3.Z,90);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.RIGHT)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(-VELOCITY).rotate(Vector3.Z,90);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.UP)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(VELOCITY);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.DOWN)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(-VELOCITY);
		}

		model.getCamera().translate(trans);
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}


	@Override
	public void dispose () {
	}



//	@Override
//	public CameraController getCameraController() {
//		return cameraController;
//	}
}