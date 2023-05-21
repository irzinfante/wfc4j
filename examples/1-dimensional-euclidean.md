# 1-dimensional euclidean grid example

Let's see how to use the wfc4j library to generate a linear pattern. These are the tiles we have available:

| L   | A   | B   | R   |L'   | A'  | B'  | R'  |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|<img src="../assets/1-dimensional-euclidean/L.png">|<img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/B.png">|<img src="../assets/1-dimensional-euclidean/R.png">|<img src="../assets/1-dimensional-euclidean/L'.png">|<img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/B'.png">|<img src="../assets/1-dimensional-euclidean/R'.png">|

```java
import java.util.HashSet;
import eu.irzinfante.wfc4j.model.Tile;

Tile<String> L  = new Tile<>("L") , A  = new Tile<>("A") , B  = new Tile<>("B") , R  = new Tile<>("R");
Tile<String> Lp = new Tile<>("L'"), Ap = new Tile<>("A'"), Bp = new Tile<>("B'"), Rp = new Tile<>("R'");

var tileSet = new HashSet<Tile<String>>();
tileSet.add(L);  tileSet.add(A);  tileSet.add(B);  tileSet.add(R);
tileSet.add(Lp); tileSet.add(Ap); tileSet.add(Bp); tileSet.add(Rp);
```

So, the logical adjacencies between tiles are the following:


| Left adjacent tiles   | Tile | Right adjacent tiles   |
| ---------------------:|:----:|:---------------------- |
|                       |<img src="../assets/1-dimensional-euclidean/L.png">|<img src="../assets/1-dimensional-euclidean/A.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R.png">|
|<img src="../assets/1-dimensional-euclidean/L.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/A'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R'.png">|
|<img src="../assets/1-dimensional-euclidean/L'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/B.png">|<img src="../assets/1-dimensional-euclidean/A.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R.png">|
|<img src="../assets/1-dimensional-euclidean/L.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/R.png">|                        |
|                       |<img src="../assets/1-dimensional-euclidean/L'.png">|<img src="../assets/1-dimensional-euclidean/A'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R'.png">|
|<img src="../assets/1-dimensional-euclidean/L'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/A.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R.png">|
|<img src="../assets/1-dimensional-euclidean/L.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/B'.png">|<img src="../assets/1-dimensional-euclidean/A'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/R'.png">|
|<img src="../assets/1-dimensional-euclidean/L'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/B'.png"> $~~~~$ <img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/R'.png">|                        |

```java
import eu.irzinfante.wfc4j.model.TileMap1D;
import eu.irzinfante.wfc4j.enums.Side1D;

var adjacentABpR = new HashSet<Tile<String>>();
adjacentABpR.add(A); adjacentABpR.add(Bp); adjacentABpR.add(R);

var adjacentApBRp = new HashSet<Tile<String>>();
adjacentApBRp.add(Ap); adjacentApBRp.add(B); adjacentApBRp.add(Rp);

var adjacentLBAp = new HashSet<Tile<String>>();
adjacentLBAp.add(L); adjacentLBAp.add(B); adjacentLBAp.add(Ap);

var adjacentLpBpA = new HashSet<Tile<String>>();
adjacentLpBpA.add(Lp); adjacentLpBpA.add(Bp); adjacentLpBpA.add(A);

var tileMap = new TileMap1D<>(tileSet);

tileMap.setAdjacents(L, Side1D.Left, new HashSet<>());  tileMap.setAdjacents(L, Side1D.Right, adjacentABpR);
tileMap.setAdjacents(A, Side1D.Left, adjacentLBAp);     tileMap.setAdjacents(A, Side1D.Right, adjacentApBRp);
tileMap.setAdjacents(B, Side1D.Left, adjacentLpBpA);    tileMap.setAdjacents(B, Side1D.Right, adjacentABpR);
tileMap.setAdjacents(R, Side1D.Left, adjacentLBAp);     tileMap.setAdjacents(R, Side1D.Right, new HashSet<>());

tileMap.setAdjacents(Lp, Side1D.Left, new HashSet<>()); tileMap.setAdjacents(Lp, Side1D.Right, adjacentApBRp);
tileMap.setAdjacents(Ap, Side1D.Left, adjacentLpBpA);   tileMap.setAdjacents(Ap, Side1D.Right, adjacentABpR);
tileMap.setAdjacents(Bp, Side1D.Left, adjacentLBAp);    tileMap.setAdjacents(Bp, Side1D.Right, adjacentApBRp);
tileMap.setAdjacents(Rp, Side1D.Left, adjacentLpBpA);   tileMap.setAdjacents(Rp, Side1D.Right, new HashSet<>());
```

Now we can instantiate the API class with a 13 elements long grid:

```java
import eu.irzinfante.wfc4j.api.WaveFunctionCollapseEuclidean1D;

int gridSize = 13;
var WFC = new WaveFunctionCollapseEuclidean1D<String>(tileMap, gridSize, -109923011117821092L);
```

We will perform 3 executions of the `generate` function in order:

- For the first one we won't impose any restriction:

  ```java
  import eu.irzinfante.wfc4j.util.WFCUtils;

  WFC.clear();
  var result = WFC.generate();

  System.out.println(WFCUtils.WFC1DToString(result));
  ```

  <img src="../assets/1-dimensional-euclidean/result1.png">

- In the second one we will impose some constraint on the left end:

  ```java
  import java.util.Arrays;
  import eu.irzinfante.wfc4j.model.Cell1D;

  WFC.clear();
  WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(L, Lp)), 1));
  result = WFC.generate();

  System.out.println(WFCUtils.WFC1DToString(result));
  ```

  <img src="../assets/1-dimensional-euclidean/result2.png">

- In the third one we will impose constraints in both ends:

  ```java
  WFC.clear();
  WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(L, Lp)), 1));
  WFC.setCellConstraint(new Cell1D<>(new HashSet<>(Arrays.asList(R, Rp)), gridSize));
  result = WFC.generate();

  System.out.println(WFCUtils.WFC1DToString(result));
  ```

  <img src="../assets/1-dimensional-euclidean/result3.png">
