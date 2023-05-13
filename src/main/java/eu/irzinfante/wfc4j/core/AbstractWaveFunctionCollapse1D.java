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

package eu.irzinfante.wfc4j.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import eu.irzinfante.wfc4j.enums.Side1D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell1D;
import eu.irzinfante.wfc4j.model.Tile;
import eu.irzinfante.wfc4j.model.TileMap1D;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.1.0
 */
abstract public class AbstractWaveFunctionCollapse1D<T> {

	protected TileMap1D<T> tileMap;
	protected Cell1D<T>[] grid;
	protected Set<Cell1D<T>> collapsableCells;
	
	protected Random random;
	protected long deepth;
	
	protected AbstractWaveFunctionCollapse1D(TileMap1D<T> tileMap, int gridSizeX) throws TileException, DimensionException {
		
		if(tileMap == null) {
			throw new TileException("TileMap cannot be null");
		} else if(gridSizeX < 1) {
			throw new DimensionException("Invalid grid size");
		}
		
		this.tileMap = tileMap;
		this.grid = (Cell1D<T>[]) new Cell1D<?>[gridSizeX];
		this.collapsableCells = new HashSet<>();
		
		this.random = new Random();
		this.deepth = 0L;
		
		this.clear();
	}
	
	protected AbstractWaveFunctionCollapse1D(TileMap1D<T> tileMap, int gridSizeX, long seed) throws TileException, DimensionException {
		this(tileMap, gridSizeX);
		this.random = new Random(seed);
	}
	
	/**
	 * Clears the cells of the entire grid
	 * 
	 * @since	0.1.0
	 */
	public void clear() {
		for(int x =  1; x <= this.grid.length; x++) {
			this.grid[x - 1] = new Cell1D<>(this.tileMap.getTileSet(), x);
		}
	}
	
	/**
	 * Sets a cell on the grid if: the cell is not null, the cell has no value assigned
	 * and the entropy of the cell is not empty
	 *
	 * @param 	cell The cell to be set in the grid
	 * @throws	DimensionException If the cell coordinates are out of the range of the grid
	 * 
	 * @since	0.1.0
	 */
	public void setCellConstraint(Cell1D<T> cell) throws DimensionException {
		
		if(cell != null && cell.getTile() == null && !cell.getEntropy().isEmpty()) {
			
			var x = cell.getX();
			if(x < 1 || x > this.grid.length) {
				throw new DimensionException("Invalid cell coordinates");
			}
			
			this.grid[x - 1] = cell;
			this.collapsableCells.add(cell);
		}
	}
	
	/**
	 * Runs the WFC algorithm to populate the tile values for the cells of the grid
	 * 
	 * @throws	TileException If exception occurs at the time of getting the adjacent tiles for some cell's value
	 * 
	 * @since	0.1.0
	 */
	public Cell1D<T>[] generate() throws TileException {
		return this.generate(Long.MAX_VALUE);
	}
	
	/**
	 * Runs the WFC algorithm to populate the tile values for the cells of the grid until the specified recursion level
	 * 
	 * @param	maxDeepth Maximum level of recursion
	 * @throws	TileException If exception occurs at the time of getting the adjacent tiles for some cell's value
	 * 
	 * @since	0.1.0
	 */
	public Cell1D<T>[] generate(long maxDeepth) throws TileException {
		
		Cell1D<T> cell;
		if(this.collapsableCells.isEmpty()) {
			cell = this.getCell(this.random.nextInt(this.grid.length) + 1);
		} else {
			cell = this.collapsableCellsToSortedList().get(0);
			this.collapsableCells.remove(cell);
		}
		
		if(this.collapseCell(cell, maxDeepth)) {
			return Arrays.copyOf(this.grid, this.grid.length);
		} else {
			return null;
		}
	}
	
	protected Cell1D<T> getCell(int x) {
		return this.grid[x - 1];
	}
	
	protected List<Cell1D<T>> collapsableCellsToSortedList() {
		var collapsableCellsList = new ArrayList<>(this.collapsableCells);
		Collections.shuffle(collapsableCellsList, this.random);
		Collections.sort(collapsableCellsList);
		return collapsableCellsList;
	}
	
	protected boolean collapseCell(Cell1D<T> cell, long maxDeepth) throws TileException {
		this.deepth++;
		
		if(this.deepth <= maxDeepth) {
			var entropy = cell.getEntropy();
			if(!entropy.isEmpty()) {
				var options = new ArrayList<>(entropy);
				Collections.shuffle(options, this.random);
	
				cell.setEntropy(new HashSet<Tile<T>>());
				for(var tile : options) {
					cell.setTile(tile);
					
					if(this.updateAdjacentCellsEntropy(cell)) {
						
						if(this.collapsableCells.isEmpty()) {
							this.deepth--;
							return true;
						}
						
						for(var collapsableCell : this.collapsableCellsToSortedList()) {
							this.collapsableCells.remove(collapsableCell);
							if(this.collapseCell(collapsableCell, maxDeepth)) {
								this.deepth--;
								return true;
							}
							this.collapsableCells.add(collapsableCell);
						}
					}
				}
				
				cell.setEntropy(entropy);
				cell.setTile(null);
			}
			
			this.deepth--;
			return false;
		} else {
			this.deepth--;
			return true;
		}
	}
	
	protected boolean updateAdjacentCellsEntropy(Cell1D<T> currentCell) throws TileException {
		var x = currentCell.getX();
		
		var valid = true;
		
		var newEntropyLeft = new HashSet<Tile<T>>();
		if(valid && this.canUpdateLeftCell(x)) {
			newEntropyLeft.addAll(this.getLeftCell(x).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side1D.Left)));
			if(newEntropyLeft.isEmpty()) {
				valid = false;
			}
		}
		
		var newEntropyRight = new HashSet<Tile<T>>();
		if(valid && this.canUpdateRightCell(x)) {
			newEntropyRight.addAll(this.getRightCell(x).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side1D.Right)));
			if(newEntropyRight.isEmpty()) {
				valid = false;
			}
		}
		
		if(valid) {
			if(this.canUpdateLeftCell(x)) {
				this.getLeftCell(x).setEntropy(newEntropyLeft);
				this.collapsableCells.add(this.getLeftCell(x));
			}
			
			if(this.canUpdateRightCell(x)) {
				this.getRightCell(x).setEntropy(newEntropyRight);
				this.collapsableCells.add(this.getRightCell(x));
			}
		}
		
		return valid;
	}
	
	abstract protected boolean canUpdateLeftCell(int x);
	
	abstract protected Cell1D<T> getLeftCell(int x);
	
	abstract protected boolean canUpdateRightCell(int x);
	
	abstract protected Cell1D<T> getRightCell(int x);
}