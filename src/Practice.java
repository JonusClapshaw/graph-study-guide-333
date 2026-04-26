import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Practice {

  /**
   * Returns the count of vertices with odd values that can be reached from the given starting vertex.
   * The starting vertex is included in the count if its value is odd.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   * Consider a graph where:
   *   5 --> 4
   *   |     |
   *   v     v
   *   8 --> 7 < -- 1
   *   |
   *   v
   *   9
   * 
   * Starting from 5, the odd nodes that can be reached are 5, 7, and 9.
   * Thus, given 5, the number of reachable odd nodes is 3.
   * @param starting the starting vertex (may be null)
   * @return the number of vertices with odd values reachable from the starting vertex
   */
  public static int oddVertices(Vertex<Integer> starting) {
    Set<Vertex<Integer>> seen = new HashSet<>();
    return oddVertices(starting, seen, 0);
  }

  private static int oddVertices(Vertex<Integer> starting, Set<Vertex<Integer>> seen, int count){
    if(starting == null || seen.contains(starting)) return count;

    seen.add(starting);

    if(starting.data % 2 != 0) {
      count++;
    }

    for(Vertex<Integer> cur : starting.neighbors){
      count = oddVertices(cur, seen, count);
    }
    return count;
  }

  /**
   * Returns a *sorted* list of all values reachable from the starting vertex (including the starting vertex itself).
   * If duplicate vertex data exists, duplicates should appear in the output.
   * If the starting vertex is null, returns an empty list.
   * They should be sorted in ascending numerical order.
   *
   * Example:
   * Consider a graph where:
   *   5 --> 8
   *   |     |
   *   v     v
   *   8 --> 2 <-- 4
   * When starting from the vertex with value 5, the output should be:
   *   [2, 5, 8, 8]
   *
   * @param starting the starting vertex (may be null)
   * @return a sorted list of all reachable vertex values by 
   */
  public static List<Integer> sortedReachable(Vertex<Integer> starting) {
    List<Integer> numbers = new ArrayList<>();
    Set<Vertex<Integer>> seen = new HashSet<>();
    
    dfs(starting, seen, numbers);

    Collections.sort(numbers);
    return numbers;
  }

  private static void dfs(Vertex<Integer> starting, Set<Vertex<Integer>> seen, List<Integer> numbers){
    if(starting == null) return;
    seen.add(starting);
    numbers.add(starting.data);
    for(Vertex<Integer> neighbor : starting.neighbors){
      dfs(neighbor, seen, numbers);
    }
  }

  /**
   * Returns a sorted list of all values reachable from the given starting vertex in the provided graph.
   * The graph is represented as a map where each key is a vertex and its corresponding value is a set of neighbors.
   * It is assumed that there are no duplicate vertices.
   * If the starting vertex is not present as a key in the map, returns an empty list.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @return a sorted list of all reachable vertex values
   */
  public static List<Integer> sortedReachable(Map<Integer, Set<Integer>> graph, int starting) {
    List<Integer> reachable = new ArrayList<>();
    Set<Integer> visited = new HashSet<>();

    if(graph == null || !graph.containsKey(starting)) return reachable;

    sortedReachable(graph, starting, visited, reachable);

    Collections.sort(reachable);
    return reachable;
  }

  public static void sortedReachable(Map<Integer, Set<Integer>> graph, int starting, Set<Integer> visited, List<Integer> reachable){
    if(graph == null) return;

    if(visited.contains(starting)) return;
    visited.add(starting);
    reachable.add(starting);

    for(Integer neighbor : graph.get(starting)){
      sortedReachable(graph, neighbor, visited, reachable);
    }
  }

  /**
   * Returns true if and only if it is possible both to reach v2 from v1 and to reach v1 from v2.
   * A vertex is always considered reachable from itself.
   * If either v1 or v2 is null or if one cannot reach the other, returns false.
   *
   * Example:
   * If v1 and v2 are connected in a cycle, the method should return true.
   * If v1 equals v2, the method should also return true.
   *
   * @param <T> the type of data stored in the vertex
   * @param v1 the starting vertex
   * @param v2 the target vertex
   * @return true if there is a two-way connection between v1 and v2, false otherwise
   */
  public static <T> boolean twoWay(Vertex<T> v1, Vertex<T> v2) {
    if(v1 == null || v2 == null) return false;
    if(v1 == v2) return true;

    return twoWay(v1, new HashSet<>(), v2) && twoWay(v2, new HashSet<>(), v1);
  }

  public static <T> boolean twoWay(Vertex<T> current, Set<Vertex<T>> seen, Vertex<T> target){
    if(current == null || seen.contains(current)) return false;
    if(current == target) return true;

    seen.add(current);

    for(Vertex<T> neighbor : current.neighbors){
      if(twoWay(neighbor, seen, target)) return true;
    }

    return false;
  }

  /**
   * Returns whether there exists a path from the starting to ending vertex that includes only positive values.
   * 
   * The graph is represented as a map where each key is a vertex and each value is a set of directly reachable neighbor vertices. A vertex is always considered reachable from itself.
   * If the starting or ending vertex is not positive or is not present in the keys of the map, or if no valid path exists,
   * returns false.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @param ending the ending vertex value
   * @return whether there exists a valid positive path from starting to ending
   */
  public static boolean positivePathExists(Map<Integer, Set<Integer>> graph, int starting, int ending) {
    if(graph == null || !graph.containsKey(starting)) return false;
  
    return positivePathExists(graph, starting, ending, new HashSet<>());
  }

  public static boolean positivePathExists(Map<Integer, Set<Integer>> graph, int starting, int ending, Set<Integer> seen){
    if(graph == null) return false;
    if(seen.contains(starting)) return false;
    if(starting <= 0) return false;

    seen.add(starting);

    if(starting == ending) return true;

    for(int neighbor : graph.get(starting)){
      if(positivePathExists(graph, neighbor, ending, seen)) return true;
    }

    return false;
  }

  /**
   * Returns true if a professional has anyone in their extended network (reachable through any number of links)
   * that works for the given company. The search includes the professional themself.
   * If the professional is null, returns false.
   *
   * @param person the professional to start the search from (may be null)
   * @param companyName the name of the company to check for employment
   * @return true if a person in the extended network works at the specified company, false otherwise
   */
  public static boolean hasExtendedConnectionAtCompany(Professional person, String companyName) {
    Set<String> seen = new HashSet<>();
    return hasExtendedConnectionAtCompany(person, companyName, seen);
  }

  public static boolean hasExtendedConnectionAtCompany(Professional person, String companyName, Set<String> seen) {
    if(person == null || person.getConnections() == null) return false;
    if(seen.contains(person.getName())) return false;
    if(person.getCompany().equals(companyName)) return true;
    seen.add(person.getName());

    for(Professional per : person.getConnections()){
      if(hasExtendedConnectionAtCompany(per, companyName, seen)) return true;
    }
    return false;
  }

  /**
   * Returns a list of possible next moves starting from a given position.
   * 
   * Starting from current, which is a [row, column] location, a player can move 
   * by one according to the directions provided.
   * 
   * The directions are given as a 2D array, where each inner array is a [row, column]
   * pair that describes a move. For example, if given the below array it would be possible
   * to move to the right, up, down, or down/left diagonally.
   * {
   *  {0, 1},  // Right
   *  {-1, 0}, // Up
   *  {1, 0},  // Down
   *  {1, -1}  // Down/left diagonal
   * }
   * 
   * However, the player can not move off the edge of the board, or onto any 
   * location that has an 'X' (capital X).
   * 
   * The possible moves are returned as a List of [row, column] pairs. The List
   * can be in any order.
   * 
   * Example:
   * 
   * board: 
   * {
   *  {' ', ' ', 'X'},
   *  {'X', ' ', ' '},
   *  {' ', ' ', ' '}
   * }
   * 
   * current:
   * {1, 2}
   * 
   * directions:
   * {
   *  {0, 1},  // Right
   *  {-1, 0}, // Up
   *  {1, 0},  // Down
   *  {1, -1}  // Down/left diagonal
   * }
   * 
   * expected output (order of list is unimportant):
   * [{2, 2}, {2, 1}]
   * 
   * Explanation:
   * The player starts at {1, 2}.
   * The four directions the player might have to go are right, up, down, and down/left (based on the directions array).
   * They cannot go right because that would go off the edge of the board.
   * They cannot go up because there is an X.
   * They can go down.
   * They can go down/left.
   * The resultant list has the two possible positions.
   * 
   * 
   * You can assume the board is rectangular.
   * You can assume valid input (no nulls, properly sized arrays, current is in-bounds,
   * directions only move 1 square, any row/column pairs are arrays of length 2,
   * directions are unique).
   * 
   * If there are no possible moves, the method returns an empty list.
   * 
   * @param board a rectangular array where 'X' represent an impassible location
   * @param current the [row, column] starting position of the player
   * @param directions an array of [row, column] possible directions
   * @return an unsorted list of next moves
   */
  public static List<int[]> nextMoves(char[][] board, int[] current, int[][] directions) {
    ArrayList<int[]> results = new ArrayList<>(); 

    for(int[] direction : directions){
      int newR = current[0] + direction[0];
      int newC = current[1] + direction[1];

      if(newR >= 0 && newC >= 0 && newR < board.length && newC < board[0].length){
        if(board[newR][newC] != 'X'){
          results.add(new int[]{newR, newC});
        }
      }
    }
    
    return results;
  }

  /* THIS IS ALL THE GENERATED QUESTIONS FOR EXTRA PRACTICE */

  /**
   * Returns the total number of vertices reachable from the starting vertex,
   * including the starting vertex itself.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   * Consider a graph where:
   *   1 --> 2
   *   |     |
   *   v     v
   *   3 --> 4
   *
   * Starting from 1, all reachable vertices are 1, 2, 3, and 4.
   * Thus, the count is 4.
   *
   * @param starting the starting vertex (may be null)
   * @return the total number of reachable vertices
   */
  public static int countReachable(Vertex<Integer> starting) {
    Set<Vertex<Integer>> seen = new HashSet<>();
    countReachable(starting, seen);
    return seen.size();
  }

  public static void countReachable(Vertex<Integer> starting, Set<Vertex<Integer>> seen){
    if(starting == null || seen.contains(starting)) return;

    seen.add(starting);

    for(Vertex<Integer> neighbor : starting.neighbors){
      countReachable(neighbor, seen);
    }
  }

  /**
   * Returns the maximum integer value among all vertices reachable from the
   * starting vertex, including the starting vertex itself.
   * If the starting vertex is null, returns Integer.MIN_VALUE.
   *
   * Example:
   * Consider a graph where:
   *   3 --> 8
   *   |     |
   *   v     v
   *   5 --> 1
   *
   * Starting from 3, the reachable values are 3, 8, 5, and 1.
   * The maximum value is 8.
   *
   * @param starting the starting vertex (may be null)
   * @return the maximum value among all reachable vertices, or Integer.MIN_VALUE if null
   */
  public static int maxValue(Vertex<Integer> starting) {
    Set<Vertex<Integer>> seen = new HashSet<>();
    int high = maxValue(starting, seen, Integer.MIN_VALUE);

    return high;
  }

  public static int maxValue(Vertex<Integer> starting, Set<Vertex<Integer>> seen, int highestValue){
    if(starting == null || seen.contains(starting)) return Integer.MIN_VALUE;

    seen.add(starting);

    if(highestValue < starting.data){
      highestValue = starting.data;
    }

    for(Vertex<Integer> neighbor : starting.neighbors){
      highestValue = Math.max(highestValue, maxValue(neighbor, seen, highestValue));
    }

    return highestValue;
  }

  /**
   * Returns true if there is any path from starting to ending in the graph.
   * Unlike positivePathExists, all vertex values are permitted on the path.
   * A vertex is always considered reachable from itself.
   * If starting is not present as a key in the graph, returns false.
   *
   * Example:
   * Graph:
   *   1 -> {2}
   *   2 -> {-3}
   *   -3 -> {4}
   *   4 -> {}
   *
   * hasPath(graph, 1, 4) returns true even though -3 is on the path.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @param ending the ending vertex value
   * @return true if a path exists from starting to ending, false otherwise
   */
  public static boolean hasPath(Map<Integer, Set<Integer>> graph, int starting, int ending) {
    if(!graph.containsKey(starting)) return false;
    Set<Integer> seen = new HashSet<>();
    return bfs(graph, starting, ending, seen);
  }

  public static boolean bfs(Map<Integer, Set<Integer>> graph, int current, int target, Set<Integer> seen){
    if(current == target) return true;
    if(graph == null || seen.contains(current)) return false;

    seen.add(current);

    for(int neighbor : graph.get(current)){
      if(bfs(graph, neighbor, target, seen)) return true;
    }
    return false;
  }

  /**
   * Returns the total number of unique professionals reachable in the extended
   * network starting from the given person, including the person themselves.
   * If person is null, returns 0.
   *
   * Example:
   * If Alice is directly connected to Bob and Carol, and Carol is connected to Dave,
   * then networkSize(Alice) = 4 (Alice, Bob, Carol, Dave).
   *
   * @param person the professional to start from (may be null)
   * @return the count of unique professionals in the extended network
   */
  public static int networkSize(Professional person) {
    return 0;
  }

  /**
   * Returns the sum of all reachable vertex values from the starting vertex,
   * including the starting vertex itself.
   *
   * If the graph contains cycles, each vertex should only be counted once.
   * If starting is null, returns 0.
   *
   * Example:
   *   2 -> 5
   *   |    |
   *   v    v
   *   3 -> 7
   * Sum from 2 is 2 + 5 + 3 + 7 = 17.
   *
   * @param starting the starting vertex (may be null)
   * @return the sum of unique reachable vertex values
   */
  public static int sumReachable(Vertex<Integer> starting) {
    Set<Vertex<Integer>> seen = new HashSet<>();
    int sum = 0;
    sum = dfs(starting, seen);
    return sum;
  }

  public static int dfs(Vertex<Integer> starting, Set<Vertex<Integer>> seen){
    if(starting == null || seen.contains(starting)) return 0;

    seen.add(starting);
    int sum = starting.data;

    for(Vertex<Integer> neighbor : starting.neighbors){
      sum += dfs(neighbor, seen);
    }
    return sum;
  }

  /**
   * Returns true if every vertex reachable from starting has a non-negative value.
   * A value of 0 is considered non-negative.
   *
   * If graph is null or starting is not present as a key in graph, returns false.
   *
   * Example:
   *   1 -> {2, 0}
   *   2 -> {3}
   *   0 -> {}
   *   3 -> {}
   * allReachableNonNegative(graph, 1) is true.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @return true if all reachable vertices are non-negative, false otherwise
   */
  public static boolean allReachableNonNegative(Map<Integer, Set<Integer>> graph, int starting) {
    if(graph == null || !graph.containsKey(starting)) return false;
    Set<Integer> seen = new HashSet<>();
    return allReachableNonNegative(graph, starting, seen);
  }

  public static boolean allReachableNonNegative(Map<Integer, Set<Integer>> graph, int current, Set<Integer> seen){
    if(seen.contains(current)) return false;
    if(current < 0) return false;

    seen.add(current);

    for(int cur : graph.getOrDefault(current, Collections.emptySet())){
      if(!allReachableNonNegative(graph, cur, seen)) return false;
    }
      
    return true;
  }

  /**
   * Returns the minimum number of edges in any path from starting to ending.
   *
   * If starting equals ending and starting exists in the graph, return 0.
   * If no path exists, or starting is not present as a key, return -1.
   *
   * Example:
   *   1 -> {2, 3}
   *   2 -> {4}
   *   3 -> {4}
   *   4 -> {}
   * shortestPathLength(graph, 1, 4) is 2.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @param ending the ending vertex value
   * @return minimum edge count from starting to ending, or -1 if unreachable
   */
  public static int shortestPathLength(Map<Integer, Set<Integer>> graph, int starting, int ending) {
    if(graph == null || !graph.containsKey(starting)) return -1;
    if(starting == ending) return 0;

    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    queue.offer(starting);
    seen.add(starting);

    int size = 0;

    while(!queue.isEmpty()){
      int dis = queue.size();
      for(int i = 0; i < dis; i++){
        int cur = queue.poll();

        if(cur == ending) return size;

        for(int neighbor : graph.get(cur)){
          if(!seen.contains(neighbor)){
            queue.offer(neighbor);
            seen.add(neighbor);
          }
        }
      }
      size++;
    }

    return -1;
  }

  /**
   * Returns the minimum integer value among all vertices reachable from the
   * starting vertex, including the starting vertex itself.
   * If the starting vertex is null, returns Integer.MAX_VALUE.
   *
   * Example:
   *   6 --> 2
   *   |     |
   *   v     v
   *   9 --> 4
   * Starting from 6, reachable values are 6, 2, 9, and 4. Minimum is 2.
   *
   * @param starting the starting vertex (may be null)
   * @return the minimum value among all reachable vertices, or Integer.MAX_VALUE if null
   */
  public static int minValue(Vertex<Integer> starting) {
    int min = Integer.MAX_VALUE;
    return minValue(starting, new HashSet<>(), min);
  }

  public static int minValue(Vertex<Integer> starting, Set<Vertex<Integer>> seen, int min){
    if(starting == null || seen.contains(starting)) return min;

    seen.add(starting);

    if(starting.data < min){
      min = starting.data;
    }

    for(Vertex<Integer> cur : starting.neighbors){
      min = minValue(cur, seen, min);
    }

    return min;
  }

  /**
   * Returns the count of vertices with even values reachable from the starting vertex,
   * including the starting vertex if its value is even.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   *   2 --> 5
   *   |     |
   *   v     v
   *   8 --> 3
   *          |
   *          v
   *          6
   * Even vertices reachable from 2 are: 2, 8, and 6. Count is 3.
   *
   * @param starting the starting vertex (may be null)
   * @return the count of reachable vertices with even values
   */
  public static int evenVertices(Vertex<Integer> starting) {
    return evenVerticies(starting, new HashSet<>(), 0);
  }

  public static int evenVerticies(Vertex<Integer> starting, Set<Vertex<Integer>> seen, int count){
    if(seen.contains(starting) || starting == null) return count;

    seen.add(starting);

    if(starting.data % 2 == 0) {
      count++;
    }

    for(Vertex<Integer> num : starting.neighbors){
      count = evenVerticies(num, seen, count);
    }
    return count;
  }

  /**
   * Returns true if the target value is reachable from starting in the graph.
   * The starting vertex itself is considered reachable.
   * If graph is null or starting is not present as a key, returns false.
   *
   * Example:
   *   1 -> {2, 3}
   *   2 -> {4}
   *   3 -> {}
   *   4 -> {}
   * containsReachable(graph, 1, 4) is true.
   * containsReachable(graph, 1, 99) is false.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @param target the value to search for
   * @return true if target is reachable from starting
   */
  public static boolean containsReachable(Map<Integer, Set<Integer>> graph, int starting, int target) {
    if(graph == null || !graph.containsKey(starting)) return false;

    Set<Integer> seen = new HashSet<>();

    return containsReachable(graph, starting, target, seen);
  }

  public static boolean containsReachable(Map<Integer, Set<Integer>> graph, int starting, int target, Set<Integer> seen){
    if(seen.contains(starting)) return false;
    if(starting == target) return true;

    seen.add(starting);

    for(int neighbor : graph.get(starting)){
      if(containsReachable(graph, neighbor, target)) return true;
    }

    return false;
  }

  /**
   * Returns the count of vertices with values above the given threshold that can be reached
   * from the starting vertex, including the starting vertex if its value is above the threshold.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   *   10 --> 5
   *   |      |
   *   v      v
   *   8 --> 3
   * Starting from 10, vertices with value > 6 are: 10 and 8. Count is 2.
   *
   * @param starting the starting vertex (may be null)
   * @param threshold the threshold value
   * @return the count of reachable vertices with values > threshold
   */
  public static int countVerticesAboveThreshold(Vertex<Integer> starting, int threshold) {
    Set<Vertex<Integer>> seen = new HashSet<>();
    return countVerticesAboveThreshold(starting, threshold, seen, 0);
  }

  public static int countVerticesAboveThreshold(Vertex<Integer> starting, int threshold, Set<Vertex<Integer>> seen, int count){
    if(starting == null || seen.contains(starting)) return count;

    seen.add(starting);

    if(starting.data > threshold){
      count++;
    }

    for(Vertex<Integer> val : starting.neighbors){
      count = countVerticesAboveThreshold(val, threshold, seen, count);
    }

    return count;
  }

  /**
   * Returns true if all vertices reachable from starting have even values.
   * Returns true if the starting vertex is null.
   * Returns false if any reachable vertex has an odd value.
   *
   * Example:
   *   2 --> 4
   *   |     |
   *   v     v
   *   6 --> 8
   * Starting from 2, all reachable vertices (2, 4, 6, 8) are even. Result is true.
   *
   * Example 2:
   *   2 --> 3
   *   |     |
   *   v     v
   *   4 --> 8
   * Starting from 2, vertex 3 is odd and reachable. Result is false.
   *
   * @param starting the starting vertex (may be null)
   * @return true if all reachable vertices are even, false otherwise
   */
  public static boolean allReachableEven(Vertex<Integer> starting) {
    Set<Integer> seen = new HashSet<>();
    return allReachableEven(starting, seen);
  }

  private static boolean allReachableEven(Vertex<Integer> starting, Set<Integer> seen){
    if(starting == null) return true;
    if(seen.contains(starting.data)) return false;

    seen.add(starting.data);

    if(starting.data % 2 != 0) return false;
    
    int num = starting.data;

    for(Vertex<Integer> cur: starting.neighbors){
      if(!allReachableEven(cur, seen)) return false;
    }

    return true;
  }

  /**
   * Returns the maximum sum of any single path starting from starting.
   * The path includes the starting vertex value and is the sum of one continuous path downward.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   *   5 --> 3 --> 8
   *         |
   *         v
   *         2 --> 1
   * Paths from 5: 5->3->8 (sum=16), 5->3->2->1 (sum=11)
   * Maximum path sum is 16.
   *
   * @param starting the starting vertex (may be null)
   * @return the maximum path sum from the starting vertex
   */
  public static int maxPathSum(Vertex<Integer> starting) {
    return maxPathSum(starting, new HashSet<>(), 0);
  }

  public static int maxPathSum(Vertex<Integer> starting, Set<Vertex<Integer>> seen, int maxSum) {
    if(starting == null || seen.contains(starting)) return 0;

    seen.add(starting);

    int bestChildPath = 0;
    for(Vertex<Integer> cur : starting.neighbors){
      int childPath = maxPathSum(cur, seen, maxSum);
      bestChildPath = Math.max(bestChildPath, childPath);
    }

    seen.remove(starting);
    return starting.data + bestChildPath;
  }
}
