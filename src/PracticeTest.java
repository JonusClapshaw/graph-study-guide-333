import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class PracticeTest {

  // --- Helper method to build the complex graph (Vertex-based) ---
  // The graph structure is:
  //    v3  = 3
  //    v7  = 7
  //    v12 = 12
  //    v34 = 34
  //    v56 = 56
  //    v78 = 78
  //    v91 = 91
  //    v45 = 45
  //    v23 = 23
  //    v67 = 67  (not connected from v3)
  //
  // Neighbors are assigned as follows:
  //    v3.neighbors  = [v7, v34]
  //    v7.neighbors  = [v12, v45, v34, v56]
  //    v12.neighbors = [v7, v56, v78]
  //    v34.neighbors = [v34, v91]    // self-loop on v34
  //    v56.neighbors = [v78]
  //    v78.neighbors = [v91]
  //    v91.neighbors = [v56]         // cycle between v91 and v56
  //    v45.neighbors = [v23]
  //    v23.neighbors = []
  //    v67.neighbors = [v91]         // v67 is isolated from the rest (not reachable from v3)
  private Vertex<Integer>[] createComplexGraph() {
    Vertex<Integer> v3  = new Vertex<>(3);
    Vertex<Integer> v7  = new Vertex<>(7);
    Vertex<Integer> v12 = new Vertex<>(12);
    Vertex<Integer> v34 = new Vertex<>(34);
    Vertex<Integer> v56 = new Vertex<>(56);
    Vertex<Integer> v78 = new Vertex<>(78);
    Vertex<Integer> v91 = new Vertex<>(91);
    Vertex<Integer> v45 = new Vertex<>(45);
    Vertex<Integer> v23 = new Vertex<>(23);
    Vertex<Integer> v67 = new Vertex<>(67);

    v3.neighbors  = new ArrayList<>(List.of(v7, v34));
    v7.neighbors  = new ArrayList<>(List.of(v12, v45, v34, v56));
    v12.neighbors = new ArrayList<>(List.of(v7, v56, v78));
    v34.neighbors = new ArrayList<>(List.of(v34, v91));
    v56.neighbors = new ArrayList<>(List.of(v78));
    v78.neighbors = new ArrayList<>(List.of(v91));
    v91.neighbors = new ArrayList<>(List.of(v56));
    v45.neighbors = new ArrayList<>(List.of(v23));
    v23.neighbors = new ArrayList<>();
    v67.neighbors = new ArrayList<>(List.of(v91));

    // Return an array with v3 as the starting vertex
    return new Vertex[]{v3, v7, v12, v34, v56, v78, v91, v45, v23, v67};
  }

  // --- Tests for oddVertices(Vertex<Integer> starting) ---

  @Test
  public void testOddVertices_NullInput() {
    // When the starting vertex is null, the count should be 0.
    assertEquals(0, Practice.oddVertices(null));
  }

  @Test
  public void testOddVertices_SimpleGraph() {
    // Create a simple graph:
    //   5 --> 4
    //   |     |
    //   v     v
    //   8 --> 7
    //          \
    //           v
    //           9
    // Expected odd vertices from 5: 5, 7, and 9 => count = 3.
    Vertex<Integer> v5 = new Vertex<>(5);
    Vertex<Integer> v4 = new Vertex<>(4);
    Vertex<Integer> v8 = new Vertex<>(8);
    Vertex<Integer> v7 = new Vertex<>(7);
    Vertex<Integer> v9 = new Vertex<>(9);
    
    v5.neighbors.add(v4);
    v5.neighbors.add(v8);
    v4.neighbors.add(v7);
    v8.neighbors.add(v7);
    v8.neighbors.add(v9);

    assertEquals(3, Practice.oddVertices(v5));
  }

  @Test
  public void testOddVertices_ComplexGraph() {
    // Using the complex graph created above.
    // Reachable vertices from v3:
    //   v3 (3, odd)
    //   v7 (7, odd)
    //   v12 (12, even)
    //   v34 (34, even)
    //   v45 (45, odd)
    //   v56 (56, even)
    //   v78 (78, even)
    //   v91 (91, odd)
    //   v23 (23, odd)
    // (v67 is not reachable from v3)
    // Thus, the odd vertices are: 3, 7, 45, 91, and 23 (total count = 5).
    Vertex<Integer>[] vertices = createComplexGraph();
    Vertex<Integer> v3 = vertices[0];
    assertEquals(5, Practice.oddVertices(v3));
  }

  // --- Additional tests for oddVertices ---

  @Test
  public void testOddVertices_SingleNodeOdd() {
    // Single vertex with odd value; should count as 1.
    Vertex<Integer> v = new Vertex<>(11);
    assertEquals(1, Practice.oddVertices(v));
  }

  @Test
  public void testOddVertices_SingleNodeEven() {
    // Single vertex with even value; should count as 0.
    Vertex<Integer> v = new Vertex<>(10);
    assertEquals(0, Practice.oddVertices(v));
  }

  @Test
  public void testOddVertices_CycleAllOdd() {
    // Create a cycle of three odd-valued vertices.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v3 = new Vertex<>(3);
    Vertex<Integer> v5 = new Vertex<>(5);
    v1.neighbors.add(v3);
    v3.neighbors.add(v5);
    v5.neighbors.add(v1);
    // All three are odd, should count = 3.
    assertEquals(3, Practice.oddVertices(v1));
  }

  @Test
  public void testOddVertices_CycleNoneOdd() {
    // Create a cycle of even-valued vertices.
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v4 = new Vertex<>(4);
    v2.neighbors.add(v4);
    v4.neighbors.add(v2);
    // Neither is odd, should count = 0.
    assertEquals(0, Practice.oddVertices(v2));
  }

  @Test
  public void testOddVertices_DisconnectedNodeNotVisited() {
    // Graph with two components; only one component reachable should be counted.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    // v1 -> v2; v3 is isolated.
    v1.neighbors.add(v2);
    // v3 not attached.
    assertEquals(1, Practice.oddVertices(v1)); // only v1 is odd in reachable component.
  }

  // --- Tests for sortedReachable(Vertex<Integer> starting) ---

  @Test
  public void testSortedReachable_NullInput() {
    List<Integer> result = Practice.sortedReachable(null);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSortedReachable_SimpleGraph() {
    // Graph structure:
    //         5
    //        / \
    //      8a   8b
    //       |
    //       v
    //       2
    // DFS should collect: 5, 8, 2, 8.
    // Sorted order: [2, 5, 8, 8]
    Vertex<Integer> v5 = new Vertex<>(5);
    Vertex<Integer> v8a = new Vertex<>(8);
    Vertex<Integer> v8b = new Vertex<>(8);
    Vertex<Integer> v2 = new Vertex<>(2);
    
    v5.neighbors.add(v8a);
    v5.neighbors.add(v8b);
    v8a.neighbors.add(v2);

    List<Integer> expected = Arrays.asList(2, 5, 8, 8);
    assertEquals(expected, Practice.sortedReachable(v5));
  }

  @Test
  public void testSortedReachable_ComplexGraph() {
    // Using the complex graph:
    // Reachable values from v3: 3, 7, 12, 34, 45, 56, 78, 91, 23.
    // Sorted ascending: [3, 7, 12, 23, 34, 45, 56, 78, 91]
    Vertex<Integer>[] vertices = createComplexGraph();
    Vertex<Integer> v3 = vertices[0];
    List<Integer> expected = Arrays.asList(3, 7, 12, 23, 34, 45, 56, 78, 91);
    assertEquals(expected, Practice.sortedReachable(v3));
  }

  // --- Additional tests for sortedReachable(Vertex<Integer>) ---

  @Test
  public void testSortedReachable_SingleNode() {
    // Single vertex; should return list with that one value.
    Vertex<Integer> v = new Vertex<>(42);
    List<Integer> result = Practice.sortedReachable(v);
    assertEquals(Collections.singletonList(42), result);
  }

  @Test
  public void testSortedReachable_CycleWithDuplicates() {
    // Create a cycle where two nodes have the same value.
    Vertex<Integer> v1 = new Vertex<>(2);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    v2.neighbors.add(v3);
    v3.neighbors.add(v1);
    // Values reachable: 2, 2, 3. Sorted: [2, 2, 3]
    List<Integer> expected = Arrays.asList(2, 2, 3);
    assertEquals(expected, Practice.sortedReachable(v1));
  }

  @Test
  public void testSortedReachable_DisconnectedSubgraphIgnored() {
    // v1 -> v2; v3 is disconnected.
    Vertex<Integer> v1 = new Vertex<>(5);
    Vertex<Integer> v2 = new Vertex<>(1);
    Vertex<Integer> v3 = new Vertex<>(7);
    v1.neighbors.add(v2);
    // v3 is not connected.
    List<Integer> result = Practice.sortedReachable(v1);
    // Only [1, 5] should appear, sorted.
    assertEquals(Arrays.asList(1, 5), result);
  }

  // --- Tests for sortedReachable(Map<Integer, Set<Integer>> graph, int starting) ---

  @Test
  public void testSortedReachable_MapGraph_StartingPresent() {
    // Simple map graph:
    // 1 -> {2, 3}
    // 2 -> {4}
    // 3 -> {}
    // 4 -> {}
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new LinkedHashSet<>(Arrays.asList(2, 3)));
    graph.put(2, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(3, new LinkedHashSet<>());
    graph.put(4, new LinkedHashSet<>());
    
    List<Integer> expected = Arrays.asList(1, 2, 3, 4);
    assertEquals(expected, Practice.sortedReachable(graph, 1));
  }

  @Test
  public void testSortedReachable_MapGraph_StartingNotPresent() {
    // Same graph as above; starting vertex 5 is not a key.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new LinkedHashSet<>(Arrays.asList(2, 3)));
    graph.put(2, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(3, new LinkedHashSet<>());
    graph.put(4, new LinkedHashSet<>());
    
    List<Integer> result = Practice.sortedReachable(graph, 5);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  // --- Additional tests for sortedReachable(Map<Integer, Set<Integer>>, int) ---

  @Test
  public void testSortedReachable_MapGraph_SingleNode() {
    // Graph with a single node pointing to itself.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(10, new HashSet<>(List.of(10)));
    // Reachable from 10: {10}, sorted -> [10]
    assertEquals(Collections.singletonList(10), Practice.sortedReachable(graph, 10));
  }

  @Test
  public void testSortedReachable_MapGraph_Cycle() {
    // Graph with a cycle: 1 -> 2, 2 -> 3, 3 -> 1.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(List.of(2)));
    graph.put(2, new HashSet<>(List.of(3)));
    graph.put(3, new HashSet<>(List.of(1)));
    List<Integer> result = Practice.sortedReachable(graph, 1);
    // Reachable: {1,2,3}, sorted -> [1,2,3]
    assertEquals(Arrays.asList(1, 2, 3), result);
  }

  @Test
  public void testSortedReachable_MapGraph_DisconnectedIgnored() {
    // Graph: 1->2, 2->{}, 3->4, 4->{}; starting at 1 should ignore 3 and 4.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(List.of(2)));
    graph.put(2, new HashSet<>());
    graph.put(3, new HashSet<>(List.of(4)));
    graph.put(4, new HashSet<>());
    assertEquals(Arrays.asList(1, 2), Practice.sortedReachable(graph, 1));
  }

  // --- Tests for twoWay(Vertex<T> v1, Vertex<T> v2) ---

  @Test
  public void testTwoWay_BothNull() {
    assertFalse(Practice.twoWay(null, null));
  }

  @Test
  public void testTwoWay_SameVertex() {
    // A vertex is always reachable from itself.
    Vertex<Integer> v = new Vertex<>(10);
    assertTrue(Practice.twoWay(v, v));
  }

  @Test
  public void testTwoWay_DirectCycle() {
    // Two vertices with a direct two-way connection.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    v1.neighbors.add(v2);
    v2.neighbors.add(v1);
    assertTrue(Practice.twoWay(v1, v2));
  }

  @Test
  public void testTwoWay_OneWayConnection() {
    // One vertex can reach the other, but not vice versa.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    v1.neighbors.add(v2);
    assertFalse(Practice.twoWay(v1, v2));
  }

  @Test
  public void testTwoWay_IndirectConnection() {
    // Indirect cycle: v1 -> v2 -> v3 -> v1.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    v2.neighbors.add(v3);
    v3.neighbors.add(v1);
    assertTrue(Practice.twoWay(v1, v3));
  }

  // --- Additional tests for twoWay ---

  @Test
  public void testTwoWay_DisconnectedVertices() {
    // Two vertices with no path between them.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    // No neighbors added.
    assertFalse(Practice.twoWay(v1, v2));
  }

  @Test
  public void testTwoWay_SelfLoop() {
    // Vertex with a self-loop; should be reachable both ways trivially.
    Vertex<Integer> v = new Vertex<>(5);
    v.neighbors.add(v);
    assertTrue(Practice.twoWay(v, v));
  }

  @Test
  public void testTwoWay_MultiNodeNoBackPath() {
    // v1 -> v2 -> v3, but no path from v3 back to v1 or v2.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    v2.neighbors.add(v3);
    assertFalse(Practice.twoWay(v1, v3));
  }

  // --- Tests for positivePathExists(Map<Integer, Set<Integer>> graph, int starting, int ending) ---

  /**
   * Test that a vertex is always reachable from itself if it is positive.
   */
  @Test
  public void testSelfPath() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    // Graph contains a single positive vertex 5 with no neighbors.
    graph.put(5, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 5, 5));
  }

  /**
   * Test a small graph with a valid positive path:
   *  3 -> {4}
   *  4 -> {5}
   *  5 -> {}
   * Expected: There is a valid positive path from 3 to 5.
   */
  @Test
  public void testValidPositivePath_SmallGraph() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 3, 5));
  }

  /**
   * Test that if the ending vertex is missing from the graph, the result is false.
   */
  @Test
  public void testMissingEndingVertex() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    // Ending vertex 10 is not present.
    assertFalse(Practice.positivePathExists(graph, 3, 10));
  }

  /**
   * Test a graph where the only available path includes a negative vertex.
   * Graph:
   *   3 -> {-4}
   *   -4 -> {5}
   *   5 -> {}
   * Even though there is a path from 3 to 5, it includes -4 (non-positive),
   * so the method should return false.
   */
  @Test
  public void testPathIncludesNegative() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(-4)));
    graph.put(-4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 3, 5));
  }

  /**
   * Test a graph with a cycle where all vertices are positive.
   * Graph:
   *   1 -> {2}
   *   2 -> {3}
   *   3 -> {1, 4}
   *   4 -> {}
   * There is a valid cycle and a positive path from 1 to 4.
   */
  @Test
  public void testCycleValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>(Arrays.asList(3)));
    graph.put(3, new HashSet<>(Arrays.asList(1, 4)));
    graph.put(4, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a graph with a cycle that forces the use of a negative vertex.
   * Graph:
   *   1 -> {-2}
   *   -2 -> {3}
   *   3 -> {1, 4}
   *   4 -> {}
   * The only available path from 1 to 4 includes -2, so the result should be false.
   */
  @Test
  public void testCycleInvalidDueToNegative() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(-2)));
    graph.put(-2, new HashSet<>(Arrays.asList(3)));
    graph.put(3, new HashSet<>(Arrays.asList(1, 4)));
    graph.put(4, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a graph with multiple paths from the starting vertex to the ending vertex.
   * One path is valid while another includes a negative vertex.
   * Graph:
   *   3 -> {4, -2}
   *   4 -> {9}
   *   -2 -> {9}
   *   9 -> {}
   * Although the path 3->(-2)->9 is invalid, 3->4->9 is valid.
   */
  @Test
  public void testMultiplePathsOneValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4, -2)));
    graph.put(4, new HashSet<>(Arrays.asList(9)));
    graph.put(-2, new HashSet<>(Arrays.asList(9)));
    graph.put(9, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 3, 9));
  }

  /**
   * Test that if the starting vertex is not positive, the method returns false.
   * Graph:
   *   -3 -> {4}
   *   4 -> {5}
   *   5 -> {}
   */
  @Test
  public void testStartingNotPositive() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(-3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, -3, 5));
  }

  /**
   * Test that if the ending vertex is not positive, the method returns false.
   * Graph:
   *   3 -> {4}
   *   4 -> {-5}
   *   -5 -> {}
   */
  @Test
  public void testEndingNotPositive() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(-5)));
    graph.put(-5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 3, -5));
  }

  /**
   * Test a disconnected graph where the starting and ending vertices belong to different components.
   * Graph:
   *   Component 1: 1 -> {2}, 2 -> {}
   *   Component 2: 3 -> {4}, 4 -> {}
   * There is no path from 1 to 4.
   */
  @Test
  public void testDisconnectedGraph() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a complex graph with cycles and multiple valid paths.
   * Graph structure:
   *   10 -> {20, 30, 40}
   *   20 -> {50, 60}
   *   30 -> {20, 70}
   *   40 -> {80}
   *   50 -> {90}
   *   60 -> {30, 90}  // cycle: 30 -> 20 -> 60 -> 30
   *   70 -> {}
   *   80 -> {90}
   *   90 -> {}
   * A valid positive path exists from 10 to 90 (e.g., 10 -> 20 -> 50 -> 90).
   */
  @Test
  public void testComplexGraphValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(10, new HashSet<>(Arrays.asList(20, 30, 40)));
    graph.put(20, new HashSet<>(Arrays.asList(50, 60)));
    graph.put(30, new HashSet<>(Arrays.asList(20, 70)));
    graph.put(40, new HashSet<>(Arrays.asList(80)));
    graph.put(50, new HashSet<>(Arrays.asList(90)));
    graph.put(60, new HashSet<>(Arrays.asList(30, 90)));
    graph.put(70, new HashSet<>());
    graph.put(80, new HashSet<>(Arrays.asList(90)));
    graph.put(90, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 10, 90));
  }

  /**
   * Test a complex graph where every path from the starting vertex to the ending vertex includes a negative vertex.
   * Modified graph structure:
   *   10 -> {20, 30}
   *   20 -> {-50}     // now, 20 only leads to -50
   *   30 -> {20, 70}
   *   -50 -> {90}
   *   70 -> {}
   *   90 -> {}
   * Every path from 10 to 90 must pass through 20 then -50.
   */
  @Test
  public void testComplexGraphInvalid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(10, new HashSet<>(Arrays.asList(20, 30)));
    graph.put(20, new HashSet<>(Arrays.asList(-50)));
    graph.put(30, new HashSet<>(Arrays.asList(20, 70)));
    graph.put(-50, new HashSet<>(Arrays.asList(90)));
    graph.put(70, new HashSet<>());
    graph.put(90, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 10, 90));
  }

  // --- Tests for hasExtendedConnectionAtCompany(Professional person, String companyName) ---

  /**
   * Test that if the professional is null, the method returns false.
   */
  @Test
  public void testNullProfessional() {
    assertFalse(Practice.hasExtendedConnectionAtCompany(null, "Acme Corp"));
  }

  /**
   * Test that if the professional themself works for the target company,
   * the method returns true.
   */
  @Test
  public void testSelfWorksForCompany() {
    // Using "Tech Solutions" as the target.
    Professional alice = new Professional("Alice", "Tech Solutions", 10, new HashSet<>());
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Tech Solutions"));
  }

  /**
   * Test a direct connection:
   * Alice works at "Global Inc.", and her direct connection Bob works at "Innovative LLC".
   */
  @Test
  public void testDirectConnection() {
    Professional bob = new Professional("Bob", "Innovative LLC", 8, new HashSet<>());
    Set<Professional> aliceConnections = new HashSet<>();
    aliceConnections.add(bob);
    Professional alice = new Professional("Alice", "Global Inc.", 5, aliceConnections);
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Innovative LLC"));
  }

  /**
   * Test an indirect connection:
   * Create a chain: Alice -> Charlie -> Bob,
   * where Bob works at "Tech Solutions".
   */
  @Test
  public void testIndirectConnection() {
    Professional bob = new Professional("Bob", "Tech Solutions", 8, new HashSet<>());
    Set<Professional> charlieConnections = new HashSet<>();
    charlieConnections.add(bob);
    Professional charlie = new Professional("Charlie", "Other Corp", 7, charlieConnections);
    
    Set<Professional> aliceConnections = new HashSet<>();
    aliceConnections.add(charlie);
    Professional alice = new Professional("Alice", "Global Inc.", 5, aliceConnections);
    
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Tech Solutions"));
  }

  /**
   * Test a network with a cycle.
   * Create a cycle: A -> B -> C -> A, where B works at "FutureTech".
   */
  @Test
  public void testCycleInNetwork() {
    Professional a = new Professional("A", "Other Corp", 3, new HashSet<>());
    Professional b = new Professional("B", "FutureTech", 4, new HashSet<>());
    Professional c = new Professional("C", "Other Corp", 5, new HashSet<>());
    
    // Build cycle: A -> B -> C -> A
    a.getConnections().add(b);
    b.getConnections().add(c);
    c.getConnections().add(a);
    
    assertTrue(Practice.hasExtendedConnectionAtCompany(a, "FutureTech"));
  }

  /**
   * Test a network where no one in the extended network works for the target company.
   * In this case, none work for "Nonexistent Corp".
   */
  @Test
  public void testNoConnectionFound() {
    Professional a = new Professional("A", "Other Corp", 3, new HashSet<>());
    Professional b = new Professional("B", "Global Inc.", 4, new HashSet<>());
    Professional c = new Professional("C", "Tech Solutions", 5, new HashSet<>());
    
    a.getConnections().add(b);
    b.getConnections().add(c);
    
    assertFalse(Practice.hasExtendedConnectionAtCompany(a, "Nonexistent Corp"));
  }

  /**
   * Test a network with multiple branches.
   * Create a network:
   *   A -> {B, C}
   *   B -> {D, E}  where D works at "Global Inc." and E works at "Other Corp".
   *   C -> {F}     where F does not work for the target.
   * Target is "Global Inc.".
   */
  @Test
  public void testMultiplePaths() {
    Professional d = new Professional("D", "Global Inc.", 6, new HashSet<>());
    Professional e = new Professional("E", "Other Corp", 4, new HashSet<>());
    Professional b = new Professional("B", "Other Corp", 5, new HashSet<>(Arrays.asList(d, e)));
    Professional f = new Professional("F", "Other Corp", 4, new HashSet<>());
    Professional c = new Professional("C", "Other Corp", 3, new HashSet<>(Arrays.asList(f)));
    
    Set<Professional> aConnections = new HashSet<>(Arrays.asList(b, c));
    Professional a = new Professional("A", "Other Corp", 7, aConnections);
    
    assertTrue(Practice.hasExtendedConnectionAtCompany(a, "Global Inc."));
  }

  /**
   * Test a deep network where the target company is never found.
   * Create a chain: A -> B -> C -> D -> E, and none work for "UltraCorp".
   */
  @Test
  public void testDeepNetworkWithoutTarget() {
    Professional e = new Professional("E", "Other Corp", 4, new HashSet<>());
    Professional d = new Professional("D", "Other Corp", 5, new HashSet<>(Arrays.asList(e)));
    Professional c = new Professional("C", "Other Corp", 6, new HashSet<>(Arrays.asList(d)));
    Professional b = new Professional("B", "Other Corp", 3, new HashSet<>(Arrays.asList(c)));
    Professional a = new Professional("A", "Other Corp", 2, new HashSet<>(Arrays.asList(b)));
    
    assertFalse(Practice.hasExtendedConnectionAtCompany(a, "UltraCorp"));
  }

  /**
   * Test a complex network with multiple cycles and branches.
   * Network structure:
   *   A works at "Other Corp".
   *   A -> {B, C}
   *   B -> {D, E}
   *   C -> {F, G}
   *   D -> {A} (cycle)
   *   E -> {} and E works at "Innovative LLC"
   *   F -> {F} (self-loop)
   *   G -> {H}
   *   H -> {B} (cycle connecting back to B)
   * The target is "Innovative LLC".
   */
  @Test
  public void testNetworkWithMultipleCycles() {
    Professional e = new Professional("E", "Innovative LLC", 5, new HashSet<>());
    Professional d = new Professional("D", "Other Corp", 4, new HashSet<>());
    Professional b = new Professional("B", "Other Corp", 3, new HashSet<>());
    Professional f = new Professional("F", "Other Corp", 4, new HashSet<>());
    Professional g = new Professional("G", "Other Corp", 4, new HashSet<>());
    Professional h = new Professional("H", "Other Corp", 3, new HashSet<>());
    Professional c = new Professional("C", "Other Corp", 6, new HashSet<>());
    Professional a = new Professional("A", "Other Corp", 7, new HashSet<>());

    // Build connections manually:
    a.getConnections().add(b);
    a.getConnections().add(c);
    
    b.getConnections().add(d);
    b.getConnections().add(e);
    
    c.getConnections().add(f);
    c.getConnections().add(g);
    
    d.getConnections().add(a); // cycle: D -> A
    g.getConnections().add(h);
    h.getConnections().add(b); // cycle: H -> B
    f.getConnections().add(f); // self-loop on F
    
    assertTrue(Practice.hasExtendedConnectionAtCompany(a, "Innovative LLC"));
  }

  // helper method for testing nextMoves
  private static Set<String> toSet(List<int[]> moves) {
    Set<String> set = new HashSet<>();
    for (int[] move : moves) {
      set.add(move[0] + "," + move[1]);
    }
    return set;
  }

  @Test
  public void testNextMoves_exampleFromJavadoc() {
    char[][] board = {
      {' ', ' ', 'X'},
      {'X', ' ', ' '},
      {' ', ' ', ' '}
    };
    int[] current = {1, 2};
    int[][] directions = {
      {0, 1},
      {-1, 0},
      {1, 0},
      {1, -1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Arrays.asList("2,2", "2,1"));

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_oneByOneBoard() {
    char[][] board = {
      {' '}
    };
    int[] current = {0, 0};
    int[][] directions = {
      {0, 1},
      {1, 0},
      {-1, 0},
      {0, -1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = Collections.emptySet();

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_moreRowsThanCols_bottomRight() {
    char[][] board = {
      {' ', ' '},
      {' ', ' '},
      {' ', ' '},
      {' ', ' '}
    };
    int[] current = {3, 1};
    int[][] directions = {
      {0, 1},
      {1, 0},
      {-1, 0},
      {0, -1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Arrays.asList("2,1", "3,0"));

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_moreColsThanRows_bottomRight() {
    char[][] board = {
      {' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' '}
    };
    int[] current = {1, 3};
    int[][] directions = {
      {0, 1},
      {1, 0},
      {-1, 0},
      {0, -1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Arrays.asList("0,3", "1,2"));

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_surroundedByXs() {
    char[][] board = {
      {'X', 'X', 'X'},
      {'X', ' ', 'X'},
      {'X', 'X', 'X'}
    };
    int[] current = {1, 1};
    int[][] directions = {
      {-1, -1}, {-1, 0}, {-1, 1},
      {0, -1},           {0, 1},
      {1, -1},  {1, 0},  {1, 1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = Collections.emptySet();

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_allDirectionsAvailable() {
    char[][] board = {
      {' ', ' ', ' '},
      {' ', ' ', ' '},
      {' ', ' ', ' '}
    };
    int[] current = {1, 1};
    int[][] directions = {
      {-1, -1}, {-1, 0}, {-1, 1},
      {0, -1},           {0, 1},
      {1, -1},  {1, 0},  {1, 1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Arrays.asList(
      "0,0", "0,1", "0,2",
      "1,0",       "1,2",
      "2,0", "2,1", "2,2"
    ));

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_topLeftCorner() {
    char[][] board = {
      {' ', ' ', ' '},
      {' ', ' ', ' '},
      {' ', ' ', ' '}
    };
    int[] current = {0, 0};
    int[][] directions = {
      {0, 1},
      {1, 0},
      {1, 1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Arrays.asList("0,1", "1,0", "1,1"));

    assertEquals(expected, actual);
  }

  @Test
  public void testNextMoves_singleDirection() {
    char[][] board = {
      {' ', ' ', ' '},
      {' ', ' ', ' '},
      {' ', ' ', ' '}
    };
    int[] current = {1, 1};
    int[][] directions = {
      {-1, -1}
    };

    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    Set<String> expected = new HashSet<>(Collections.singletonList("0,0"));

    assertEquals(expected, actual);
  }

  // --- Additional tests for nextMoves ---

  @Test
  public void testNextMoves_NoDirections() {
    char[][] board = {
      {' ', ' '},
      {' ', ' '}
    };
    int[] current = {0, 0};
    int[][] directions = {};
    // No directions means no moves.
    List<int[]> result = Practice.nextMoves(board, current, directions);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testNextMoves_AllBlockedByX() {
    char[][] board = {
      {'X', 'X', 'X'},
      {'X', ' ', 'X'},
      {'X', 'X', 'X'}
    };
    int[] current = {1, 1};
    int[][] directions = {
      {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };
    // All four adjacent cells are 'X'.
    List<int[]> result = Practice.nextMoves(board, current, directions);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testNextMoves_DiagonalOnly() {
    char[][] board = {
      {' ', 'X'},
      {'X', ' '}
    };
    int[] current = {0, 0};
    int[][] directions = {
      {1, 1}  // only the diagonal down-right is allowed
    };
    // That diagonal is open.
    List<int[]> result = Practice.nextMoves(board, current, directions);
    Set<String> actual = toSet(result);
    assertEquals(Collections.singleton("1,1"), actual);
  }

  // -----------------------------------------------------------------------
  // --- Tests for countReachable(Vertex<Integer> starting) ---
  // -----------------------------------------------------------------------

  @Test
  public void testCountReachable_NullInput() {
    // Null starting vertex should return 0.
    assertEquals(0, Practice.countReachable(null));
  }

  @Test
  public void testCountReachable_SingleNode() {
    // A single vertex with no neighbors counts as 1.
    Vertex<Integer> v = new Vertex<>(42);
    assertEquals(1, Practice.countReachable(v));
  }

  @Test
  public void testCountReachable_SimpleGraph() {
    // Graph:
    //   1 --> 2
    //   |     |
    //   v     v
    //   3 --> 4
    // All four vertices are reachable from 1.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    Vertex<Integer> v4 = new Vertex<>(4);
    v1.neighbors.add(v2);
    v1.neighbors.add(v3);
    v2.neighbors.add(v4);
    v3.neighbors.add(v4);
    assertEquals(4, Practice.countReachable(v1));
  }

  @Test
  public void testCountReachable_Cycle() {
    // Cycle of three vertices: all three are reachable but each visited once.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    v2.neighbors.add(v3);
    v3.neighbors.add(v1);
    assertEquals(3, Practice.countReachable(v1));
  }

  @Test
  public void testCountReachable_DisconnectedIgnored() {
    // v1 -> v2; v3 is isolated. Only 2 vertices reachable from v1.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    // v3 not connected.
    assertEquals(2, Practice.countReachable(v1));
  }

  @Test
  public void testCountReachable_ComplexGraph() {
    // Using the complex graph (9 vertices reachable from v3; v67 is isolated).
    Vertex<Integer>[] vertices = createComplexGraph();
    assertEquals(9, Practice.countReachable(vertices[0]));
  }

  // -----------------------------------------------------------------------
  // --- Tests for maxValue(Vertex<Integer> starting) ---
  // -----------------------------------------------------------------------

  @Test
  public void testMaxValue_NullInput() {
    // Null input should return Integer.MIN_VALUE.
    assertEquals(Integer.MIN_VALUE, Practice.maxValue(null));
  }

  @Test
  public void testMaxValue_SingleNode() {
    // Single vertex; max is its own value.
    Vertex<Integer> v = new Vertex<>(7);
    assertEquals(7, Practice.maxValue(v));
  }

  @Test
  public void testMaxValue_SimpleGraph() {
    // Graph:
    //   3 --> 8
    //   |     |
    //   v     v
    //   5 --> 1
    // Reachable values: 3, 8, 5, 1. Max = 8.
    Vertex<Integer> v3 = new Vertex<>(3);
    Vertex<Integer> v8 = new Vertex<>(8);
    Vertex<Integer> v5 = new Vertex<>(5);
    Vertex<Integer> v1 = new Vertex<>(1);
    v3.neighbors.add(v8);
    v3.neighbors.add(v5);
    v8.neighbors.add(v1);
    v5.neighbors.add(v1);
    assertEquals(8, Practice.maxValue(v3));
  }

  @Test
  public void testMaxValue_MaxAtStart() {
    // Starting vertex has the largest value.
    Vertex<Integer> v10 = new Vertex<>(10);
    Vertex<Integer> v2  = new Vertex<>(2);
    Vertex<Integer> v4  = new Vertex<>(4);
    v10.neighbors.add(v2);
    v10.neighbors.add(v4);
    assertEquals(10, Practice.maxValue(v10));
  }

  @Test
  public void testMaxValue_Cycle() {
    // Cycle: 4 -> 9 -> 2 -> 4. Max = 9.
    Vertex<Integer> v4 = new Vertex<>(4);
    Vertex<Integer> v9 = new Vertex<>(9);
    Vertex<Integer> v2 = new Vertex<>(2);
    v4.neighbors.add(v9);
    v9.neighbors.add(v2);
    v2.neighbors.add(v4);
    assertEquals(9, Practice.maxValue(v4));
  }

  @Test
  public void testMaxValue_ComplexGraph() {
    // Complex graph reachable from v3: values 3,7,12,34,45,56,78,91,23. Max = 91.
    Vertex<Integer>[] vertices = createComplexGraph();
    assertEquals(91, Practice.maxValue(vertices[0]));
  }

  // -----------------------------------------------------------------------
  // --- Tests for hasPath(Map<Integer, Set<Integer>> graph, int starting, int ending) ---
  // -----------------------------------------------------------------------

  @Test
  public void testHasPath_StartingNotInGraph() {
    // Starting vertex absent from the graph => false.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    assertFalse(Practice.hasPath(graph, 5, 2));
  }

  @Test
  public void testHasPath_SelfPath() {
    // A vertex can always reach itself.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>());
    assertTrue(Practice.hasPath(graph, 3, 3));
  }

  @Test
  public void testHasPath_DirectEdge() {
    // 1 -> 2; direct path exists.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    assertTrue(Practice.hasPath(graph, 1, 2));
  }

  @Test
  public void testHasPath_NegativeVertexAllowed() {
    // Path goes through a negative vertex: 1 -> -3 -> 4.
    // Unlike positivePathExists, this should still return true.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1,  new HashSet<>(Arrays.asList(-3)));
    graph.put(-3, new HashSet<>(Arrays.asList(4)));
    graph.put(4,  new HashSet<>());
    assertTrue(Practice.hasPath(graph, 1, 4));
  }

  @Test
  public void testHasPath_NoPathExists() {
    // Two disconnected components: 1->2 and 3->4.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>());
    assertFalse(Practice.hasPath(graph, 1, 4));
  }

  @Test
  public void testHasPath_Cycle() {
    // Cycle: 1 -> 2 -> 3 -> 1. Can reach any node from any node.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>(Arrays.asList(3)));
    graph.put(3, new HashSet<>(Arrays.asList(1)));
    assertTrue(Practice.hasPath(graph, 1, 3));
    assertTrue(Practice.hasPath(graph, 3, 2));
  }

  @Test
  public void testHasPath_EndingNotInGraph() {
    // Ending vertex is not a key; path cannot lead there.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    assertFalse(Practice.hasPath(graph, 1, 99));
  }

  // -----------------------------------------------------------------------
  // --- Tests for networkSize(Professional person) ---
  // -----------------------------------------------------------------------

  @Test
  public void testNetworkSize_NullInput() {
    // Null person should return 0.
    assertEquals(0, Practice.networkSize(null));
  }

  @Test
  public void testNetworkSize_SinglePerson() {
    // A professional with no connections counts as 1.
    Professional alice = new Professional("Alice", "Acme", 5, new HashSet<>());
    assertEquals(1, Practice.networkSize(alice));
  }

  @Test
  public void testNetworkSize_DirectConnections() {
    // Alice is directly connected to Bob and Carol. Total = 3.
    Professional bob   = new Professional("Bob",   "Acme", 3, new HashSet<>());
    Professional carol = new Professional("Carol", "Acme", 4, new HashSet<>());
    Professional alice = new Professional("Alice", "Acme", 5,
        new HashSet<>(Arrays.asList(bob, carol)));
    assertEquals(3, Practice.networkSize(alice));
  }

  @Test
  public void testNetworkSize_IndirectConnections() {
    // Chain: Alice -> Bob -> Carol -> Dave. Total = 4.
    Professional dave  = new Professional("Dave",  "Acme", 2, new HashSet<>());
    Professional carol = new Professional("Carol", "Acme", 3, new HashSet<>(Arrays.asList(dave)));
    Professional bob   = new Professional("Bob",   "Acme", 4, new HashSet<>(Arrays.asList(carol)));
    Professional alice = new Professional("Alice", "Acme", 5, new HashSet<>(Arrays.asList(bob)));
    assertEquals(4, Practice.networkSize(alice));
  }

  @Test
  public void testNetworkSize_Cycle() {
    // Cycle: A -> B -> C -> A. Should count each person once => 3.
    Professional a = new Professional("A", "Acme", 1, new HashSet<>());
    Professional b = new Professional("B", "Acme", 2, new HashSet<>());
    Professional c = new Professional("C", "Acme", 3, new HashSet<>());
    a.getConnections().add(b);
    b.getConnections().add(c);
    c.getConnections().add(a);
    assertEquals(3, Practice.networkSize(a));
  }

  @Test
  public void testNetworkSize_BranchedNetwork() {
    // Network:
    //   Alice -> {Bob, Carol}
    //   Bob   -> {Dave}
    //   Carol -> {Dave, Eve}
    //   Dave  -> {}
    //   Eve   -> {}
    // Total unique professionals = 5.
    Professional dave  = new Professional("Dave",  "Acme", 3, new HashSet<>());
    Professional eve   = new Professional("Eve",   "Acme", 2, new HashSet<>());
    Professional bob   = new Professional("Bob",   "Acme", 5, new HashSet<>(Arrays.asList(dave)));
    Professional carol = new Professional("Carol", "Acme", 4, new HashSet<>(Arrays.asList(dave, eve)));
    Professional alice = new Professional("Alice", "Acme", 6,
        new HashSet<>(Arrays.asList(bob, carol)));
    assertEquals(5, Practice.networkSize(alice));
  }

  @Test
  public void testNetworkSize_SelfLoop() {
    // A professional connected to themselves; should still count as 1.
    Professional alice = new Professional("Alice", "Acme", 5, new HashSet<>());
    alice.getConnections().add(alice);
    assertEquals(1, Practice.networkSize(alice));
  }
}
