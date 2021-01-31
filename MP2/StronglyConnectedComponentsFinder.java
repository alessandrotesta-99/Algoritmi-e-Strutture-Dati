/**
 * 
 */
package it.unicam.cs.asdl2021.mp2;

import java.util.*;

/**
 * Un oggetto di questa classe singoletto è un attore che trova le componenti
 * fortemente connesse in un grafo orientato che viene passato come parametro.
 * 
 * @author Template: Luca Tesei, Implementation: Alessandro Testa - alessandro.testa@studenti.unicam.it
 *
 */
public class StronglyConnectedComponentsFinder<L> {

    /*
     * NOTA: per tutti i metodi che ritornano un set utilizzare la classe
     * HashSet<E> per creare l'insieme risultato. Questo garantisce un buon
     * funzionamento dei test JUnit che controllano l'uguaglianza tra insiemi
     */

    /**
     * Dato un grafo orientato determina l'insieme di tutte le componenti
     * fortemente connesse dello stesso.
     * 
     * @param g
     *              un grafo orientato
     * @return l'insieme di tutte le componenti fortemente connesse di g dove
     *         ogni componente fortemente connessa è rappresentata dall'insieme
     *         dei nodi che la compongono.
     * @throws IllegalArgumentException
     *                                      se il grafo passato non è orientato
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     */
    public Set<Set<GraphNode<L>>> findStronglyConnectedComponents(Graph<L> g) {
        if(!g.isDirected())
            throw new IllegalArgumentException("grafo non orientato");
        if(g == null)
            throw new NullPointerException("grafo nullo");
        //creo uno stack dove verranno inseriti i nodi visitati completamente.
        Stack<GraphNode<L>> stack = new Stack<>();
        //set di componenti fortemente connesse.
        Set<Set<GraphNode<L>>> getSCC = new HashSet<>();
        //effettuo una visita sul grafo passato.
        this.initDFS(g,stack);
        //creo il grafo trasposto.
        Graph<L> transpose = this.getTranspose(g);
        //i nodi del grafo trasposto non sono visitati. setto il colore a bianco.
        for(GraphNode<L> n : g.getNodes())
            n.setColor(GraphNode.COLOR_WHITE);
        while(stack.empty() == false){
            //prendo il primo nodo dallo stack.
            GraphNode<L> gn = stack.pop();
            //se il nodo non è stato visitato, effettuo una visita in profondità del grafo trasposto
            //partendo da questo nodo.
            if(gn.getColor() == GraphNode.COLOR_WHITE){
                Set<GraphNode<L>> comp = new HashSet<>();
                DFSvisit(transpose, transpose.getNodeOf(gn.getLabel()), comp);
                getSCC.add(comp);
            }
        }
        return getSCC;
    }

    private void fillOrder(Graph<L> g, GraphNode<L> u, Stack stack) {
        //il nodo è stato visitato completamente, quindi setto il suo colore a nero.
        u.setColor(GraphNode.COLOR_BLACK);
        //controllo tutti i suoi nodi adiacenti, se ce ne sono ancora non visitati,
        //effettuo la ricorsione di questo metodo.
        for (GraphNode<L> n : g.getAdjacentNodesOf(u)) {
            if (n.getColor() == GraphNode.COLOR_WHITE)
                fillOrder(g,n, stack);
        }
        //inserisco in uno stack i nodi che sono stati visitati completamente.
        stack.push(u);
    }

    private Graph<L> getTranspose(Graph<L> g){
        //creo un nuovo grafo trasposto
        Graph<L> transpose =  new MapAdjacentListDirectedGraph<>();
        //aggiungo al nuovo grafo i nodi del grafo passato.
        for (GraphNode<L> node : g.getNodes())
            transpose.addNode(node);
        //per ogni arco del grafo passato, ne creo uno nuovo con nodo sorgente e destinazione invertiti.
        //inserisco questo arco nel grafo creato sopra.
        for(GraphEdge<L> e : g.getEdges())
            transpose.addEdge(new GraphEdge<L>(e.getNode2(), e.getNode1(), true));
        return transpose;
    }

    private void DFSvisit(Graph<L> g, GraphNode<L> u, Set<GraphNode<L>> comp) {
        //inizialmente setto il colore del nodo da dove inizia la visita a grigio.
        u.setColor(GraphNode.COLOR_GREY);
        GraphNode<L> n;
        //continuo la visita scorrendo tutti i nodi adiacenti al grafo.
        //se un nodo adiacente non è ancora visitato (colore bianco) faccio la ricorsione di questo metodo.
        for (GraphNode<L> lGraphNode : g.getAdjacentNodesOf(u)) {
            n = lGraphNode;
            if (n.getColor() == GraphNode.COLOR_WHITE)
                DFSvisit(g, n, comp);
        }
        //aggiungo il nodo visitato ad una lista.
        comp.add(u);
    }

    private void initDFS(Graph<L> g, Stack stack){
        //inizialmente nessun nodo è visitato.
        for(GraphNode<L> n : g.getNodes())
            n.setColor(GraphNode.COLOR_WHITE);
        for(GraphNode<L> n : g.getNodes()) {
            if (n.getColor() == GraphNode.COLOR_WHITE)
                fillOrder(g,n,stack);
        }
    }
}
