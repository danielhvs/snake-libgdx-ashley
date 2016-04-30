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
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.TransformComponent;

public class PlatformSystem extends IteratingSystem {
	private static final Family family = Family.all(PlatformComponent.class, TransformComponent.class).get();
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<PlatformComponent> pm;

	public PlatformSystem() {
		super(family);

		tm = ComponentMapper.getFor(TransformComponent.class);
		pm = ComponentMapper.getFor(PlatformComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent pos = tm.get(entity);
		pos.rotation += pm.get(entity).rotation;
	}

}
