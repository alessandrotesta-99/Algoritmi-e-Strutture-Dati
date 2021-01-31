/**
 * 
 */
package it.unicam.cs.asdl2021.mp2;

import java.util.*;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Template: Luca Tesei, Implementation: Alessandro Testa - alessandro.testa@studenti.unicam.it
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    // Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
    // matrice di adiacenza
    protected Map<GraphNode<L>, Integer> nodesIndex;

    // Matrice di adiacenza, gli elementi sono null o oggetti della classe
    // GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
    // dimensione gradualmente ad ogni inserimento di un nuovo nodo.
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /*
     * NOTA: per tutti i metodi che ritornano un set utilizzare la classe
     * HashSet<E> per creare l'insieme risultato. Questo garantisce un buon
     * funzionamento dei test JUnit che controllano l'uguaglianza tra insiemi
     */
    
    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.keySet().size();
    }

    @Override
    public int edgeCount() {
        //creo una lista dove verranno salvati tutti gli archi che sono contenuti nel grafo.
        ArrayList<GraphEdge<L>> i = new ArrayList<>();
        for(ArrayList<GraphEdge<L>> n : matrix){
            for (GraphEdge<L> ed : n) {
                if(ed != null && !i.contains(ed))
                    i.add(ed);
            }
        }
        //ritorno la lunghezza della lista dove ho salvato tutti gli archi di questo grafo
        return i.size();
    }

    @Override
    public void clear() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa un grafo non orientato
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        boolean addNode;
        if(node == null)
            throw new NullPointerException("Nodo nullo");
        if(nodesIndex.containsKey(node))
            addNode = false;
        else {
            //se il nodo non è contenuto nel grafo, aggiungo il nodo nella prima posizione libera e
            //di conseguenza aggiungo una ArrayList contenente tutti gli archi del nodo aggiunto.
            //inizialmente gli archi sono nulli. L'arrayList creata viene aggiunta alla matrice nella
            //stessa posizione dove è stato inserito il nodo.
            nodesIndex.put(node, nodeCount());
            matrix.add(nodesIndex.get(node), new ArrayList<>());
            addNode = true;
            for(ArrayList<GraphEdge<L>> nodo : matrix){
                for (int i = 0; i < nodeCount(); i++) {
                    if(nodo.size() != nodesIndex.size())
                        nodo.add(null);
                }
            }
        }
        return addNode;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Remove di nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("nodo nullo");
        for(GraphNode<L> n : nodesIndex.keySet())
            if(n.equals(node))
                return true;
        return false;
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if(label == null)
            throw new NullPointerException("etichetta nulla");
        for(GraphNode<L> n : nodesIndex.keySet()){
            if(n.getLabel().equals(label))
                return n;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        int index = 0;
        if(label == null)
            throw new NullPointerException("etichetta null");
        if(this.getNodeOf(label) == null)
            throw new IllegalArgumentException("nodo inesistente");
        for(GraphNode<L> n : nodesIndex.keySet())
            if(n.getLabel().equals(label))
               index = nodesIndex.get(n);
        return index;
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        if(i > this.nodeCount() - 1 && i < 0 || !nodesIndex.containsValue(i))
            throw new IndexOutOfBoundsException("not ok");
        for(GraphNode<L> n : nodesIndex.keySet()) {
            if (nodesIndex.get(n).equals(i))
                return n;
        }
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("nodo nullo");
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Nodo inesistente");
        //creo una lista dove inserire tutti i nodi adiacenti a quello passato.
        Set<GraphNode<L>> allAdjacentNode = new HashSet<>();
        //per ogni arco di tutti gli archi del nodo passato.
        for(GraphEdge<L> edge : this.getEdgesOf(node)) {
            //se il nodo passato è il nodo sorgente, nella lista inserirò il nodo di destinazione
            if (edge.getNode1().equals(node))
                allAdjacentNode.add(edge.getNode2());
            //altrimenti, viceversa.
            else
                allAdjacentNode.add(edge.getNode1());
        }
        return allAdjacentNode;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        //lista dove aggiungere gli archi
        Set<GraphEdge<L>> allEdges = new HashSet<>();
        //per ogni ArrayList all'interno della matrice,
        //aggiungo gli archi (non nulli) di ognuna alla lista "allEdges" che ho creato.
        for(ArrayList<GraphEdge<L>> n : matrix){
            for (GraphEdge<L> ed : n) {
                if(ed != null)
                    allEdges.add(ed);
            }
        }
        return allEdges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("nodo nullo");
        if(!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException("not ok");
        if(edge.isDirected())
            throw new IllegalArgumentException("arco orientato");
        if(this.containsEdge(edge))
                return false;
        //se l'arco non è contenuto in questo grafo.
        else{
            //salvo l'indice del primo nodo dell'arco
            int indexOfNode1 = nodesIndex.get(edge.getNode1());
            //salvo l'indice del secondo nodo dell'arco
            int indexOfNode2 = nodesIndex.get(edge.getNode2());
            //scorro tutta la matrice e quando l'indice del nodo dell'arco
            //sarà uguale ad un indice dell'elemento della
            //matrice, inserisco in quella posizione l'arco.
            for(int i = 0; i < matrix.size(); i ++){
                if(indexOfNode1 == i)
                    //inserisce l'arco nella posizione dell'array equivalente alla posizione del nodo 1
                    matrix.get(i).set(indexOfNode2, edge);
                if (indexOfNode2 == i && !(indexOfNode1 == i))
                    //inserisce l'arco nella posizione dell'array equivalente alla posizione del nodo 2
                    matrix.get(i).set(indexOfNode1, edge);
            }
        }
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Operazione di remove non supportata in questa classe");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("arco nullo");
        if(!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("nodi nulli");
        return this.getEdges().contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if(node == null)
            throw new NullPointerException("nodo nullo");
        if(!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("nodo inesistente");
        //creo una lista dove inserire tutti gli archi del nodo passato.
        Set<GraphEdge<L>> edgesOfNode = new HashSet<>();
        //contatore.
        int count = 0;
        //fino a che ci sono archi nel grafo, per ogni arco controllo se il nodo sorgente o di destinazione
        //corrisponde al nodo passato. Se si inserisco questo arco nella lista creata sopra e vado avanti.
        while (count != edgeCount()){
            for(GraphEdge<L> e : this.getEdges()){
                if((e.getNode1().equals(node) || e.getNode2().equals(node)))
                    edgesOfNode.add(e);
            }
            count++;
        }
        return edgesOfNode;
      }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

}
