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

package dev.irzinfante.wfc4j.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

import dev.irzinfante.wfc4j.exceptions.TileException;
import dev.irzinfante.wfc4j.exceptions.DimensionException;
import dev.irzinfante.wfc4j.enums.Side2D;
import dev.irzinfante.wfc4j.model.TileMap2D;
import dev.irzinfante.wfc4j.model.Tile;
import dev.irzinfante.wfc4j.model.Cell;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version	1.0.0
 * @since	1.0.0
 */
abstract public class AbstractWFC2D<T> {

	private TileMap2D<T> tileMap;
	protected Cell[][] grid;
	private List<Integer[]> collapsableCells;
	
	protected AbstractWFC2D(TileMap2D<T> tileMap, int gridSizeX, int gridSizeY) throws TileException, DimensionException {
		this(tileMap, gridSizeX, gridSizeY, new HashMap<>());
	}

	protected AbstractWFC2D(
		TileMap2D<T> tileMap,
		int gridSizeX,
		int gridSizeY,
		Map<Integer[], Set<Tile<T>>> initialEntropy
	) throws TileException, DimensionException {
		
		if(tileMap == null) {
			throw new TileException("TileMap cannot be null");
		} else if(gridSizeX < 1 || gridSizeY < 1) {
			throw new DimensionException("Invalid grid size");
		}
		
		this.tileMap = tileMap;
		this.grid = new Cell[gridSizeX][gridSizeY];
		this.collapsableCells = new ArrayList<>();
		
		var tilesNumber = this.tileMap.getTileSet().size();
		for(int indX = 0; indX < gridSizeX; indX++) {
			for(int indY = 0; indY < gridSizeY; indY++) {
				this.grid[indX][indY] = new Cell(tilesNumber == Long.SIZE ? -1L : (1L << tilesNumber) - 1);
				this.collapsableCells.add(new Integer[] {indX, indY});
			}
		}

		for(var ieEntry : initialEntropy.entrySet()) {
			var index = ieEntry.getKey();
			if(
				index[0] < 0 || index[0] >= gridSizeX ||
				index[1] < 0 || index[1] >= gridSizeY
			) {
				throw new DimensionException("Cannot set initial entropy for cell outside the grid");
			} else if (ieEntry.getValue().isEmpty()) {
				throw new TileException("Cannot initialize cell with zero entropy");
			}

			var tileSet = this.tileMap.getTileSet();
			if(!tileSet.containsAll(ieEntry.getValue())) {
				throw new TileException("Tiles for initial entropy must exist in tilemap");
			}

			var entropy = 0L;
			for(var tile : ieEntry.getValue()) {
				entropy += 1L << tileSet.indexOf(tile);
			}
			this.grid[index[0]][index[1]].popEntropy();
			this.grid[index[0]][index[1]].pushEntropy(entropy);
		}
	}
	
	/**
	 * Runs the WFC algorithm to populate the tile values for the cells of the grid
	 * 
	 * @return	Boolean indicating whether the algorithm finished successfully or not
	 * 
	 * @version	1.0.0
	 * @since	1.0.0
	 */
	public boolean run() throws TileException {
		if(this.collapsableCells.size() > 0) {
			this.shuffleSortCollapsableCells();
			var cellIndex = this.collapsableCells.remove(0);
			var randomTileIndex = this.randomEntropyTilesFromCell(cellIndex);
			for(var tileIndex : randomTileIndex) {
				if(this.collapseAndPropagate(cellIndex, tileIndex)) {
					if(run()) {
						return true;
					}
					this.revert(cellIndex);
				}
			}
			this.collapsableCells.add(cellIndex);
			return false;
		} else {
			return true;
		}
	}

	public List<List<Tile<T>>> getGrid() {
		var grid = new ArrayList<List<Tile<T>>>();
		for(var row : this.grid) {
			var gridRow = new ArrayList<Tile<T>>();
			for(var cell : row) {
				gridRow.add(this.tileMap.getTileSet().get(cell.getTile()));
			}
			grid.add(gridRow);
		}
		return grid;
	}

	private void shuffleSortCollapsableCells() {
		Collections.shuffle(this.collapsableCells);
		this.collapsableCells.sort((Integer[] self, Integer[] other) -> this.grid[self[0]][self[1]].compareTo(this.grid[other[0]][other[1]]));
	}

	private List<Integer> randomEntropyTilesFromCell(Integer[] cellIndex) {
		var entropy = this.grid[cellIndex[0]][cellIndex[1]].getEntropy();
		var entropyBitStrings = new StringBuilder(Long.toBinaryString(entropy)).reverse().toString().split("");
		var tiles = new ArrayList<Integer>();
		for(var i = 0; i < entropyBitStrings.length; i++) {
			if(entropyBitStrings[i].equals("1")) {
				tiles.add(i);
			}
		}
		Collections.shuffle(tiles);
		return tiles;
	}

	private boolean collapseAndPropagate(Integer[] cellIndex, int tileIndex) throws TileException {
		var tile = this.tileMap.getTileSet().get(tileIndex);

		var newEntropies = new long[Side2D.values().length];
		var sideCellIndexes = new Integer[Side2D.values().length][];

		for(var side : Side2D.values()) {
			newEntropies[side.getValue()] = -1L;
			var sideCellIndex = this.getSideCellIndex(cellIndex, side);
			sideCellIndexes[side.getValue()] = sideCellIndex;
			if(sideCellIndex[0] != -1 && sideCellIndex[1] != -1) {
				var sideAdjacents = this.tileMap.getAdjacents(tile, side);
				var sideCellEntropy = this.grid[sideCellIndex[0]][sideCellIndex[1]].getEntropy();
				newEntropies[side.getValue()] = sideCellEntropy & sideAdjacents;
			}
		}

		if(
			newEntropies[Side2D.Left.getValue()] != 0 &&
			newEntropies[Side2D.Right.getValue()] != 0 &&
			newEntropies[Side2D.Bottom.getValue()] != 0 &&
			newEntropies[Side2D.Top.getValue()] != 0
		) {
			for(var side : Side2D.values()) {
				var sideCellIndex = sideCellIndexes[side.getValue()];
				if(sideCellIndex[0] != -1 && sideCellIndex[1] != -1) {
					this.grid[sideCellIndex[0]][sideCellIndex[1]].pushEntropy(newEntropies[side.getValue()]);
				}
			}

			this.grid[cellIndex[0]][cellIndex[1]].setTile(tileIndex);

			return true;
		}

		return false;
	}

	private void revert(Integer[] cellIndex) {
		for(var side : Side2D.values()) {
			var sideCellIndex = this.getSideCellIndex(cellIndex, side);
			if(sideCellIndex[0] != -1 && sideCellIndex[1] != -1) {
				this.grid[sideCellIndex[0]][sideCellIndex[1]].popEntropy();
			}
		}

		this.grid[cellIndex[0]][cellIndex[1]].setTile(-1);
	}

	abstract protected Integer[] getSideCellIndex(Integer[] cellIndex, Side2D side);
}