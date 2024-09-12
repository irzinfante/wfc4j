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

package dev.irzinfante.wfc4j.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

import dev.irzinfante.wfc4j.enums.Side2D;
import dev.irzinfante.wfc4j.exceptions.DimensionException;
import dev.irzinfante.wfc4j.exceptions.TileException;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version	1.0.0
 * @since	1.0.0
 */
final public class TileMap2D<T> {

	private List<Tile<T>> tileSet;
	private Map<Integer, Long[]> adjacents;

	/**
	 * Creates a tilemap for 2-dimensional tiles with the given tileset
	 *
	 * @param	tileSet The set of tiles (i.e. tileset) for the tilemap
	 * @throws	TileException If tileSet is empty
	 * @throws	DimensionException If tileSet is too big
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public TileMap2D(Set<Tile<T>> tileSet) throws TileException, DimensionException {
		
		if(tileSet.isEmpty()) {
			throw new TileException("Set of tiles cannot be empty");
		} else if(tileSet.size() > Long.SIZE) {
			throw new DimensionException(String.format("Set of tiles too big: cannot exceed %d tiles", Long.SIZE));
		}
		
		this.tileSet = new ArrayList<>(tileSet);
		this.adjacents = new HashMap<>();

		tileSet.forEach(tile -> {
			this.adjacents.put(this.tileSet.indexOf(tile), new Long[Side2D.values().length]);
		});
	}
	
	/**
	 * Get the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to get the possible adjacent tiles
	 * @param	side The side of the given tile from which to get the possible adjacent tiles
	 * @return	Binary encoded list tiles that can be adjacent to the provided tile from the selected side
	 * @throws	TileException If the given tile doesn't exist in tilemap
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public long getAdjacents(Tile<T> tile, Side2D side) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must exist in tilemap");
		}
		
		return this.adjacents.get(this.tileSet.indexOf(tile))[side.getValue()];
	}
	
	/**
	 * Set the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to set the possible adjacent tiles
	 * @param	side The side of the given tile for which to set the possible adjacent tiles
	 * @param	adjacents The set of tiles to be set as the possible adjacent tiles for the given tile
	 * @throws	TileException If the given tile or any of the potential adjacent tiles don't exist in tilemap
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public void setAdjacents(Tile<T> tile, Side2D side, Set<Tile<T>> adjacents) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must be exist in tilemap");
		} else if(!this.tileSet.containsAll(adjacents)) {
			throw new TileException("All adjacent tiles must exist in tilemap");
		}

		var bynaryAdjacents = 0L;
		for(var adjacent : adjacents) {
			bynaryAdjacents += (1L << tileSet.indexOf(adjacent));
		}
		
		this.adjacents.get(tileSet.indexOf(tile))[side.getValue()] = bynaryAdjacents;
	}
	
	/**
	 * Add a single tile to the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to add the possible adjacent tile
	 * @param	side The side of the given tile for which to add the possible adjacent tile
	 * @param	adjacent A single tile to be added to the possible adjacent tiles for the given tile
	 * @throws	TileException If the given tile or the potential adjacent tile to be added don't exist in tilemap
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public void addAdjacent(Tile<T> tile, Side2D side, Tile<T> adjacent) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must exist in tilemap");
		} else if(!this.tileSet.contains(adjacent)) {
			throw new TileException("Adjacent tile must exist in tilemap");
		}
		
		var tileIndex = this.tileSet.indexOf(adjacent);
		this.adjacents.get(tileIndex)[side.getValue()] |= (1L << tileIndex);
	}
	
	public List<Tile<T>> getTileSet() {
		return this.tileSet;
	}
}