package com.yourfinger.game.UIScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yourfinger.game.Main;
import com.yourfinger.game.GameScreens.ComixScreen;
import com.yourfinger.game.GameScreens.FinalScriptScreen;
import com.yourfinger.game.GameScreens.GameAlphaScreen;
import com.yourfinger.game.GameScreens.GameExtraScreen;
import com.yourfinger.game.Helpers.PrefBuilder;
import com.yourfinger.game.ImportantClasses.Constants;

public class ChooseFinalScreen implements Screen {

	final float RELATIVE_SIZE_OF_MANAGE_BUTTON = 0.22f;
	final float SIZE_OF_ALPHA_BUTTON = 0.5f;
	final float SIZE_OF_COMIX_BUTTON = 0.25f;
	final float SIZE_OF_FINAL_BUTTON = 0.3f;
	final float RELATIVE_SIZE_OF_LABEL = 0.22f;

	float vertical_indent;

	// ключевые объекты
	Main game;
	TextureAtlas atlas;
	Stage stage;
	Label lblWorldName;

	// фон
	Sprite sprBackground;

	// кнопки
	Button btnPrevious;
	Button btnBack;

	Button btnPrevAlpfaComix;
	Button btnPostAlphaComix;
	Button btnFinalComix;
	Button btnAlpha;
	Button btnFinal;



