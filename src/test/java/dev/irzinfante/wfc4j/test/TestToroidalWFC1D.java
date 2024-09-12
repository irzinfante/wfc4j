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

package dev.irzinfante.wfc4j.test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;

import dev.irzinfante.wfc4j.exceptions.DimensionException;
import dev.irzinfante.wfc4j.exceptions.TileException;
import dev.irzinfante.wfc4j.api.ToroidalWFC1D;
import dev.irzinfante.wfc4j.enums.Side1D;
import dev.irzinfante.wfc4j.model.Tile;
import dev.irzinfante.wfc4j.model.TileMap1D;

public class TestToroidalWFC1D {

	@Test
	public void testStringTile() throws TileException, DimensionException {
		
		/*
		 *		AA | BA  <- AA ->  AA | AB		AA | BA  <- AB ->  BA | BB
		 *		AB | BB  <- BA ->  AA | AB		AB | BB  <- BB ->  BA | BB
		 */
		
		Tile<String> AA = new Tile<>("AA"), AB = new Tile<>("AB");
		Tile<String> BA = new Tile<>("BA"), BB = new Tile<>("BB");
		
		var tileSet = new HashSet<Tile<String>>();
		tileSet.add(AA); tileSet.add(AB);
		tileSet.add(BA); tileSet.add(BB);
		
		var adjacentAABA = new HashSet<Tile<String>>();
		adjacentAABA.add(AA); adjacentAABA.add(BA);
		
		var adjacentAAAB = new HashSet<Tile<String>>();
		adjacentAAAB.add(AA); adjacentAAAB.add(AB);
		
		var adjacentBABB = new HashSet<Tile<String>>();
		adjacentBABB.add(BA); adjacentBABB.add(BB);
		
		var adjacentABBB = new HashSet<Tile<String>>();
		adjacentABBB.add(AB); adjacentABBB.add(BB);
		
		var tileMap = new TileMap1D<>(tileSet);
		
		tileMap.setAdjacents(AA, Side1D.Left, adjacentAABA); tileMap.setAdjacents(AA, Side1D.Right, adjacentAAAB);
		tileMap.setAdjacents(AB, Side1D.Left, adjacentAABA); tileMap.setAdjacents(AB, Side1D.Right, adjacentBABB);
		
		tileMap.setAdjacents(BA, Side1D.Left, adjacentABBB); tileMap.setAdjacents(BA, Side1D.Right, adjacentAAAB);
		tileMap.setAdjacents(BB, Side1D.Left, adjacentABBB); tileMap.setAdjacents(BB, Side1D.Right, adjacentBABB);
		
		var gridSize = 5;

		for(var i = 0; i < 20; i++) {
			var WFC_1 = new ToroidalWFC1D<String>(tileMap, gridSize);
			assertTrue(WFC_1.run());
		}

		var entropy = new HashMap<Integer, Set<Tile<String>>>();
		entropy.put(0, new HashSet<>(Arrays.asList(AA)));
		entropy.put(gridSize / 2, new HashSet<>(Arrays.asList(BB)));
		entropy.put(gridSize - 1, new HashSet<>(Arrays.asList(AA)));

		for(var i = 0; i < 20; i++) {
			var WFC_2 = new ToroidalWFC1D<String>(tileMap, gridSize, entropy);
			assertTrue(WFC_2.run());
		}
	}
}