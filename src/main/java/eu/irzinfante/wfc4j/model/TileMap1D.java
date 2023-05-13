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

package eu.irzinfante.wfc4j.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import eu.irzinfante.wfc4j.enums.Side1D;
import eu.irzinfante.wfc4j.exceptions.TileException;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.1.0
 */
public class TileMap1D<T> {

	private Set<Tile<T>> tileSet;
	private Map<Tile<T>, Set<Tile<T>>[]> adjacents;

	/**
	 * Creates a 1-dimensional tilemap with the given tileset
	 *
	 * @param	tileSet The set of tiles (i.e. tileset) for the tilemap
	 * @throws	TileException If tileSet is empty
	 * 
	 * @since	0.1.0
	 */
	public TileMap1D(Set<Tile<T>> tileSet) throws TileException {
		
		if(tileSet.isEmpty()) {
			throw new TileException("Set of tiles must be non-empty");
		}
		
		this.tileSet = tileSet;
		this.adjacents = new HashMap<>();
		
		tileSet.forEach(tile -> {
			this.adjacents.put(tile, (Set<Tile<T>>[]) new HashSet<?>[2]);
		});
	}
	
	/**
	 * Get the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to get the possible adjacent tiles
	 * @param	side The side of the given tile from which to get the possible adjacent tiles
	 * @return	Set of tiles that can be adjacent to the provided tile from the selected side
	 * @throws	TileException If the given tile is not in present in the tilemap's tileset
	 * 
	 * @since	0.1.0
	 */
	public Set<Tile<T>> getAdjacents(Tile<T> tile, Side1D side) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must be present in TileMap");
		}
		
		return this.adjacents.get(tile)[side.getValue()];
	}
	
	/**
	 * Set the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to set the possible adjacent tiles
	 * @param	side The side of the given tile for which to set the possible adjacent tiles
	 * @param	adjacents The set of tiles to be set as the possible adjacent tiles for the given tile
	 * @throws	TileException If the given tile or any of the potential adjacent tiles are not in present in the tilemap's tileset
	 * 
	 * @since	0.1.0
	 */
	public void setAdjacents(Tile<T> tile, Side1D side, Set<Tile<T>> adjacents) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must be present in TileMap");
		} else if(!this.tileSet.containsAll(adjacents)) {
			throw new TileException("All adjacent tiles must be present in TileMap");
		}
		
		this.adjacents.get(tile)[side.getValue()] = adjacents;
	}
	
	/**
	 * Add a single tile to the possible adjacent tiles to a specific side of a given tile
	 *
	 * @param	tile The given tile for which to add the possible adjacent tile
	 * @param	side The side of the given tile for which to add the possible adjacent tile
	 * @param	adjacent A single tile to be added to the possible adjacent tiles for the given tile
	 * @return	true if the adjacent tile to be added was not already contained in the set of adjacent sets
	 * @throws	TileException If the given tile or the potential adjacent tile to be added are not in present in the tilemap's tileset
	 * 
	 * @since	0.1.0
	 */
	public boolean addAdjacent(Tile<T> tile, Side1D side, Tile<T> adjacent) throws TileException {
		
		if(!this.tileSet.contains(tile)) {
			throw new TileException("Tile must be present in TileMap");
		} else if(!this.tileSet.contains(adjacent)) {
			throw new TileException("Adjacent tile must be present in TileMap");
		}
		
		return this.adjacents.get(tile)[side.getValue()].add(adjacent);
	}
	
	public Set<Tile<T>> getTileSet() {
		return this.tileSet;
	}
}