package com.mygdx.ascendjumper;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Jun on 15/10/31.
 */
public class GameMap {
	TiledMap _tiledMap;
	OrthogonalTiledMapRenderer _tiledMapRenderer;
	private int _mapPixelWidth;
	private int _mapPixelHeight;
	MapObjects _objects;
	Rectangle _goalRect;
	private static final int[] BACKGROUND = new int[]{0};
	private static final int[] FOREGROUND = new int[]{2};


	public GameMap(int stageNum) {
		String tmxName = "map/stage" + stageNum + ".tmx";
		_tiledMap = new TmxMapLoader().load(tmxName);
		_tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);
		calcMapPixel();
		// object layer
		_objects = _tiledMap.getLayers().get("object").getObjects();
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("goal") && object instanceof RectangleMapObject) {
				_goalRect = ((RectangleMapObject) object).getRectangle();
				break;
			}
		}
	}

	public boolean reachGoal(Rectangle bounds) {
		return _goalRect.overlaps(bounds);
	}

	private void calcMapPixel() {
		MapProperties prop = _tiledMap.getProperties();
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		_mapPixelWidth = mapWidth * tilePixelWidth;
		_mapPixelHeight = mapHeight * tilePixelHeight;
	}

	public void render(OrthographicCamera camera) {
		_tiledMapRenderer.setView(camera);
		_tiledMapRenderer.render();
	}

	public void renderBackground(OrthographicCamera camera) {
		_tiledMapRenderer.setView(camera);
		_tiledMapRenderer.render(BACKGROUND);
	}

	public void renderForeground(OrthographicCamera camera) {
		_tiledMapRenderer.setView(camera);
		_tiledMapRenderer.render(FOREGROUND);
	}

	public MapLayer getLayer(int n) {
		return _tiledMap.getLayers().get(n);
	}

	public int getWidth() {
		return _mapPixelWidth;
	}

	public int getHeight() {
		return _mapPixelHeight;
	}

	public void dispose() {
		_tiledMap.dispose();
		_tiledMapRenderer.dispose();
	}

	public TiledMapTileLayer getCollisionLayer() {
		return (TiledMapTileLayer) _tiledMap.getLayers().get("foreground");
	}

	public boolean isCellPlatform(float x, float y) {
		int cellX = (int) (x / getCollisionLayer().getTileWidth());
		int cellY = (int) (y / getCollisionLayer().getTileHeight());

		TiledMapTileLayer.Cell cell = getCollisionLayer().getCell(cellX, cellY);
		if (cell == null) return false;
		if (cell.getTile() == null) return false;
		return cell.getTile().getProperties().containsKey("platform");
	}

	public float getMaxHeight() {
		return _mapPixelHeight - Ascend.GAME_HEIGHT / 2;
	}

	public Rectangle getGoalRect() {
		return _goalRect;
	}

	public void generateDevils(ArrayList<Devil> devils) {
		Rectangle rect;
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("devil")) {
				rect = ((RectangleMapObject) object).getRectangle();
				devils.add(new Devil(rect.getX(), rect.getY()));
			} else if (object.getProperties().containsKey("devil1")) {
				rect = ((RectangleMapObject) object).getRectangle();
				devils.add(new Devil1(rect.getX(), rect.getY()));
			} else if (object.getProperties().containsKey("fish")) {
				rect = ((RectangleMapObject) object).getRectangle();
				devils.add(new Fish(rect.getX(), rect.getY()));
			}
		}
	}

	public void generatePlatforms(ArrayList<com.mygdx.ascendjumper.MovingPlatform> platforms) {
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("moving_platform")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				platforms.add(new com.mygdx.ascendjumper.MovingPlatform(rect.getX(), rect.getY(), (int) rect.getWidth(), (int) rect.getHeight()));
			} else if(object.getProperties().containsKey("moving_platform1")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				platforms.add(new MovingPlatformAlpha(rect.getX(), rect.getY(), (int) rect.getWidth(), (int) rect.getHeight()));
			} else if (object.getProperties().containsKey("elevator")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				platforms.add(new com.mygdx.ascendjumper.Elevator(rect.getX(), rect.getY(), (int) rect.getWidth(), (int) rect.getHeight()));
			} else if (object.getProperties().containsKey("elevator1")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				platforms.add(new ElevatorAlpha(rect.getX(), rect.getY(), (int) rect.getWidth(), (int) rect.getHeight()));
			}
		}
	}

	public void generateCannons(ArrayList<com.mygdx.ascendjumper.Cannon> cannons) {
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("cannon")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				cannons.add(new com.mygdx.ascendjumper.Cannon(rect.getX(), rect.getY()));
			}
		}
	}

	public void generateKomainu(ArrayList<Komainu> komainus) {
		for (MapObject object : _objects) {
			if (object.getProperties().containsKey("komainu")) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				komainus.add(new Komainu(rect.getX(), rect.getY()));
			}
		}
	}

}
