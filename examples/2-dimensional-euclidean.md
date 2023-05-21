# 2-dimensional euclidean grid example

Let's see how to use the wfc4j library to generate a planar pattern. These are the tiles we have available:

| NE  | NW  | SE  | SW  |
|:---:|:---:|:---:|:---:|
|<img src="../assets/2-dimensional-euclidean/NE.png">|<img src="../assets/2-dimensional-euclidean/NW.png">|<img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/SW.png">|

```java
import java.util.HashSet;
import eu.irzinfante.wfc4j.model.Tile;

Tile<String> NE = new Tile<>("NE"), NW = new Tile<>("NW"), SE = new Tile<>("SE"), SW = new Tile<>("SW");

var tileSet = new HashSet<Tile<String>>();
tileSet.add(NE); tileSet.add(NW); tileSet.add(SE); tileSet.add(SW);
```

So, the logical adjacencies between tiles are the following:


| Tile | Left adjacent tiles   | Right adjacent tiles   | Bottom adjacent tiles  | Top adjacent tiles     |
|:----:|:---------------------:|:----------------------:|:----------------------:|:----------------------:|
|<img src="../assets/2-dimensional-euclidean/NE.png">|<img src="../assets/2-dimensional-euclidean/NW.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/NW.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/SE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/SE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|
|<img src="../assets/2-dimensional-euclidean/NW.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/SE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/SE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|
|<img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/NW.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/NW.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/NW.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/NW.png">|
|<img src="../assets/2-dimensional-euclidean/SW.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/SE.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/NW.png">|<img src="../assets/2-dimensional-euclidean/NE.png"> $~~~~$ <img src="../assets/2-dimensional-euclidean/NW.png">|

```java
import eu.irzinfante.wfc4j.model.TileMap2D;
import eu.irzinfante.wfc4j.enums.Side2D;

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
```

Now we can instantiate the API class with an 8 by 6 grid:

```java
import eu.irzinfante.wfc4j.api.WaveFunctionCollapseEuclidean2D;

int gridSizeX = 8, gridSizeY = 6;
var WFC = new WaveFunctionCollapseEuclidean2D<String>(tileMap, gridSizeX, gridSizeY, 148576907989080L);
```

We will perform 2 executions of the `generate` function in order:

- For the first one we won't impose any restriction:

  ```java
  import eu.irzinfante.wfc4j.util.WFCUtils;

  WFC.clear();
  var result = WFC.generate();

  System.out.println(WFCUtils.WFC2DToString(result));
  ```

  <img src="../assets/2-dimensional-euclidean/result1.png">

- For the second one we will impose some constraint on the center of the grid:

  ```java
  import java.util.Arrays;
  import eu.irzinfante.wfc4j.model.Cell2D;

  WFC.clear();
  WFC.setCellConstraint(new Cell2D<>(new HashSet<>(Arrays.asList(SE)), 4, 3));
  WFC.setCellConstraint(new Cell2D<>(new HashSet<>(Arrays.asList(NW)), 5, 4));
  result = WFC.generate();

  System.out.println(WFCUtils.WFC2DToString(result));
  ```

  <img src="../assets/2-dimensional-euclidean/result2.png">
