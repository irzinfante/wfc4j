/**
 * Library to use the Wave Function Collapse strategy for procedural generation
 * Copyright (C) 2023-2024 Iker Ruiz de Infante Gonzalez <iker@irzinfante.dev>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.irzinfante.wfc4j.api;

import java.util.Map;
import java.util.Set;

import dev.irzinfante.wfc4j.core.AbstractWFC2D;
import dev.irzinfante.wfc4j.enums.Side2D;
import dev.irzinfante.wfc4j.exceptions.TileException;
import dev.irzinfante.wfc4j.exceptions.DimensionException;
import dev.irzinfante.wfc4j.model.TileMap2D;
import dev.irzinfante.wfc4j.model.Tile;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version	1.0.0
 * @since	1.0.0
 */
final public class EuclideanWFC2D<T> extends AbstractWFC2D<T> {
	
	/**
	 * Creates a 2-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap
	 *
	 * @param	tileMap The tilemap to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis
	 * @param	gridSizeX The size of the grid in the Y axis
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX or gridSizeY is less than one
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public EuclideanWFC2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY) throws TileException, DimensionException {
		super(tileMap, gridSizeX, gridSizeY);
	}
	
	/**
	 * Creates a 2-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap,
	 * initializing some cells with the given entropy
	 *
	 * @param	tileMap The tilemap to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis
	 * @param	gridSizeY The size of the grid in the Y axis
	 * @param	initialEntropy Map from cell index to initial entropy
	 * @throws	TileException If tileMap is null or initial entropy is set with no tiles or tiles not existing in tilemap
	 * @throws	DimensionException If gridSizeX or gridSizeY is less than one or initial entropy is set for cell outside the grid
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public EuclideanWFC2D(
		TileMap2D<T> tileMap,
		int gridSizeX,
		int gridSizeY,
		Map<Integer[], Set<Tile<T>>> initialEntropy
	) throws TileException, DimensionException {
		super(tileMap, gridSizeX, gridSizeY, initialEntropy);
	}

	@Override
	protected Integer[] getSideCellIndex(Integer[] cellIndex, Side2D side) {
		switch(side) {
			case Side2D.Left:
				return new Integer[] {cellIndex[0], cellIndex[1] - 1};
			case Side2D.Right:
				if(cellIndex[1] < this.grid[cellIndex[0]].length - 1) {
					return new Integer[] {cellIndex[0], cellIndex[1] + 1};
				} else {
					return new Integer[] {cellIndex[0], -1};
				}
			case Side2D.Bottom:
				if(cellIndex[0] < this.grid.length - 1) {
					return new Integer[] {cellIndex[0] + 1, cellIndex[1]};
				} else {
					return new Integer[] {-1, cellIndex[1]};
				}
			case Side2D.Top:
				return new Integer[] {cellIndex[0] - 1, cellIndex[1]};
			default:
				return null;
		}
	}
}