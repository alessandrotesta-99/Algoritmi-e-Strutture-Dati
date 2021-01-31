/**
 * 
 */
package it.unicam.cs.asdl2021.mp2;

import java.util.*;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * orientato. Non sono accettate etichette dei nodi null e non sono accettate
 * etichette duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * Per la rappresentazione viene usata una variante della rappresentazione con
 * liste di adiacenza. A differenza della rappresentazione standard si usano
 * strutture dati più efficienti per quanto riguarda la complessità in tempo
 * della ricerca se un nodo è presente (pseudocostante, con tabella hash) e se
 * un arco è presente (pseudocostante, con tabella hash). Lo spazio occupato per
 * la rappresentazione risultà tuttavia più grande di quello che servirebbe con
 * la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è l'insieme dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi uscenti dal nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presenta basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementation: Alessandro Testa - alessandro.testa@studenti.unicam.it
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListDirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto. La variabile
     * istanza è protected solo per scopi di test JUnit.
     */
    protected final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /*
     * NOTA: per tutti i metodi che ritornano un set utilizzare la classe
     * HashSet<E> per creare l'insieme risultato. Questo garantisce un buon
     * funzionamento dei test JUnit che controllano l'uguaglianza tra insiemi
     */
    
    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListDirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
      return this.adjacentLists.keySet().size();
    }

    @Override
    public int edgeCount() {
        int edgeCount = 0;
        //mi salvo tutti gli archi del grafo in una lista.
        Collection<Set<GraphEdge<L>>> values =  this.adjacentLists.values();
        //inserisco nella variabile "edgeCount" creata sopra, la dimensione di tutti gli archi dei nodi.
        for (Set<GraphEdge<L>> value : values)
            edgeCount += value.size();
       return edgeCount;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi orientati
        return true;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        boolean addNode;
        if(node == null)
            throw new NullPointerException("nodo nullo");
        if(adjacentLists.containsKey(node))
            addNode = false;
        else{
            adjacentLists.put(node, new HashSet<>());
            addNode = true;
        }
        return addNode;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("nodo nullo");
        for (GraphNode<L> n : adjacentLists.keySet())
            if(n.equals(node))
                return true;
            return false;
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if(label == null)
            throw new NullPointerException("etichetta nulla.");
        for (GraphNode<L> n : adjacentLists.keySet()){
            if(n.getLabel().equals(label))
                return n;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if(!this.containsNode(node))
            throw new IllegalArgumentException("nodo inesistente");
        if(node == null)
            throw new NullPointerException("nodo nullo");
        //creo una lista dove inserire tutti i nodi adiacenti a quello passato.
        Set<GraphNode<L>> allAdjacentNode = new HashSet<>();
        //scorro tutti gli archi del nodo passato e inserisco il nodo destinazione di ogni arco nella
        //lista creata sopra.
        for(GraphEdge<L> edge : adjacentLists.get(node))
            allAdjacentNode.add(edge.getNode2());
        return allAdjacentNode;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        if(!this.isDirected())
            throw new UnsupportedOperationException("grafo non orientato");
        if(!this.containsNode(node))
            throw new IllegalArgumentException("nodo inesistente");
        if(node == null)
            throw new NullPointerException("nodo nullo");
        //creo una lista dove inserire tutti i nodi predecessori a quello passato.
        Set<GraphNode<L>> allPredecessorNode = new HashSet<>();
        //scorro tutti gli archi entranti del nodo passato, e per ognuno inserisco il nodo sorgente nella
        //lista creata sopra.
        for(GraphEdge<L> edge : this.getIngoingEdgesOf(node))
            allPredecessorNode.add(edge.getNode1());
        return allPredecessorNode;
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
       Set<GraphEdge<L>> allEdges = new HashSet<>();
       //per ogni nodo all'interno della lista di adiacenza,
        //aggiungo il proprio valore (archi) alla lista che viene ritornata
        for (GraphNode<L> n : this.adjacentLists.keySet())
            allEdges.addAll(adjacentLists.get(n));
        return allEdges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        boolean addEdge;
        if(edge == null)
            throw new NullPointerException("arco nullo");
        if(!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException("uno dei due o entrambi i nodi non esistono.");
        if(!edge.isDirected())
            throw new IllegalArgumentException("arco non orientato");
        if(this.containsEdge(edge))
            addEdge = false;
        //se l'arco non è contenuto nel grafo, prendo il nodo sorgente dell'arco passato e gli aggiungo
        //l'arco stesso.
        else{
            adjacentLists.get(edge.getNode1()).add(edge);
            addEdge = true;
        }
        return addEdge;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("arco nullo");
        if(!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException("nodi nulli");
        return adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if(!this.containsNode(node))
            throw new IllegalArgumentException("nodo inesistente");
        if(node == null)
            throw new NullPointerException("nodo nullo");
        return adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        if(!this.isDirected())
            throw new UnsupportedOperationException("grafo non orientato");
        if(node == null)
            throw new NullPointerException("nodo nullo");
        if(!this.containsNode(node))
            throw new IllegalArgumentException("nodo inesistente");
        //creo una lista dove inserire tutti gli archi entranti al nodo passato.
        Set<GraphEdge<L>> allIngoingEdges = new HashSet<>();
        //scorro tutti gli archi del grafo e controllo che il nodo passato è il nodo destinazione dell'arco.
        //se si inserisco l'arco nella lista creata sopra.
        for(GraphEdge<L> e : this.getEdges()){
            if(e.getNode2().equals(node))
                allIngoingEdges.add(e);
        }
        return allIngoingEdges;
    }

}
