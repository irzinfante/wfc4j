/**
 * Library to use the Wave Function Collapse strategy for procedural generation
 * Copyright (C) 2023 Iker Ruiz de Infante Gonzalez <iker@irzinfante.eu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.irzinfante.wfc4j.api;

import eu.irzinfante.wfc4j.core.AbstractWaveFunctionCollapse2D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell2D;
import eu.irzinfante.wfc4j.model.TileMap2D;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.2.0
 */
public class WaveFunctionCollapseToroidal2D<T> extends AbstractWaveFunctionCollapse2D<T> {
	
	/**
	 * Creates a 2-dimensional toroidal grid (borders are stitched) on which to apply the WFC algorithm with the specified tilemap
	 *
	 * @param	tileMap The to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (2-dimensional)
	 * @param	gridSizeY The size of the grid in the Y axis (2-dimensional)
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX or gridSizeY is less than one
	 * 
	 * @since	0.2.0
	 */
	public WaveFunctionCollapseToroidal2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY) throws TileException, DimensionException {
		super(tileMap, gridSizeX, gridSizeY);
	}
	
	/**
	 * Creates a 2-dimensional toroidal grid (borders are stitched) on which to apply the WFC algorithm with the specified tilemap,
	 * with the possibility to control the random results providing a seed
	 *
	 * @param	tileMap The to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (2-dimensional)
	 * @param	gridSizeY The size of the grid in the Y axis (2-dimensional)
	 * @param	seed Seed for random functions
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX or gridSizeY is less than one
	 * 
	 * @since	0.2.0
	 */
	public WaveFunctionCollapseToroidal2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY, long seed) throws TileException, DimensionException {
		super(tileMap, gridSizeX, gridSizeY, seed);
	}
	
	@Override
	protected boolean canUpdateLeftCell(int x, int y) {
		return this.getLeftCell(x, y).getTile() == null;
	}
	
	@Override
	protected Cell2D<T> getLeftCell(int x, int y) {
		if(x == 1) {
			return this.getCell(this.grid[y - 1].length, y);
		} else {
			return this.getCell(x - 1, y);
		}
	}
	
	@Override
	protected boolean canUpdateRightCell(int x, int y) {
		return this.getRightCell(x, y).getTile() == null;
	}
	
	@Override
	protected Cell2D<T> getRightCell(int x, int y) {
		if(x == this.grid[y - 1].length) {
			return this.getCell(1, y);
		} else {
			return this.getCell(x + 1, y);
		}
	}

	@Override
	protected boolean canUpdateBottomCell(int x, int y) {
		return this.getBottomCell(x, y).getTile() == null;
	}

	@Override
	protected Cell2D<T> getBottomCell(int x, int y) {
		if(y == this.grid.length) {
			return this.getCell(x, 1);
		} else {
			return this.getCell(x, y + 1);
		}
	}

	@Override
	protected boolean canUpdateTopCell(int x, int y) {
		return this.getTopCell(x, y).getTile() == null;
	}

	@Override
	protected Cell2D<T> getTopCell(int x, int y) {
		if(y == 1) {
			return this.getCell(x, this.grid.length);
		} else {
			return this.getCell(x, y - 1);
		}
	}
}