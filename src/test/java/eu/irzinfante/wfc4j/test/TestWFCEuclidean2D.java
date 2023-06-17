package eu.irzinfante.wfc4j.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import eu.irzinfante.wfc4j.api.WaveFunctionCollapseEuclidean2D;
import eu.irzinfante.wfc4j.enums.Side2D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell2D;
import eu.irzinfante.wfc4j.model.Tile;
import eu.irzinfante.wfc4j.model.TileMap2D;
import eu.irzinfante.wfc4j.util.WFCUtils;

public class TestWFCEuclidean2D {

	@Test
	public void testStringTile() throws TileException, DimensionException {
		
		/*	
		 *		Tile	| Left		| Right		| Bottom	| Top		|
		 *		NE		| NW, SW	| NW, SW	| SE, SW	| SE, SW	|
		 *		NW		| NE, SE	| NE, SE	| SE, SW	| SE, SW	|
		 *		SE		| NW, SW	| NW, SW	| NE, NW	| NE, NW	|
		 *		SW		| NE, SE	| NE, SE	| NE, NW	| NE, NW	|
		 */
		
		Tile<String> NE = new Tile<>("NE"), NW = new Tile<>("NW"), SE = new Tile<>("SE"), SW = new Tile<>("SW");
		
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
		
		int gridSizeX = 8, gridSizeY = 6;
		var WFC = new WaveFunctionCollapseEuclidean2D<String>(tileMap, gridSizeX, gridSizeY);
		
		WFC.clear();
		var result = WFC.generate();
		
		System.out.println(WFCUtils.WFC2DToString(result));
		
		WFC.clear();
		WFC.setCellConstraint(new Cell2D<>(new HashSet<>(Arrays.asList(SE)), 4, 3));
		WFC.setCellConstraint(new Cell2D<>(new HashSet<>(Arrays.asList(NW)), 5, 4));
		result = WFC.generate();
		
		System.out.println(WFCUtils.WFC2DToString(result));
		assertEquals("Unexpected tile", result[2][4].getTile(), SW);
		assertEquals("Unexpected tile", result[3][3].getTile(), NE);
	}
}