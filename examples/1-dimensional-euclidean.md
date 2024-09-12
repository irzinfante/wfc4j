# 1-dimensional euclidean grid example

Let's see how to use the wfc4j library to generate a linear pattern. These are the tiles we have available:

| L   | A   | B   | R   |L'   | A'  | B'  | R'  |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|<img src="../assets/1-dimensional-euclidean/L.png">|<img src="../assets/1-dimensional-euclidean/A.png">|<img src="../assets/1-dimensional-euclidean/B.png">|<img src="../assets/1-dimensional-euclidean/R.png">|<img src="../assets/1-dimensional-euclidean/L'.png">|<img src="../assets/1-dimensional-euclidean/A'.png">|<img src="../assets/1-dimensional-euclidean/B'.png">|<img src="../assets/1-dimensional-euclidean/R'.png">|

```java
import java.util.HashSet;
import dev.irzinfante.wfc4j.model.Tile;

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
import dev.irzinfante.wfc4j.model.TileMap1D;
import dev.irzinfante.wfc4j.enums.Side1D;

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

Now we can instantiate the API class with a 7 elements long grid:

```java
import dev.irzinfante.wfc4j.api.EuclideanWFC1D;

var gridSize = 7;
```

We will run 3 examples:

- For the first one we won't impose any restriction:

  ```java
  var WFC_1 = new EuclideanWFC1D<String>(tileMap, gridSize);
  WFC_1.run();
  ```

  <img src="../assets/1-dimensional-euclidean/result1.png">

- In the second one we will impose some constraint on the left end:

  ```java
  import java.util.Set;
  import java.util.Arrays;

  var entropy_start = new HashMap<Integer, Set<Tile<String>>>();
  entropy_start.put(0, new HashSet<>(Arrays.asList(L, Lp)));

  var WFC_2 = new EuclideanWFC1D<String>(tileMap, gridSize, entropy_start);
  WFC_2.run();
  ```

  <img src="../assets/1-dimensional-euclidean/result2.png">

- In the third one we will impose constraints in both ends:

  ```java
  var entropy_both = new HashMap<Integer, Set<Tile<String>>>();
  entropy_both.put(0, new HashSet<>(Arrays.asList(L, Lp)));
  entropy_both.put(gridSize - 1, new HashSet<>(Arrays.asList(R, Rp)));

  var WFC_3 = new EuclideanWFC1D<String>(tileMap, gridSize, entropy_both);
  WFC_3.run();
  ```

  <img src="../assets/1-dimensional-euclidean/result3.png">
