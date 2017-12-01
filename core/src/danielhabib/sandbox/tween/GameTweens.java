package danielhabib.sandbox.tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class GameTweens {

	public static void fadeIn(Actor actor, TweenManager tweenManager) {
		float duration = .5f;
		Timeline.createSequence().beginParallel().push(Tween.set(actor, ActorAcessor.ALPHA).target(0))
				.push(Tween.to(actor, ActorAcessor.ALPHA, duration).target(1)).end().start(tweenManager);
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

}
