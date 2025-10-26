package com.example.mst;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String inputFile = "assign_3_input.json";
        String outputFile = "output.json";
        String csvFile = "results.csv";

        JsonObject finalOutput = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        // Write CSV header
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile))) {
            csvWriter.println("Graph ID,V,E,Algorithm,Cost,Operations,Time (ms)");

            try (FileReader reader = new FileReader(inputFile)) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray graphs = jsonObject.getAsJsonArray("graphs");

                for (JsonElement graphElement : graphs) {
                    JsonObject graphJson = graphElement.getAsJsonObject();
                    Graph graph = new Graph();

                    // Load nodes
                    JsonArray nodes = graphJson.getAsJsonArray("nodes");
                    for (JsonElement node : nodes) {
                        graph.addVertex(node.getAsString());
                    }

                    // Load edges
                    JsonArray edges = graphJson.getAsJsonArray("edges");
                    for (JsonElement edge : edges) {
                        JsonObject e = edge.getAsJsonObject();
                        graph.addEdge(
                                e.get("from").getAsString(),
                                e.get("to").getAsString(),
                                e.get("weight").getAsInt()
                        );
                    }

                    // Run Prim
                    MST.Result primResult = MST.prim(graph, nodes.get(0).getAsString());

                    // Run Kruskal
                    MST.Result kruskalResult = MST.kruskal(graph);

                    // Build JSON output
                    JsonObject graphResult = new JsonObject();
                    graphResult.addProperty("graph_id", graphJson.get("id").getAsInt());

                    JsonObject stats = new JsonObject();
                    stats.addProperty("vertices", graph.getVertexCount());
                    stats.addProperty("edges", graph.getEdgeCount());
                    graphResult.add("input_stats", stats);

                    // Prim JSON
                    JsonObject primJson = new JsonObject();
                    primJson.addProperty("total_cost", primResult.totalCost);
                    primJson.addProperty("operations_count", primResult.operationsCount);
                    primJson.addProperty("execution_time_ms", primResult.executionTimeMs);
                    primJson.add("mst_edges", edgeListJson(primResult.mstEdges));
                    graphResult.add("prim", primJson);

                    // Kruskal JSON
                    JsonObject kruskalJson = new JsonObject();
                    kruskalJson.addProperty("total_cost", kruskalResult.totalCost);
                    kruskalJson.addProperty("operations_count", kruskalResult.operationsCount);
                    kruskalJson.addProperty("execution_time_ms", kruskalResult.executionTimeMs);
                    kruskalJson.add("mst_edges", edgeListJson(kruskalResult.mstEdges));
                    graphResult.add("kruskal", kruskalJson);

                    resultsArray.add(graphResult);

                    // Write CSV rows
                    int graphId = graphJson.get("id").getAsInt();
                    int v = graph.getVertexCount();
                    int eCount = graph.getEdgeCount();
                    csvWriter.printf("%d,%d,%d,Prim,%d,%d,%.3f%n",
                            graphId, v, eCount, primResult.totalCost, primResult.operationsCount, primResult.executionTimeMs);
                    csvWriter.printf("%d,%d,%d,Kruskal,%d,%d,%.3f%n",
                            graphId, v, eCount, kruskalResult.totalCost, kruskalResult.operationsCount, kruskalResult.executionTimeMs);
                }

                finalOutput.add("results", resultsArray);

                // Write output.json
                try (FileWriter writer = new FileWriter(outputFile)) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(finalOutput, writer);
                    System.out.println("✅ JSON result saved to " + outputFile);
                }

                System.out.println("✅ CSV summary saved to " + csvFile);

            } catch (IOException e) {
                System.err.println("❌ Error reading input.json: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("❌ Error writing results.csv: " + e.getMessage());
        }
    }

    private static JsonArray edgeListJson(List<Edge> mstEdges) {
        JsonArray array = new JsonArray();
        for (Edge e : mstEdges) {
            JsonObject obj = new JsonObject();
            obj.addProperty("from", e.getFrom());
            obj.addProperty("to", e.getTo());
            obj.addProperty("weight", e.getWeight());
            array.add(obj);
        }
        return array;
    }
}
