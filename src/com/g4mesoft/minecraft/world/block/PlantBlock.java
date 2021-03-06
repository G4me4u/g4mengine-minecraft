package com.g4mesoft.minecraft.world.block;

import com.g4mesoft.minecraft.renderer.BlockTextures;
import com.g4mesoft.minecraft.renderer.RenderLayer;
import com.g4mesoft.minecraft.renderer.tessellator.CropBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.IBlockModel;
import com.g4mesoft.minecraft.renderer.tessellator.PlantBlockModel;
import com.g4mesoft.minecraft.world.World;
import com.g4mesoft.minecraft.world.block.state.BlockState;
import com.g4mesoft.minecraft.world.block.state.EnumBlockProperty;
import com.g4mesoft.minecraft.world.block.state.IBlockProperty;

public class PlantBlock extends Block {

	public static final IBlockProperty<PlantType> PLANT_TYPE_PROPERTY = 
			new EnumBlockProperty<PlantType>("type", PlantType.PLANT_TYPES);
	
	private final IBlockModel[] models;
	
	public PlantBlock() {
		models = new IBlockModel[PlantType.PLANT_TYPES.length];
		
		models[PlantType.GRASS.getIndex()] = new PlantBlockModel(BlockTextures.GRASS_PLANT_TEXTURE);
		models[PlantType.FORGETMENOT.getIndex()] = new CropBlockModel(BlockTextures.FORGETMENOT_PLANT_TEXTURE);
		models[PlantType.MARIGOLD.getIndex()] = new PlantBlockModel(BlockTextures.MARIGOLD_PLANT_TEXTURE);
		models[PlantType.DAISY.getIndex()] = new PlantBlockModel(BlockTextures.DAISY_PLANT_TEXTURE);
	}
	
	@Override
	public IBlockModel getModel(World world, IBlockPosition pos, BlockState blockState) {
		return models[blockState.getValue(PLANT_TYPE_PROPERTY).getIndex()];
	}

	@Override
	public RenderLayer getLayer(World world, IBlockPosition pos, BlockState blockState) {
		return RenderLayer.SPRITE_LAYER;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	protected BlockState createDefaultState() {
		return BlockState.createStateTree(this, PLANT_TYPE_PROPERTY);
	}
}
