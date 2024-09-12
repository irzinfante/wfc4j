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

import dev.irzinfante.wfc4j.core.AbstractWFC1D;
import dev.irzinfante.wfc4j.enums.Side1D;
import dev.irzinfante.wfc4j.exceptions.TileException;
import dev.irzinfante.wfc4j.exceptions.DimensionException;
import dev.irzinfante.wfc4j.model.TileMap1D;
import dev.irzinfante.wfc4j.model.Tile;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version	1.0.0
 * @since	1.0.0
 */
final public class EuclideanWFC1D<T> extends AbstractWFC1D<T> {
	
	/**
	 * Creates a 1-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap
	 *
	 * @param	tileMap The tilemap to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (1-dimensional)
	 * @throws	TileException If tileMap is null
	 * @throws	DimensionException If gridSizeX is less than one
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public EuclideanWFC1D(TileMap1D<T> tileMap, int gridSizeX) throws TileException, DimensionException {
		super(tileMap, gridSizeX);
	}
	
	/**
	 * Creates a 1-dimensional euclidean grid on which to apply the WFC algorithm with the specified tilemap,
	 * initializing some cells with the given entropy
	 *
	 * @param	tileMap The tilemap to use for the WFC algorithm
	 * @param	gridSizeX The size of the grid in the X axis (1-dimensional)
	 * @param	initialEntropy Map from cell index to initial entropy
	 * @throws	TileException If tileMap is null or initial entropy is set with no tiles or tiles not existing in tilemap
	 * @throws	DimensionException If gridSizeX is less than one or initial entropy is set for cell outside the grid
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public EuclideanWFC1D(
		TileMap1D<T> tileMap,
		int gridSizeX,
		Map<Integer, Set<Tile<T>>> initialEntropy
	) throws TileException, DimensionException {
		super(tileMap, gridSizeX, initialEntropy);
	}

	@Override
	protected Integer getSideCellIndex(int cellIndex, Side1D side) {
		switch(side) {
			case Side1D.Left:
				return cellIndex - 1;
			case Side1D.Right:
				if(cellIndex < this.grid.length - 1) {
					return cellIndex + 1;
				} else {
					return -1;
				}
			default:
				return null;
		}
	}
}