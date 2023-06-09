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

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.2.0
 */
public class Cell2D<T> implements Comparable<Cell2D<T>> {

	private Set<Tile<T>> entropy;
	private Tile<T> tile;
	private int x;
	private int y;
	
	/**
	 * Creates a 2-dimensional cell with the given entropy and coordinates
	 *
	 * @param	entropy The default entropy (all possible tile values) for the cell
	 * @param	x The X component of the cell's coordinates in a 2-dimensional grid
	 * @param	y The Y component of the cell's coordinates in a 2-dimensional grid
	 * 
	 * @since	0.2.0
	 */
	public Cell2D(Set<Tile<T>> entropy, int x, int y) {
		this.entropy = entropy;
		this.tile = null;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the set intersection between the cell's current entropy and the intersectant set of tiles
	 *
	 * @param	intersectant The set of tiles to be intersected with the cell's current entropy
	 * @return	The set intersection between the current cell entropy and the intersectant set
	 * 
	 * @since	0.2.0
	 */
	public Set<Tile<T>> getReducedEntropy(Set<Tile<T>> intersectant) {
		var reducedEntropy = new HashSet<>(this.entropy);
		reducedEntropy.retainAll(intersectant);
		return reducedEntropy;
	}
	
	public Set<Tile<T>> getEntropy() {
		return this.entropy;
	}
	
	public void setEntropy(Set<Tile<T>> entropy) {
		this.entropy = entropy;
	}
	
	public Tile<T> getTile() {
		return this.tile;
	}
	
	public void setTile(Tile<T> tile) {
		this.tile = tile;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	@Override
	public int compareTo(Cell2D<T> other) {
		return this.entropy.size() - other.entropy.size();
	}
}