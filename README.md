# wfc4j - The Wave Function Collapse library for Java

The ```wfc4j``` library is a Java library that provides an implementation of the wave function collapse algorithm for procedural generation of content. This library can be used to generate a wide variety of content, such as tile maps, textures, and patterns.

## Installation

To use the ```wfc4j``` library in your Java project, you can include the library as a dependency in your build configuration. Here's an example of how to do it with Maven:

```xml
<dependency>
    <groupId>dev.irzinfante</groupId>
    <artifactId>wfc4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

Alternatively, you can download the library JAR file manually and add it to your project's classpath.

## Examples

Here are some examples that demonstrate the usage of the different API of the library:

- 1-dimensional:
    - [Euclidean (linear grid)](examples/1-dimensional-euclidean.md)
    - [Toroidal (cyclic grid)](examples/1-dimensional-toroidal.md)
- 2-dimensional:
    - [Euclidean (planar grid)](examples/2-dimensional-euclidean.md)
    - [Toroidal (toroidal grid)](examples/2-dimensional-toroidal.md)

## License

Copyright (C) 2023-2024 Iker Ruiz de Infante Gonzalez iker@irzinfante.dev

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>.

[LICENSE](LICENSE) contains a copy of the full AGPLv3 licensing conditions.

## Acknowledgments

The ```wfc4j``` library is inspired on Dan Shiffman's [Coding Challenge 171: Wave Function Collapse](https://youtu.be/rI_y2GAlQFM) video, which is based on the original [Wave Function Collapse Algorithm](https://github.com/mxgmn/WaveFunctionCollapse) developed by Maxim Gumin.
