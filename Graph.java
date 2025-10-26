package com.example.mst;

import java.util.*;

public class Graph {
    private final Set<String> vertices = new LinkedHashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addVertex(String v) {
        vertices.add(v);
        adjacencyList.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        addVertex(from);
        addVertex(to);
        Edge e = new Edge(from, to, weight);
        edges.add(e);
        adjacencyList.get(from).add(e);
        adjacencyList.get(to).add(new Edge(to, from, weight)); // Undirected graph
    }

    public Set<String> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return Collections.unmodifiableMap(adjacencyList);
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }
}
