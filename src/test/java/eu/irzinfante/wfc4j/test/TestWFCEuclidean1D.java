package eu.irzinfante.wfc4j.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import eu.irzinfante.wfc4j.api.WaveFunctionCollapseEuclidean1D;
import eu.irzinfante.wfc4j.enums.Side1D;
import eu.irzinfante.wfc4j.exceptions.DimensionException;
import eu.irzinfante.wfc4j.exceptions.TileException;
import eu.irzinfante.wfc4j.model.Cell1D;
import eu.irzinfante.wfc4j.model.Tile;
import eu.irzinfante.wfc4j.model.TileMap1D;
import eu.irzinfante.wfc4j.util.WFCUtils;

public class TestWFCEuclidean1D {

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
		
		int gridSize = 13;
		var WFC = new WaveFunctionCollapseEuclidean1D<String>(tileMap, gridSize, -109923011117821092L);
		
		WFC.clear();
		var result = WFC.generate();
		
		var firstResult =     "┌──┬──┬─┬─┬─┬─┬─┬──┬─┬──┬──┬──┬─┐";
		firstResult += "\n" + "│B'│A'│A│B│A│B│A│A'│A│A'│B'│A'│R│";
		firstResult += "\n" + "└──┴──┴─┴─┴─┴─┴─┴──┴─┴──┴──┴──┴─┘";
		
		assertEquals("Unexpected first result", firstResult, WFCUtils.WFC1DToString(result));
		
		WFC.clear();
		WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(L, Lp)), 1));
		result = WFC.generate();
		
		var secondResult =     "┌─┬─┬─┬──┬─┬──┬──┬─┬──┬─┬──┬──┬──┐";
		secondResult += "\n" + "│L│A│B│B'│B│B'│A'│A│A'│A│A'│B'│A'│";
		secondResult += "\n" + "└─┴─┴─┴──┴─┴──┴──┴─┴──┴─┴──┴──┴──┘";
		
		assertEquals("Unexpected second result", secondResult, WFCUtils.WFC1DToString(result));
		
		WFC.clear();
		WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(L, Lp)), 1));
		WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(R, Rp)), gridSize));
		result = WFC.generate();
		
		var thirdResult =     "┌─┬──┬──┬─┬──┬──┬─┬─┬──┬─┬──┬─┬──┐";
		thirdResult += "\n" + "│L│B'│A'│A│A'│B'│B│A│A'│A│A'│A│R'│";
		thirdResult += "\n" + "└─┴──┴──┴─┴──┴──┴─┴─┴──┴─┴──┴─┴──┘";
		
		assertEquals("Unexpected third result", thirdResult, WFCUtils.WFC1DToString(result));
	}
}