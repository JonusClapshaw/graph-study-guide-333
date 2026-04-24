import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    List<Integer> reachable = new ArrayList<>();
    Set<Vertex<Integer>> visited = new HashSet<>();

    sortedReachable(starting, visited, reachable);

    Collections.sort(reachable);
    return reachable;
    
  }

  public static void sortedReachable(Vertex<Integer> starting, Set<Vertex<Integer>> visited, List<Integer> reachable) {
    if(starting == null || visited.contains(starting)) return;

    visited.add(starting);

    reachable.add(starting.data);

    for(Vertex<Integer> neighbor : starting.neighbors) {
      sortedReachable(neighbor, visited, reachable);
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
    return 0;
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
    return Integer.MIN_VALUE;
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
}
