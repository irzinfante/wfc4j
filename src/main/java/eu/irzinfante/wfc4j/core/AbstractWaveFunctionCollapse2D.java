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

import eu.irzinfante.wfc4j.enums.Side2D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell2D;
import eu.irzinfante.wfc4j.model.Tile;
import eu.irzinfante.wfc4j.model.TileMap2D;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.2.0
 */
abstract public class AbstractWaveFunctionCollapse2D<T> {

	protected TileMap2D<T> tileMap;
	protected Cell2D<T>[][] grid;
	protected Set<Cell2D<T>> collapsableCells;
	
	protected Random random;
	protected long deepth;
	
	protected AbstractWaveFunctionCollapse2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY) throws TileException, DimensionException {
		
		if(tileMap == null) {
			throw new TileException("TileMap cannot be null");
		} else if(gridSizeX < 1 || gridSizeY < 1) {
			throw new DimensionException("Invalid grid size");
		}
		
		this.tileMap = tileMap;
		this.grid = (Cell2D<T>[][]) new Cell2D<?>[gridSizeY][gridSizeX];
		this.collapsableCells = new HashSet<>();
		
		this.random = new Random();
		this.deepth = 0L;
		
		this.clear();
	}
	
	protected AbstractWaveFunctionCollapse2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY, long seed) throws TileException, DimensionException {
		this(tileMap, gridSizeX, gridSizeY);
		this.random = new Random(seed);
	}
	
	/**
	 * Clears the cells of the entire grid
	 * 
	 * @since	0.2.0
	 */
	public void clear() {
		for(int y =  1; y <= this.grid.length; y++) {
			for(int x =  1; x <= this.grid[y - 1].length; x++) {
				this.grid[y - 1][x - 1] = new Cell2D<>(this.tileMap.getTileSet(), x, y);
			}
		}
	}
	
	/**
	 * Sets a cell on the grid if: the cell is not null, the cell has no value assigned
	 * and the entropy of the cell is not empty
	 *
	 * @param 	cell The cell to be set in the grid
	 * @throws	DimensionException If the cell coordinates are out of the range of the grid
	 * 
	 * @since	0.2.0
	 */
	public void setCellConstraint(Cell2D<T> cell) throws DimensionException {
		
		if(cell != null && cell.getTile() == null && !cell.getEntropy().isEmpty()) {
			
			var x = cell.getX();
			var y = cell.getY();
			if(y < 1 || y > this.grid.length) {
				throw new DimensionException("Invalid cell y coordinate");
			} else if(x < 1 || x > this.grid[y - 1].length) {
				throw new DimensionException("Invalid cell x coordinate");
			}
			
			this.grid[y - 1][x - 1] = cell;
			this.collapsableCells.add(cell);
		}
	}
	
	/**
	 * Runs the WFC algorithm to populate the tile values for the cells of the grid
	 * 
	 * @return	A copy of the grid populated with tiles
	 * @throws	TileException If exception occurs at the time of getting the adjacent tiles for some cell's value
	 * 
	 * @since	0.2.0
	 */
	public Cell2D<T>[][] generate() throws TileException {
		return this.generate(Long.MAX_VALUE);
	}
	
	/**
	 * Runs the WFC algorithm to populate the tile values for the cells of the grid until the specified recursion level
	 * 
	 * @param	maxDeepth Maximum level of recursion
	 * @return	A copy of the grid populated with tiles
	 * @throws	TileException If exception occurs at the time of getting the adjacent tiles for some cell's value
	 * 
	 * @since	0.2.0
	 */
	public Cell2D<T>[][] generate(long maxDeepth) throws TileException {
		
		Cell2D<T> cell;
		if(this.collapsableCells.isEmpty()) {
			var y = this.random.nextInt(this.grid.length) + 1;
			var x = this.random.nextInt(this.grid[y - 1].length) + 1;
			cell = this.getCell(x, y);
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
	
	protected Cell2D<T> getCell(int x, int y) {
		return this.grid[y - 1][x - 1];
	}
	
	protected List<Cell2D<T>> collapsableCellsToSortedList() {
		var collapsableCellsList = new ArrayList<>(this.collapsableCells);
		Collections.shuffle(collapsableCellsList, this.random);
		Collections.sort(collapsableCellsList);
		return collapsableCellsList;
	}
	
	protected boolean collapseCell(Cell2D<T> cell, long maxDeepth) throws TileException {
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
	
	protected boolean updateAdjacentCellsEntropy(Cell2D<T> currentCell) throws TileException {
		var x = currentCell.getX();
		var y = currentCell.getY();
		
		var valid = true;
		
		var newEntropyLeft = new HashSet<Tile<T>>();
		if(valid && this.canUpdateLeftCell(x, y)) {
			newEntropyLeft.addAll(this.getLeftCell(x, y).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side2D.Left)));
			if(newEntropyLeft.isEmpty()) {
				valid = false;
			}
		}
		
		var newEntropyRight = new HashSet<Tile<T>>();
		if(valid && this.canUpdateRightCell(x, y)) {
			newEntropyRight.addAll(this.getRightCell(x, y).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side2D.Right)));
			if(newEntropyRight.isEmpty()) {
				valid = false;
			}
		}
		
		var newEntropyBottom = new HashSet<Tile<T>>();
		if(valid && this.canUpdateBottomCell(x, y)) {
			newEntropyBottom.addAll(this.getBottomCell(x, y).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side2D.Bottom)));
			if(newEntropyBottom.isEmpty()) {
				valid = false;
			}
		}
		
		var newEntropyTop = new HashSet<Tile<T>>();
		if(valid && this.canUpdateTopCell(x, y)) {
			newEntropyTop.addAll(this.getTopCell(x, y).getReducedEntropy(this.tileMap.getAdjacents(currentCell.getTile(), Side2D.Top)));
			if(newEntropyTop.isEmpty()) {
				valid = false;
			}
		}
		
		if(valid) {
			if(this.canUpdateLeftCell(x, y)) {
				this.getLeftCell(x, y).setEntropy(newEntropyLeft);
				this.collapsableCells.add(this.getLeftCell(x, y));
			}
			
			if(this.canUpdateRightCell(x, y)) {
				this.getRightCell(x, y).setEntropy(newEntropyRight);
				this.collapsableCells.add(this.getRightCell(x, y));
			}
			
			if(this.canUpdateBottomCell(x, y)) {
				this.getBottomCell(x, y).setEntropy(newEntropyBottom);
				this.collapsableCells.add(this.getBottomCell(x, y));
			}
			
			if(this.canUpdateTopCell(x, y)) {
				this.getTopCell(x, y).setEntropy(newEntropyTop);
				this.collapsableCells.add(this.getTopCell(x, y));
			}
		}
		
		return valid;
	}
	
	abstract protected boolean canUpdateLeftCell(int x, int y);
	
	abstract protected Cell2D<T> getLeftCell(int x, int y);
	
	abstract protected boolean canUpdateRightCell(int x, int y);
	
	abstract protected Cell2D<T> getRightCell(int x, int y);
	
	abstract protected boolean canUpdateBottomCell(int x, int y);
	
	abstract protected Cell2D<T> getBottomCell(int x, int y);
	
	abstract protected boolean canUpdateTopCell(int x, int y);
	
	abstract protected Cell2D<T> getTopCell(int x, int y);
}