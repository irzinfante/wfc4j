# 1-dimensional toroidal grid example

Let's see how to use the wfc4j library to generate a cyclic (first cell is adjacent to last cell) pattern. These are the tiles we have available:

| AA  | AB  | BA  | BB  |
|:---:|:---:|:---:|:---:|
|<img src="../assets/1-dimensional-toroidal/AA.png">|<img src="../assets/1-dimensional-toroidal/AB.png">|<img src="../assets/1-dimensional-toroidal/BA.png">|<img src="../assets/1-dimensional-toroidal/BB.png">|

```java
import java.util.HashSet;
import dev.irzinfante.wfc4j.model.Tile;

Tile<String> AA = new Tile<>("AA"), AB = new Tile<>("AB");
Tile<String> BA = new Tile<>("BA"), BB = new Tile<>("BB")

var tileSet = new HashSet<Tile<String>>();
tileSet.add(AA); tileSet.add(AB);
tileSet.add(BA); tileSet.add(BB);
```

So, the logical adjacencies between tiles are the following:


| Left adjacent tiles   | Tile | Right adjacent tiles   |
| ---------------------:|:----:|:---------------------- |
|<img src="../assets/1-dimensional-toroidal/AA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BA.png">|<img src="../assets/1-dimensional-toroidal/AA.png">|<img src="../assets/1-dimensional-toroidal/AA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/AB.png">|
|<img src="../assets/1-dimensional-toroidal/AA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BA.png">|<img src="../assets/1-dimensional-toroidal/AB.png">|<img src="../assets/1-dimensional-toroidal/BA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BB.png">|
|<img src="../assets/1-dimensional-toroidal/AB.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BB.png">|<img src="../assets/1-dimensional-toroidal/BA.png">|<img src="../assets/1-dimensional-toroidal/AA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/AB.png">|
|<img src="../assets/1-dimensional-toroidal/AB.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BB.png">|<img src="../assets/1-dimensional-toroidal/BB.png">|<img src="../assets/1-dimensional-toroidal/BA.png"> $~~~~$ <img src="../assets/1-dimensional-toroidal/BB.png">|

```java
import dev.irzinfante.wfc4j.model.TileMap1D;
import dev.irzinfante.wfc4j.enums.Side1D;

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
```

Now we can instantiate the API class with a 5 elements long grid:

```java
import dev.irzinfante.wfc4j.api.ToroidalWFC1D;

var gridSize = 5;
```

We will run 2 examples:

- For the first one we won't impose any restriction:

  ```java
  var WFC_1 = new ToroidalWFC1D<String>(tileMap, gridSize);
  WFC_1.run();
  ```

  <img src="../assets/1-dimensional-toroidal/result1.png">

- In the second one we will impose some constraints:

  ```java
  import java.util.Set;
  import java.util.Arrays;

  var entropy = new HashMap<Integer, Set<Tile<String>>>();
  entropy.put(0, new HashSet<>(Arrays.asList(AA)));
  entropy.put(gridSize / 2, new HashSet<>(Arrays.asList(BB)));
  entropy.put(gridSize - 1, new HashSet<>(Arrays.asList(AA)));

  var WFC_2 = new ToroidalWFC1D<String>(tileMap, gridSize, entropy);
  WFC_2.run();
  ```

  <img src="../assets/1-dimensional-toroidal/result2.png">
