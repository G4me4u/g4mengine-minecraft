package com.g4mesoft.minecraft.renderer;

import com.g4mesoft.graphics3d.AbstractPixelRenderer3D;
import com.g4mesoft.graphics3d.Vertex3D;
import com.g4mesoft.math.Mat4f;
import com.g4mesoft.math.MathUtils;
import com.g4mesoft.math.Vec3f;
import com.g4mesoft.math.Vec4f;
import com.g4mesoft.minecraft.world.BlockHitResult;
import com.g4mesoft.minecraft.world.Blocks;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;

public class BlockSelectionRenderer {

	private static final Vertex3D[] SELECTION_VERTICES = new Vertex3D[] {
		// FRONT
		new Vertex3D(new Vec4f( 1.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f, -0.0f,  1.0f,  1.0f)),
		
		new Vertex3D(new Vec4f( 1.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f,  1.0f,  1.0f)),

		// BACK
		new Vertex3D(new Vec4f(-0.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f, -0.0f, -0.0f,  1.0f)),
		
		new Vertex3D(new Vec4f(-0.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f, -0.0f,  1.0f)),

		// BOTTOM
		new Vertex3D(new Vec4f(-0.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f, -0.0f,  1.0f,  1.0f)),
		
		new Vertex3D(new Vec4f(-0.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f, -0.0f, -0.0f,  1.0f)),

		// TOP 
		new Vertex3D(new Vec4f(-0.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f, -0.0f,  1.0f)),
		
		new Vertex3D(new Vec4f(-0.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f,  1.0f,  1.0f)),
		
		// LEFT
		new Vertex3D(new Vec4f(-0.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f, -0.0f, -0.0f,  1.0f)),
		
		new Vertex3D(new Vec4f(-0.0f, -0.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f(-0.0f,  1.0f, -0.0f,  1.0f)),
		
		// RIGHT
		new Vertex3D(new Vec4f( 1.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f,  1.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f, -0.0f,  1.0f,  1.0f)),
		
		new Vertex3D(new Vec4f( 1.0f, -0.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f, -0.0f,  1.0f)),
		new Vertex3D(new Vec4f( 1.0f,  1.0f,  1.0f,  1.0f))
	};
	
	private final World world;
	private final WorldCamera camera;
	
	private final BlockSelectionShader3D shader;
	
	private float time;
	private float prevTime;

	public BlockSelectionRenderer(World world, WorldCamera camera) {
		this.world = world;
		this.camera = camera;
		
		shader = new BlockSelectionShader3D(camera);
	
		time = 0.0f;
	}
	
	public void update() {
		if (time > 2.0f * MathUtils.PI)
			time -= 2.0f * MathUtils.PI;

		prevTime = time;
		time += 0.5f;
	}

	public void render(AbstractPixelRenderer3D renderer3d, float dt) {
		Mat4f viewMatrix = camera.getViewMatrix().inverseCopy();
		
		Vec3f forward = new Vec3f(-viewMatrix.m20, -viewMatrix.m21, -viewMatrix.m22);
		BlockHitResult res = world.castBlockRay(viewMatrix.m30, viewMatrix.m31, viewMatrix.m32, forward);

		if (res != null) {
			BlockState state = world.getBlockState(res.blockPos);
			if (state.getBlock() != Blocks.AIR_BLOCK) {
				shader.setSelectionPosition(res.blockPos);
				
				float t = (MathUtils.sin(prevTime + (time - prevTime) * dt) + 1.0f) * 0.5f;
				shader.setSelectionAlpha(0x40 + (int)(0x3F * t));
				
				renderer3d.setShader(shader);
				renderer3d.drawVertices(SELECTION_VERTICES);
			}
		}
	}
}
