package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

public class SelectScreen extends ScreenAdapter {
	com.mygdx.ascendjumper.Ascend _game;
	OrthographicCamera _camera;
	Viewport _viewport;
	Vector3 _touchPoint;
	SpriteBatch _batch;

	TiledMap _tiledMap;
	OrthogonalTiledMapRenderer _tiledMapRenderer;

	Rectangle _leftArrowBounds;
	Rectangle _rightArrowBounds;
	Rectangle _mainmenuBounds;

	MapObjects _objects;
	HashMap<Integer, Rectangle> _stages;

	int _page = 1;
	final int FINAL_PAGE = 4;
	final int FINAL_STAGE = 16;
	int _stage = -1;
	int _unlockedStage = -1;
	String[] times = new String[FINAL_STAGE + 1];
	int[] HPs = new int[FINAL_STAGE + 1];
	Rectangle rect;

	@Override
	public void dispose() {
		super.dispose();
		_tiledMap.dispose();
		_tiledMapRenderer.dispose();
		_batch.dispose();
	}

	public SelectScreen(com.mygdx.ascendjumper.Ascend game) {
		_game = game;
		_camera = new OrthographicCamera(com.mygdx.ascendjumper.Ascend.GAME_WIDTH, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(com.mygdx.ascendjumper.Ascend.GAME_WIDTH, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();
		_batch = new SpriteBatch();
		_touchPoint = new Vector3();

		_tiledMap = new TmxMapLoader().load("map/stage_select.tmx");
		_tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);
		loadBounds();
		loadMapObjects();
		loadBestRecord();
	}

	private void loadBounds() {
		// Bounds
		_leftArrowBounds = new Rectangle(0, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT - 64, 64, 64);
		_rightArrowBounds = new Rectangle(com.mygdx.ascendjumper.Ascend.GAME_WIDTH - 64, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT - 64, 64, 64);
		_mainmenuBounds = new Rectangle(10, 10, Assets.backtomenu.getRegionWidth(), Assets.backtomenu.getRegionHeight());
	}

	private void loadMapObjects() {
		// stage objects
		_stages = new HashMap<Integer, Rectangle>();
		_objects = _tiledMap.getLayers().get("object").getObjects();
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("stage") && object instanceof RectangleMapObject) {
				String prop = (String) object.getProperties().get("stage");
				_stages.put(Integer.parseInt(prop), ((RectangleMapObject) object).getRectangle());
			}
		}
	}

	private void loadBestRecord() {
		// ベストタイムとHPの読み込み
		for (int i = 1; i <= FINAL_STAGE; i++) {
			if (Assets.prefsTime.contains("" + i)) {
				float time = Assets.prefsTime.getFloat("" + i);
				times[i] = String.format("%02d:%02.02f", (int) (time / 60f), time % 60);
				HPs[i] = Assets.prefsHP.getInteger("" + i);
			} else {
				if (_unlockedStage == -1)
					_unlockedStage = i;
				times[i] = "--:--.--";
				HPs[i] = 0;
			}
		}
		if(_unlockedStage == -1)
			_unlockedStage = FINAL_STAGE;
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	private void drawTime(String str, float x, float y) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (ch) {
				case ':':
					_batch.draw(Assets.numbers.getTexture(), x + i * 16, y, Assets.numbers.getRegionX() + 10 * 16, Assets.numbers.getRegionY(), 16, 32);
					break;
				case '.':
					_batch.draw(Assets.numbers.getTexture(), x + i * 16, y, Assets.numbers.getRegionX() + 11 * 16, Assets.numbers.getRegionY(), 16, 32);
					break;
				case '-':
					_batch.draw(Assets.numbers.getTexture(), x + i * 16, y, Assets.numbers.getRegionX() + 12 * 16, Assets.numbers.getRegionY(), 16, 32);
					break;
				default:
					int n = ch - '0';
					_batch.draw(Assets.numbers.getTexture(), x + i * 16, y, Assets.numbers.getRegionX() + n * 16, Assets.numbers.getRegionY(), 16, 32);
			}
		}
	}

	private void update() {
		if (Gdx.input.justTouched()) {
			_viewport.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (!touchArrow()) return;
			if (_mainmenuBounds.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				_game.setScreen(new MainMenuScreen(_game));
			}
			_stage = touchStage();
			if (_stage != -1 && _stage <= Assets.FINAL_STAGE) {
				Assets.playSound(Assets.selectSound);
				Assets.musicStop();
				_game.setScreen(new com.mygdx.ascendjumper.GameScreen(_game, _stage));
			}
		}
	}

	private int touchStage() {
		for (Map.Entry<Integer, Rectangle> e : _stages.entrySet()) {
			Rectangle stageRect = e.getValue();
			if (stageRect.contains(_touchPoint.x, _touchPoint.y)) {
				int selectedStage = e.getKey();
				if (selectedStage <= _unlockedStage)
					return selectedStage;
			}
		}
		return -1;
	}

	private boolean touchArrow() {
		if (_leftArrowBounds.contains(_touchPoint.x, _touchPoint.y)) {
			if (_page == 1) return false;
			Assets.playSound(Assets.selectSound);
			_page--;
			_leftArrowBounds.x -= com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_rightArrowBounds.x -= com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_mainmenuBounds.x -= com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_camera.position.sub((float) com.mygdx.ascendjumper.Ascend.GAME_WIDTH, 0, 0);
		}else if (_rightArrowBounds.contains(_touchPoint.x, _touchPoint.y)) {
			if (_page == FINAL_PAGE) return false;
			Assets.playSound(Assets.selectSound);
			_page++;
			_leftArrowBounds.x += com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_rightArrowBounds.x += com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_mainmenuBounds.x += com.mygdx.ascendjumper.Ascend.GAME_WIDTH;
			_camera.position.add((float) com.mygdx.ascendjumper.Ascend.GAME_WIDTH, 0, 0);
		}
		return true;
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_batch.setProjectionMatrix(_camera.combined);

		_tiledMapRenderer.setView(_camera);
		_tiledMapRenderer.render();
		_batch.begin();
		_batch.draw(Assets.backtomenu, _mainmenuBounds.getX(), _mainmenuBounds.getY());
		drawBestRecord();
		drawStageArts();
		_batch.end();
	}

	private void drawStageArts() {
		// 各ステージを描画
		for (int i = 0; i < _unlockedStage; i++) {
			rect = _stages.get(i+1);
			_batch.draw(Assets.stagesTexture, rect.getX(), rect.getY(), (i%4)*160, (i/4)*96, 160, 96);
		}
	}

	private void drawBestRecord() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				drawTime(times[i * 4 + j + 1], com.mygdx.ascendjumper.Ascend.GAME_WIDTH / 2 + i * com.mygdx.ascendjumper.Ascend.GAME_WIDTH, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT - 200 - j * 160);
				if (HPs[i * 4 + j + 1] != 0)
					drawHP(HPs[i * 4 + j + 1], com.mygdx.ascendjumper.Ascend.GAME_WIDTH / 2 - 175 + i * com.mygdx.ascendjumper.Ascend.GAME_WIDTH, com.mygdx.ascendjumper.Ascend.GAME_HEIGHT - 170 - j * 160);
			}
		}
	}

	private void drawHP(int hp, int x, int y) {
		for (int i = 1; i <= 3; i++) {
			if (hp >= i)
				_batch.draw(Assets.hitpoint, x + (i - 1) * 32, y);
			else
				_batch.draw(Assets.damage, x + (i - 1) * 32, y);
		}
	}

	@Override
	public void resize(int width, int height) {
		_viewport.update(width, height);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
	}
}
