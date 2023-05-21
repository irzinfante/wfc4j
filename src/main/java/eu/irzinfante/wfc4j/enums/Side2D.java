/**
 * Library to use the Wave Function Collapse strategy for procedural generation
 * Copyright (C) 2023 Iker Ruiz de Infante Gonzalez <iker@irzinfante.eu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.irzinfante.wfc4j.enums;

/**
 * @author	irzinfante iker@irzinfante.eu
 * @since	0.2.0
 */
public enum Side2D {
	Left(0),
	Right(1),
	Bottom(2),
	Top(3);

	private final int value;
	
	private Side2D(int value) {
		this.value = value;
	}
	
	public int getValue() {
        return value;
    }
}