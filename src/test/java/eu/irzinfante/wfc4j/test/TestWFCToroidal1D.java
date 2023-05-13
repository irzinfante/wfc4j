package eu.irzinfante.wfc4j.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import eu.irzinfante.wfc4j.api.WaveFunctionCollapseToroidal1D;
import eu.irzinfante.wfc4j.enums.Side1D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell1D;
import eu.irzinfante.wfc4j.model.Tile;
import eu.irzinfante.wfc4j.model.TileMap1D;
import eu.irzinfante.wfc4j.util.WFCUtils;

public class TestWFCToroidal1D {

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
		
		int gridSize = 7;
		var WFC = new WaveFunctionCollapseToroidal1D<String>(tileMap, gridSize);
		
		WFC.clear();
		var result = WFC.generate();
		
		System.out.println(WFCUtils.WFC1DToString(result));
		
		WFC.clear();
		WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(BB)), 5));
		result = WFC.generate();
		
		System.out.println(WFCUtils.WFC1DToString(result));
		assertEquals("Unexpected fifth cell tile", BB, result[4].getTile());
	}
}