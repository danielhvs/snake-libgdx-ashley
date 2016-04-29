/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.TransformComponent;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<BoundsComponent> bm;

	public static interface CollisionListener {
		public void hit();
	}

	private Engine engine;
	private CollisionListener listener;
	private ImmutableArray<Entity> snakes;
	private ImmutableArray<Entity> platformComponents;

	public CollisionSystem(CollisionListener listener) {
		this.listener = listener;

		bm = ComponentMapper.getFor(BoundsComponent.class);
		ComponentMapper.getFor(TransformComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		snakes = engine.getEntitiesFor(
				Family.all(SnakeComponent.class, BoundsComponent.class, TransformComponent.class).get());
		platformComponents = engine.getEntitiesFor(
				Family.all(PlatformComponent.class, BoundsComponent.class, TransformComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		for (Entity snake : snakes) {
			for (Entity platform : platformComponents) {
				BoundsComponent snakeBound = bm.get(snake);
				BoundsComponent platformBound = bm.get(platform);
				System.out.println("snake: " + snakeBound);
				System.out.println("paltform: " + platformBound);
				if (snakeBound.bounds.overlaps(platformBound.bounds)) {
					listener.hit();
					snakeSystem.revert();
				}
			}
		}
	}
}
