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

import dev.irzinfante.wfc4j.api.EuclideanWFC1D;
import dev.irzinfante.wfc4j.enums.Side1D;
import dev.irzinfante.wfc4j.model.Tile;
import dev.irzinfante.wfc4j.model.TileMap1D;

public class TestEuclideanWFC1D {

	@Test
	public void testStringTile() throws TileException, DimensionException {
		
		/*
		 *		               L ->  A | B'| R			               L' ->  A'| B | R'
		 *		L | B | A'  <- A ->  A'| B | R'			L'| B'| A   <- A' ->  A | B'| R
		 *		L'| B'| A   <- B ->  A | B'| R			L | B | A'  <- B' ->  A'| B | R'
		 *		L | B | A'  <- R ->						L'| B'| A   <- R' ->
		 */
		
		Tile<String> L  = new Tile<>("L") , A  = new Tile<>("A") , B  = new Tile<>("B") , R  = new Tile<>("R");
		Tile<String> Lp = new Tile<>("L'"), Ap = new Tile<>("A'"), Bp = new Tile<>("B'"), Rp = new Tile<>("R'");
		
		var tileSet = new HashSet<Tile<String>>();
		tileSet.add(L);  tileSet.add(A);  tileSet.add(B);  tileSet.add(R);
		tileSet.add(Lp); tileSet.add(Ap); tileSet.add(Bp); tileSet.add(Rp);
		
		var adjacentABpR = new HashSet<Tile<String>>();
		adjacentABpR.add(A); adjacentABpR.add(Bp); adjacentABpR.add(R);
		
		var adjacentApBRp = new HashSet<Tile<String>>();
		adjacentApBRp.add(Ap); adjacentApBRp.add(B); adjacentApBRp.add(Rp);
		
		var adjacentLBAp = new HashSet<Tile<String>>();
		adjacentLBAp.add(L); adjacentLBAp.add(B); adjacentLBAp.add(Ap);
		
		var adjacentLpBpA = new HashSet<Tile<String>>();
		adjacentLpBpA.add(Lp); adjacentLpBpA.add(Bp); adjacentLpBpA.add(A);
		
		var tileMap = new TileMap1D<>(tileSet);
		
		tileMap.setAdjacents(L, Side1D.Left, new HashSet<>());	tileMap.setAdjacents(L, Side1D.Right, adjacentABpR);
		tileMap.setAdjacents(A, Side1D.Left, adjacentLBAp);		tileMap.setAdjacents(A, Side1D.Right, adjacentApBRp);
		tileMap.setAdjacents(B, Side1D.Left, adjacentLpBpA);	tileMap.setAdjacents(B, Side1D.Right, adjacentABpR);
		tileMap.setAdjacents(R, Side1D.Left, adjacentLBAp);		tileMap.setAdjacents(R, Side1D.Right, new HashSet<>());
		
		tileMap.setAdjacents(Lp, Side1D.Left, new HashSet<>());	tileMap.setAdjacents(Lp, Side1D.Right, adjacentApBRp);
		tileMap.setAdjacents(Ap, Side1D.Left, adjacentLpBpA);	tileMap.setAdjacents(Ap, Side1D.Right, adjacentABpR);
		tileMap.setAdjacents(Bp, Side1D.Left, adjacentLBAp);	tileMap.setAdjacents(Bp, Side1D.Right, adjacentApBRp);
		tileMap.setAdjacents(Rp, Side1D.Left, adjacentLpBpA);	tileMap.setAdjacents(Rp, Side1D.Right, new HashSet<>());
		
		var gridSize = 7;

		for(var i = 0; i < 20; i++) {
			var WFC_1 = new EuclideanWFC1D<String>(tileMap, gridSize);
			assertTrue(WFC_1.run());
		}

		var entropy_start = new HashMap<Integer, Set<Tile<String>>>();
		entropy_start.put(0, new HashSet<>(Arrays.asList(L, Lp)));

		for(var i = 0; i < 20; i++) {
			var WFC_2 = new EuclideanWFC1D<String>(tileMap, gridSize, entropy_start);
			assertTrue(WFC_2.run());
		}

		var entropy_both = new HashMap<Integer, Set<Tile<String>>>();
		entropy_both.put(0, new HashSet<>(Arrays.asList(L, Lp)));
		entropy_both.put(gridSize - 1, new HashSet<>(Arrays.asList(R, Rp)));

		for(var i = 0; i < 20; i++) {
			var WFC_3 = new EuclideanWFC1D<String>(tileMap, gridSize, entropy_both);
			assertTrue(WFC_3.run());
		}
	}
}