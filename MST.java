package com.example.mst;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MST {

    public static class Result {
        public final List<Edge> mstEdges;
        public final long totalCost;
        public final long operationsCount;
        public final double executionTimeMs;

        public Result(List<Edge> mstEdges, long totalCost, long operationsCount, double executionTimeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.operationsCount = operationsCount;
            this.executionTimeMs = executionTimeMs;
        }
    }

    // ---------------- PRIM'S ALGORITHM ----------------
    public static Result prim(Graph graph, String startNode) {
        long operations = 0;
        long startTime = System.nanoTime();

        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        List<Edge> mst = new ArrayList<>();
        long totalCost = 0;

        visited.add(startNode);
        minHeap.addAll(graph.getAdjacencyList().get(startNode));

        while (!minHeap.isEmpty() && mst.size() < graph.getVertexCount() - 1) {
            Edge edge = minHeap.poll();
            operations++;

            if (visited.contains(edge.getTo())) {
                continue;
            }

            visited.add(edge.getTo());
            mst.add(edge);
            totalCost += edge.getWeight();

            for (Edge next : graph.getAdjacencyList().get(edge.getTo())) {
                if (!visited.contains(next.getTo())) {
                    minHeap.add(next);
                    operations++;
                }
            }
        }

        double time = (System.nanoTime() - startTime) / 1e6;
        return new Result(mst, totalCost, operations, time);
    }

    // ---------------- KRUSKAL'S ALGORITHM ----------------
    public static Result kruskal(Graph graph) {
        long startTime = System.nanoTime();
        AtomicLong operations = new AtomicLong();

        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        sortedEdges.sort((a, b) -> {
            operations.incrementAndGet();
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        UnionFind uf = new UnionFind(graph.getVertices());
        List<Edge> mst = new ArrayList<>();
        long totalCost = 0;

        for (Edge edge : sortedEdges) {
            if (uf.union(edge.getFrom(), edge.getTo())) {
                mst.add(edge);
                totalCost += edge.getWeight();
                operations.incrementAndGet();
            }
        }

        double time = (System.nanoTime() - startTime) / 1e6;
        return new Result(mst, totalCost, operations.get(), time);
    }
}
