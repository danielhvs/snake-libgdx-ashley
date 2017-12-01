package danielhabib.sandbox.tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class GameTweens {

	public static void fadeIn(Button button, TweenManager tweenManager) {
		float duration = .75f;
		Timeline.createSequence().beginParallel().push(Tween.set(button, ButtonAcessor.ALPHA).target(0))
				.push(Tween.to(button, ButtonAcessor.ALPHA, duration).target(1)).end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

}
