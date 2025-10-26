package com.example.mst;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MSTTest {

    private Graph buildSampleGraph() {
        Graph g = new Graph();
        g.addEdge("A", "B", 4);
        g.addEdge("A", "C", 3);
        g.addEdge("B", "C", 2);
        g.addEdge("B", "D", 5);
        g.addEdge("C", "D", 7);
        g.addEdge("C", "E", 8);
        g.addEdge("D", "E", 6);
        return g;
    }

    @Test
    void testMSTCorrectness() {
        Graph graph = buildSampleGraph();

        MST.Result prim = MST.prim(graph, "A");
        MST.Result kruskal = MST.kruskal(graph);

        assertEquals(prim.totalCost, kruskal.totalCost, "MST costs must match");
        assertEquals(graph.getVertexCount() - 1, prim.mstEdges.size(), "Prim MST must have V-1 edges");
        assertEquals(graph.getVertexCount() - 1, kruskal.mstEdges.size(), "Kruskal MST must have V-1 edges");
    }

    @Test
    void testNoCycles() {
        Graph graph = buildSampleGraph();
        MST.Result prim = MST.prim(graph, "A");
        MST.Result kruskal = MST.kruskal(graph);

        UnionFind uf1 = new UnionFind(graph.getVertices());
        for (Edge e : prim.mstEdges) {
            assertTrue(uf1.union(e.getFrom(), e.getTo()), "Prim MST must not contain cycles");
        }

        UnionFind uf2 = new UnionFind(graph.getVertices());
        for (Edge e : kruskal.mstEdges) {
            assertTrue(uf2.union(e.getFrom(), e.getTo()), "Kruskal MST must not contain cycles");
        }
    }

    @Test
    void testDisconnectedGraph() {
        Graph graph = new Graph();
        graph.addVertex("X");
        graph.addVertex("Y");

        MST.Result prim = MST.prim(graph, "X");
        MST.Result kruskal = MST.kruskal(graph);

        assertEquals(0, prim.mstEdges.size(), "Prim MST should be empty for disconnected graphs");
        assertEquals(0, kruskal.mstEdges.size(), "Kruskal MST should be empty for disconnected graphs");
    }

    @Test
    void testPerformanceValues() {
        Graph graph = buildSampleGraph();
        MST.Result prim = MST.prim(graph, "A");
        MST.Result kruskal = MST.kruskal(graph);

        assertTrue(prim.operationsCount >= 0);
        assertTrue(kruskal.operationsCount >= 0);
        assertTrue(prim.executionTimeMs >= 0);
        assertTrue(kruskal.executionTimeMs >= 0);
    }
}
