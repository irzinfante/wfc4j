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

package dev.irzinfante.wfc4j.model;

import java.util.Stack;

/**
 * @author	irzinfante iker@irzinfante.dev
 * @version 1.0.0
 * @since	1.0.0
 */
final public class Cell implements Comparable<Cell> {

	private int tile;
    private Stack<Long> entropy;

    public Cell(long entropy) {
		this.tile = -1;
		this.entropy = new Stack<>();
        this.entropy.push(entropy);
    }

	public int getTile() {
		return this.tile;
	}
	
	public void setTile(int tile) {
		this.tile = tile;
	}

    public long getEntropy() {
		return this.entropy.peek();
	}
	
	public void pushEntropy(long entropy) {
		this.entropy.push(entropy);
	}

	public long popEntropy() {
		return this.entropy.pop();
	}

    @Override
	public int compareTo(Cell other) {
		return Long.bitCount(this.entropy.peek()) - Long.bitCount(other.entropy.peek());
	}
}
