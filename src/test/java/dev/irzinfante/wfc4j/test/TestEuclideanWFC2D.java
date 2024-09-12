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
 
 import dev.irzinfante.wfc4j.api.EuclideanWFC2D;
 import dev.irzinfante.wfc4j.enums.Side2D;
 import dev.irzinfante.wfc4j.model.Tile;
 import dev.irzinfante.wfc4j.model.TileMap2D;

public class TestEuclideanWFC2D {

	@Test
	public void testStringTile() throws TileException, DimensionException {
		
		/*	
		 *		Tile	| Left	| Right	| Bottom| Top	|
		 *		└		| ┘, ┐	| ┘, ┐	| ┌, ┐	| ┌, ┐	|
		 *		┘		| └, ┌	| └, ┌	| ┌, ┐	| ┌, ┐	|
		 *		┌		| ┘, ┐	| ┘, ┐	| └, ┘	| └, ┘	|
		 *		┐		| └, ┌	| └, ┌	| └, ┘	| └, ┘	|
		 */
		
		Tile<String> NE = new Tile<>("└"), NW = new Tile<>("┘"), SE = new Tile<>("┌"), SW = new Tile<>("┐");
		
		var tileSet = new HashSet<Tile<String>>();
		tileSet.add(NE); tileSet.add(NW); tileSet.add(SE); tileSet.add(SW);
		
		var adjacentNWSW = new HashSet<Tile<String>>();
		adjacentNWSW.add(NW); adjacentNWSW.add(SW);
		
		var adjacentSESW = new HashSet<Tile<String>>();
		adjacentSESW.add(SE); adjacentSESW.add(SW);
		
		var adjacentNESE = new HashSet<Tile<String>>();
		adjacentNESE.add(NE); adjacentNESE.add(SE);
		
		var adjacentNENW = new HashSet<Tile<String>>();
		adjacentNENW.add(NE); adjacentNENW.add(NW);
		
		var tileMap = new TileMap2D<>(tileSet);
		
		tileMap.setAdjacents(NE, Side2D.Left, adjacentNWSW);	tileMap.setAdjacents(NE, Side2D.Right, adjacentNWSW);
		tileMap.setAdjacents(NE, Side2D.Bottom, adjacentSESW);	tileMap.setAdjacents(NE, Side2D.Top, adjacentSESW);
		
		tileMap.setAdjacents(NW, Side2D.Left, adjacentNESE);	tileMap.setAdjacents(NW, Side2D.Right, adjacentNESE);
		tileMap.setAdjacents(NW, Side2D.Bottom, adjacentSESW);	tileMap.setAdjacents(NW, Side2D.Top, adjacentSESW);
		
		tileMap.setAdjacents(SE, Side2D.Left, adjacentNWSW);	tileMap.setAdjacents(SE, Side2D.Right, adjacentNWSW);
		tileMap.setAdjacents(SE, Side2D.Bottom, adjacentNENW);	tileMap.setAdjacents(SE, Side2D.Top, adjacentNENW);
		
		tileMap.setAdjacents(SW, Side2D.Left, adjacentNESE);	tileMap.setAdjacents(SW, Side2D.Right, adjacentNESE);
		tileMap.setAdjacents(SW, Side2D.Bottom, adjacentNENW);	tileMap.setAdjacents(SW, Side2D.Top, adjacentNENW);
		
		int gridSizeX = 6, gridSizeY = 8;

		for(var i = 0; i < 20; i++) {
			var WFC_1 = new EuclideanWFC2D<String>(tileMap, gridSizeX, gridSizeY);
			assertTrue(WFC_1.run());
		}

		var entropy = new HashMap<Integer[], Set<Tile<String>>>();
		entropy.put(new Integer[]{1, 5}, new HashSet<>(Arrays.asList(NW)));
		entropy.put(new Integer[]{2, 6}, new HashSet<>(Arrays.asList(SE)));

		for(var i = 0; i < 20; i++) {
			var WFC_2 = new EuclideanWFC2D<String>(tileMap, gridSizeX, gridSizeY, entropy);
			assertTrue(WFC_2.run());
		}
	}
}