	public ChooseFinalScreen(Main game) {
		this.game = game;
		atlas = game.getChooseAtlas(6);
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()), game.GetBatch());

		// вычисляем отступы, исходя из размеров экрана
		vertical_indent = ComputeVerticalIndent();

		// создание всех эдементов
		CreateBackgroundSprite();
		CreateManageButtons();
		CreateMainTopButtons();
		CreateMainBottomButtons();
		CreateLabelWorldName();

		stage.addActor(btnPrevious);
		stage.addActor(btnBack);
		stage.addActor(btnAlpha);
		stage.addActor(btnPrevAlpfaComix);
		stage.addActor(btnFinal);
		stage.addActor(btnPostAlphaComix);
		stage.addActor(btnFinalComix);
		stage.addActor(lblWorldName);

		// для реагирования нажатия на кнопки
		Gdx.input.setInputProcessor(stage);
	}



	@Override
	public void render(float delta) {
		// чистим экран и устанавливавем фон
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.GetBatch().begin();
		sprBackground.draw(game.GetBatch());
		game.GetBatch().end();
		stage.draw();
	}



	@Override
	public void dispose() {
		stage.dispose();

	}



	float ComputeVerticalIndent() {

		return (Gdx.graphics.getHeight() - Gdx.graphics.getWidth()
				* (SIZE_OF_ALPHA_BUTTON + SIZE_OF_FINAL_BUTTON + RELATIVE_SIZE_OF_MANAGE_BUTTON)) / 3;

	}



	void CreateLabelWorldName() {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = game.GetFont().labelFont;
		lblWorldName = new Label(game.GetString("final"), labelStyle);
		lblWorldName.setWidth(Gdx.graphics.getWidth());
		lblWorldName.setWrap(true);
		lblWorldName.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() - lblWorldName.getHeight() / 2);

		lblWorldName.setAlignment(Align.center);
		lblWorldName.setColor(game.GetTextColor(6));
	}



	// ///////////////////////////////////
	// //МЕТОДЫ СОЗДАНИЯ ОБЪЕКТОВ///////////
	void CreateBackgroundSprite() {
		// создаем фон
		sprBackground = new Sprite(atlas.findRegion("background"));
		sprBackground.setPosition(0, 0);
		sprBackground.setSize(Gdx.graphics.getWidth(),
				Constants.ASPECT_RATIO_OF_BACKGROUND * Gdx.graphics.getWidth());
	}



	void CreateManageButtons() {
		Skin skin = new Skin();
		skin.addRegions(atlas);

		ButtonStyle buttonStylePrevious = new ButtonStyle();
		buttonStylePrevious.up = skin.getDrawable("previous_up");
		buttonStylePrevious.down = skin.getDrawable("previous_down");
		buttonStylePrevious.checked = skin.getDrawable("previous_up");
		btnPrevious = new Button(buttonStylePrevious);
		btnPrevious.setSize(Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnPrevious.setCenterPosition(Gdx.graphics.getWidth() * 0.15f,
				btnPrevious.getHeight() * 0.53f);

		btnPrevious.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnPrevious.getWidth())
						&& (y < btnPrevious.getHeight()) && (x > 0) && (y > 0)) {
					// пред мир
					game.setScreen(new ChooseLevelScreen(game, 5));
				}
			}
		});

		ButtonStyle buttonStyleBack = new ButtonStyle();
		buttonStyleBack.up = skin.getDrawable("back_up");
		buttonStyleBack.down = skin.getDrawable("back_down");
		buttonStyleBack.checked = skin.getDrawable("back_up");
		btnBack = new Button(buttonStyleBack);
		btnBack.setSize(Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON, Gdx.graphics.getWidth()
				* this.RELATIVE_SIZE_OF_MANAGE_BUTTON);
		btnBack.setCenterPosition(Gdx.graphics.getWidth() * 0.85f,
				btnBack.getHeight() / 2);

		btnBack.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnBack.getWidth()) && (y < btnBack.getHeight())
						&& (x > 0) && (y > 0)) {
					game.setScreen(new MainMenuScreen(game));
					dispose();
				}
			}
		});

	}



	void CreateMainTopButtons() {
		Skin skin = new Skin();
		skin.addRegions(atlas);

		// кнопка alpha
		ButtonStyle buttonStyleAlpha = new ButtonStyle();
		buttonStyleAlpha.up = skin.getDrawable("alpha_up");
		buttonStyleAlpha.down = skin.getDrawable("alpha_down");
		buttonStyleAlpha.checked = skin.getDrawable("alpha_up");

		btnAlpha = new Button(buttonStyleAlpha);
		btnAlpha.setSize(Gdx.graphics.getWidth() * SIZE_OF_ALPHA_BUTTON,
				Gdx.graphics.getWidth() * SIZE_OF_ALPHA_BUTTON);

		btnAlpha.setCenterPosition(Gdx.graphics.getWidth()
				* (0.95f - SIZE_OF_ALPHA_BUTTON / 2), Gdx.graphics.getHeight()
				- btnAlpha.getHeight() / 2 - vertical_indent);

		btnAlpha.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnAlpha.getWidth()) && (y < btnAlpha.getHeight())
						&& (x > 0) && (y > 0)) {
					// битва с Альфой
					game.setScreen(new GameAlphaScreen(game));
				}
			}
		});

		// комикс перед Альфой
		ButtonStyle buttonStyleComix = new ButtonStyle();
		buttonStyleComix.up = skin.getDrawable("comix_up");
		buttonStyleComix.down = skin.getDrawable("comix_down");
		buttonStyleComix.checked = skin.getDrawable("comix_up");

		btnPrevAlpfaComix = new Button(buttonStyleComix);
		btnPrevAlpfaComix.setSize(Gdx.graphics.getWidth()
				* SIZE_OF_COMIX_BUTTON, Gdx.graphics.getWidth()
				* SIZE_OF_COMIX_BUTTON);
		btnPrevAlpfaComix.setCenterPosition(btnAlpha.getX() / 2,
				btnAlpha.getCenterY());

		btnPrevAlpfaComix.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnPrevAlpfaComix.getWidth())
						&& (y < btnPrevAlpfaComix.getHeight()) && (x > 0)
						&& (y > 0)) {
					// комикс перед Альфой
						game.setScreen(new ComixScreen(game, 6, 1));
				}
			}
		});

	}



	void CreateMainBottomButtons() {
		Skin skin = new Skin();
		skin.addRegions(atlas);

		// кнопка final
		ButtonStyle buttonStyleFinal = new ButtonStyle();
		if ((PrefBuilder.GetComleteLevels() > 0)) {
			buttonStyleFinal.up = skin.getDrawable("final_up");
			buttonStyleFinal.down = skin.getDrawable("final_down");
			buttonStyleFinal.checked = skin.getDrawable("final_up");
		} else {
			buttonStyleFinal.up = skin.getDrawable("block_up");
			buttonStyleFinal.down = skin.getDrawable("block_down");
			buttonStyleFinal.checked = skin.getDrawable("block_up");
		}

		btnFinal = new Button(buttonStyleFinal);
		btnFinal.setSize(Gdx.graphics.getWidth() * SIZE_OF_FINAL_BUTTON,
				Gdx.graphics.getWidth() * SIZE_OF_FINAL_BUTTON);
		btnFinal.setCenterPosition(Gdx.graphics.getWidth() / 2, btnAlpha.getY()
				- vertical_indent - btnFinal.getHeight() / 2);
		btnFinal.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnFinal.getWidth()) && (y < btnFinal.getHeight())
						&& (x > 0) && (y > 0)) {
					// финальный уровень
					if ((PrefBuilder.GetComleteLevels() > 0)) {
						game.setScreen(new GameExtraScreen(game));
					}
				}
			}
		});

		// комиксы
		ButtonStyle buttonStyleComix = new ButtonStyle();
		buttonStyleComix.up = skin.getDrawable("comix_up");
		buttonStyleComix.down = skin.getDrawable("comix_down");
		buttonStyleComix.checked = skin.getDrawable("comix_up");

		// блок
		ButtonStyle buttonStyleBlock = new ButtonStyle();
		buttonStyleBlock.up = skin.getDrawable("block_up");
		buttonStyleBlock.down = skin.getDrawable("block_down");
		buttonStyleBlock.checked = skin.getDrawable("block_up");

		if (PrefBuilder.GetComleteLevels() > 0)
			btnPostAlphaComix = new Button(buttonStyleComix);
		else
			btnPostAlphaComix = new Button(buttonStyleBlock);
		btnPostAlphaComix.setSize(Gdx.graphics.getWidth()
				* SIZE_OF_COMIX_BUTTON, Gdx.graphics.getWidth()
				* SIZE_OF_COMIX_BUTTON);
		btnPostAlphaComix.setCenterPosition(btnFinal.getX() / 2,
				btnFinal.getCenterY());

		btnPostAlphaComix.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnPostAlphaComix.getWidth())
						&& (y < btnPostAlphaComix.getHeight()) && (x > 0)
						&& (y > 0)) {
					// комикс после Альфы
					if (PrefBuilder.GetComleteLevels() > 0)
					game.setScreen(new ComixScreen(game, 6, 2));
				}
			}
		});

		if (PrefBuilder.GetComleteLevels() > 1)
			btnFinalComix = new Button(buttonStyleComix);
		else
			btnFinalComix = new Button(buttonStyleBlock);
		btnFinalComix.setSize(Gdx.graphics.getWidth() * SIZE_OF_COMIX_BUTTON,
				Gdx.graphics.getWidth() * SIZE_OF_COMIX_BUTTON);
		btnFinalComix.setCenterPosition(
				(btnFinal.getX() + btnFinal.getWidth() + Gdx.graphics
						.getWidth()) / 2, btnFinal.getCenterY());

		btnFinalComix.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.input.vibrate(20);
				return true;
			};



			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if ((x < btnFinalComix.getWidth())
						&& (y < btnFinalComix.getHeight()) && (x > 0)
						&& (y > 0)) {
					// комикс финальный
					if (PrefBuilder.GetComleteLevels() > 1)
					game.setScreen(new FinalScriptScreen(game));
				}
			}
		});
	}



	// ///////////////////////////////////////////////////////
	// ///////////////////////////////////////////

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}



	@Override
	public void show() {
		// TODO Auto-generated method stub

	}



	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}



	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}



	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
