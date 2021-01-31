package it.unicam.cs.asdl2021.mp2;

/**
 *
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 *
 * L'algoritmo usa una coda di min priorità tra i nodi implementata dalla classe
 * TernaryHeapMinPriorityQueue. I nodi vengono visti come PriorityQueueElement
 * poiché la classe GraphNode<L> implementa questa interfaccia. Si noti che
 * nell'esecuzione dell'algoritmo è necessario utilizzare l'operazione di
 * decreasePriority.
 *
 * @author Template: Luca Tesei, Implementation: Alessandro Testa - alessandro.testa@studenti.unicam.it
 *
 * @param <L>
 *                etichette dei nodi del grafo
 *
 */
public class PrimMSP<L> {

    /*
     * Coda di priorità che va usata dall'algoritmo. La variabile istanza è
     * protected solo per scopi di testing JUnit.
     */
    protected TernaryHeapMinPriorityQueue queue;

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        this.queue = new TernaryHeapMinPriorityQueue();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non negativi.
     * Dopo l'esecuzione del metodo nei nodi del grafo il campo previous deve
     * contenere un puntatore a un nodo in accordo all'albero di copertura
     * minimo calcolato, la cui radice è il nodo sorgente passato.
     *
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     *
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */

    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        //variabile inizializzata a infinito.
        double INF = Double.MAX_VALUE;
        double pesoArco;
        if (g == null || s == null)
            throw new NullPointerException("grafo o nodo nullo");
        if (!g.getNodes().contains(s))
            throw new IllegalArgumentException("nodo non contenuto");
        if (g.isDirected()) {
            for (GraphEdge<L> e : g.getEdges())
                if (!e.hasWeight() || e.getWeight() < 0)
                    throw new IllegalArgumentException("not ok");
        }
        //inizialmente per ogni nodo setto la priorità a infinito e il precedente a null.
        for (GraphNode<L> v : g.getNodes()) {
            v.setPriority(INF);
            v.setPrevious(null);
        }
        //setto la priorità del nodo di partenza a 0 e sicuramente non avrà nodi prima.
        s.setPriority(0);
        s.setPrevious(null);
        //inserisco i nodi in una coda di priorità.
        //inizialmente i nodi sono tutti bianchi.
        for(GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            queue.insert(node);
        }
        //fino a che ci sono elementi nella coda di priorità
        while(!queue.getTernaryHeap().isEmpty()){
            //parto da primo nodo.
            s = (GraphNode<L>) queue.extractMinimum();
            //prendo tutti i nodi adiacenti a lui
            for(GraphNode<L> v : g.getAdjacentNodesOf(s)){
                //controllo che il nodo non sia gia stato visitato.
                if(v.getColor() != GraphNode.COLOR_BLACK) {
                    //se la distanza tra il nodo iterato e il nodo adiacente iterato in questo
                    //momento, è minore della priorità del nodo adiacente, al nodo adiacente
                    //setto la priorità uguale alla distanza tra i due (peso dell'arco)

                    //prendo ogni arco del nodo estratto dalla coda
                    for (GraphEdge<L> e : g.getEdgesOf(s)) {
                        pesoArco = e.getWeight();
                        //se il nodo adiacente iterato in questo momento corrisponde
                        //al nodo sorgente o destinazione di un certo arco
                        //del nodo estratto dalla coda, controllo se il peso di questo arco che si sta iterando
                        //è minore della priorità del nodo adiacente (iterato in questo momento) del nodo estratto
                        //dalla coda.
                        //se si, il nodo estratto dalla coda di priorità diventa il nodo precedente al nodo adiacente
                        //iterato in questo momento
                        //infine setto la priorità del nodo adiacente iterato uguale al peso dell'arco iterato.
                        if(v == e.getNode2() || v == e.getNode1()){
                            if (pesoArco < v.getPriority()) {
                                    v.setPrevious(s);
                                    queue.decreasePriority(v, pesoArco);
                            }
                        }
                    }
                }
                else
                    continue;
                s.setColor(GraphNode.COLOR_BLACK);
            }
        }
    }
}
