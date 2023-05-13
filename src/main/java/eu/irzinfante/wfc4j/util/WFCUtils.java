package eu.irzinfante.wfc4j.util;

import eu.irzinfante.wfc4j.model.Cell1D;

/**
 * @author	irzinfante iker@irzinfante.eu
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
			var cellTile = grid[i].getValue();
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
}