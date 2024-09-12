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
import dev.irzinfante.wfc4j.enums.Side1D;
import dev.irzinfante.wfc4j.model.TileMap1D;
import dev.irzinfante.wfc4j.model.Tile;
import dev.irzinfante.wfc4j.model.Cell;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version	1.0.0
 * @since	1.0.0
 */
abstract public class AbstractWFC1D<T> {

	private TileMap1D<T> tileMap;
	protected Cell[] grid;
	private List<Integer> collapsableCells;
	
	protected AbstractWFC1D(TileMap1D<T> tileMap, int gridSizeX) throws TileException, DimensionException {
		this(tileMap, gridSizeX, new HashMap<>());
	}

	protected AbstractWFC1D(
		TileMap1D<T> tileMap,
		int gridSizeX,
		Map<Integer, Set<Tile<T>>> initialEntropy
	) throws TileException, DimensionException {
		
		if(tileMap == null) {
			throw new TileException("TileMap cannot be null");
		} else if(gridSizeX < 1) {
			throw new DimensionException("Invalid grid size");
		}
		
		this.tileMap = tileMap;
		this.grid = new Cell[gridSizeX];
		this.collapsableCells = new ArrayList<>();
		
		var tilesNumber = this.tileMap.getTileSet().size();
		for(int index = 0; index < gridSizeX; index++) {
			this.grid[index] = new Cell(tilesNumber == Long.SIZE ? -1L : (1L << tilesNumber) - 1);
			this.collapsableCells.add(index);
		}

		for(var ieEntry : initialEntropy.entrySet()) {
			var index = ieEntry.getKey();
			if(index < 0 || index >= gridSizeX) {
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
			this.grid[index].popEntropy();
			this.grid[index].pushEntropy(entropy);
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

	public List<Tile<T>> getGrid() {
		var grid = new ArrayList<Tile<T>>();
		for(var cell : this.grid) {
			grid.add(this.tileMap.getTileSet().get(cell.getTile()));
		}
		return grid;
	}

	private void shuffleSortCollapsableCells() {
		Collections.shuffle(this.collapsableCells);
		this.collapsableCells.sort((Integer self, Integer other) -> this.grid[self].compareTo(this.grid[other]));
	}

	private List<Integer> randomEntropyTilesFromCell(int cellIndex) {
		var entropy = this.grid[cellIndex].getEntropy();
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

	private boolean collapseAndPropagate(int cellIndex, int tileIndex) throws TileException {
		var tile = this.tileMap.getTileSet().get(tileIndex);

		var newEntropies = new long[Side1D.values().length];
		var sideCellIndexes = new int[Side1D.values().length];

		for(var side : Side1D.values()) {
			newEntropies[side.getValue()] = -1L;
			var sideCellIndex = this.getSideCellIndex(cellIndex, side);
			sideCellIndexes[side.getValue()] = sideCellIndex;
			if(sideCellIndex != -1) {
				var sideAdjacents = this.tileMap.getAdjacents(tile, side);
				var sideCellEntropy = this.grid[sideCellIndex].getEntropy();
				newEntropies[side.getValue()] = sideCellEntropy & sideAdjacents;
			}
		}

		if(
			newEntropies[Side1D.Left.getValue()] != 0 &&
			newEntropies[Side1D.Right.getValue()] != 0
		) {
			for(var side : Side1D.values()) {
				var sideCellIndex = sideCellIndexes[side.getValue()];
				if(sideCellIndex != -1) {
					this.grid[sideCellIndex].pushEntropy(newEntropies[side.getValue()]);
				}
			}

			this.grid[cellIndex].setTile(tileIndex);

			return true;
		}

		return false;
	}

	private void revert(int cellIndex) {
		for(var side : Side1D.values()) {
			var sideCellIndex = this.getSideCellIndex(cellIndex, side);
			if(sideCellIndex != -1) {
				this.grid[sideCellIndex].popEntropy();
			}
		}

		this.grid[cellIndex].setTile(-1);
	}

	abstract protected Integer getSideCellIndex(int cellIndex, Side1D side);
}