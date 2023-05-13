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

import eu.irzinfante.wfc4j.core.AbstractWaveFunctionCollapse1D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell1D;
import eu.irzinfante.wfc4j.model.TileMap1D;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.1.0
 */
public class WaveFunctionCollapseEuclidean1D<T> extends AbstractWaveFunctionCollapse1D<T> {
	
	/**
	 * Creates a 1-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap
	 *
	 * @param	tileMap The to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (1-dimensional)
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX is less than one
	 * 
	 * @since	0.1.0
	 */
	public WaveFunctionCollapseEuclidean1D(TileMap1D<T> tileMap, int gridSizeX) throws TileException, DimensionException {
		super(tileMap, gridSizeX);
	}
	
	/**
	 * Creates a 1-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap,
	 * with the possibility to control the random results providing a seed
	 *
	 * @param	tileMap The to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (1-dimensional)
	 * @param	seed Seed for random functions
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX is less than one
	 * 
	 * @since	0.1.0
	 */
	public WaveFunctionCollapseEuclidean1D(TileMap1D<T> tileMap, int gridSizeX, long seed) throws TileException, DimensionException {
		super(tileMap, gridSizeX, seed);
	}
	
	@Override
	protected boolean canUpdateLeftCell(int x) {
		return x - 1 >= 1 && this.getLeftCell(x).getTile() == null;
	}
	
	@Override
	protected Cell1D<T> getLeftCell(int x) {
		if(x == 1) {
			return null;
		} else {
			return this.getCell(x - 1);
		}
	}
	
	@Override
	protected boolean canUpdateRightCell(int x) {
		return x + 1 <= this.grid.length && this.getRightCell(x).getTile() == null;
	}
	
	@Override
	protected Cell1D<T> getRightCell(int x) {
		if(x == this.grid.length) {
			return null;
		} else {
			return this.getCell(x + 1);
		}
	}
}