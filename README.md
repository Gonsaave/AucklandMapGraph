AucklandMapGraph
================

Java - Node graph of Auckland Road Map

Program creates a graph structure of the greater Auckland regions road network, where nodes are identified as intersections.
User can select intersections to display information, zoom and pan.
I have implemented an articulation points algorithm, that identifies all intersections in the graph, that if removed, would disconnect part of the graph. Articulation points are highlighted as red on the map, after the algorithm has run.
I have implemented the graph search algorithm - 'a star' , that uses a distance heuristic to calculate the shortest path between two given nodes. The path is highlighted as red on the graph, and the path information (contained road info) is displayed, after the algorithm has run.
