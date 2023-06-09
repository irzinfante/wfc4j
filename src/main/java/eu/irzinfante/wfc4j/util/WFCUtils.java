package eu.irzinfante.wfc4j.util;

import eu.irzinfante.wfc4j.model.Cell1D;
import eu.irzinfante.wfc4j.model.Cell2D;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @version 0.2.0
 * @since	0.1.0
 */
public final class WFCUtils {
	
	/**
	 * Gets the String representation of a 1-dimensional grid
	 *
	 * @param	grid 1-dimensional grid to be represented as a String
	 * @return	String representation of grid
	 * @since	0.1.0
	 */
	public static String WFC1DToString(Cell1D<String>[] grid) {
		var topBorder = new StringBuilder("┌");
		var cellsRepr = new StringBuilder("│");
		var bottomBorder = new StringBuilder("└");
		for(int i = 0; i < grid.length; i++) {
			var cellTile = grid[i].getTile();
			var value = cellTile == null ? new String(" ") : cellTile.getValue().toString();
			
			topBorder.append("─".repeat(value.length()));
			cellsRepr.append(value);
			bottomBorder.append("─".repeat(value.length()));
			
			if(i < grid.length - 1) {
				topBorder.append("┬");
				cellsRepr.append("│");
				bottomBorder.append("┴");
			}
		}
		
		topBorder.append("┐");
		cellsRepr.append("│");
		bottomBorder.append("┘");
		
		return topBorder.toString() + "\n" + cellsRepr + "\n" + bottomBorder.toString();
	}
	
	/**
	 * Gets the String representation of a 2-dimensional grid
	 *
	 * @param	grid 2-dimensional grid to be represented as a String
	 * @return	String representation of grid
	 * @since	0.2.0
	 */
	public static String WFC2DToString(Cell2D<String>[][] grid) {
		var gridBuilder = new StringBuilder();

		var bottomBorderBuilder = new StringBuilder("└");
		for(int y = 0; y < grid.length; y++) {
			
			var topBorderBuilder = new StringBuilder(y == 0 ? "┌" : "├");
			var rowBuilder = new StringBuilder("│");
			
			for(int x = 0; x < grid[y].length; x++) {
				var cellTile = grid[y][x].getTile();
				var value = cellTile == null ? new String(" ") : cellTile.getValue().toString();
				
				topBorderBuilder.append("─".repeat(value.length()));
				rowBuilder.append(value).append("│");
				
				if (y == 0) {
					topBorderBuilder.append(x == grid[y].length - 1 ? "┐" : "┬");
				} else {
					topBorderBuilder.append(x == grid[y].length - 1 ? "┤" : "┼");
					if (y == grid.length - 1) {
						bottomBorderBuilder.append("─".repeat(value.length())).append(x == grid[y].length - 1 ? "┘" : "┴");
					}
				}
			}
			
			gridBuilder.append(topBorderBuilder).append("\n");
			gridBuilder.append(rowBuilder).append("\n");
		}
		gridBuilder.append(bottomBorderBuilder);
		
		return gridBuilder.toString();
	}
}