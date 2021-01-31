package it.unicam.cs.asdl2021.mp1;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that provides an implementation of a "dynamic" min-priority queue based
 * on a ternary heap. "Dynamic" means that the priority of an element already
 * present in the queue may be decreased, so possibly this element may become
 * the new minumum element. The elements that can be inserted may be of any
 * class implementing the interface <code>PriorityQueueElement</code>. This
 * min-priority queue does not have capacity restrictions, i.e., it is always
 * possible to insert new elements and the number of elements is unbound.
 * Duplicated elements are permitted while <code>null</code> elements are not
 * permitted.
 * 
 * @author Template: Luca Tesei,
 * Implementation: ALESSANDRO TESTA - alessandro.testa@studenti.unicam.it
 *
 */
public class TernaryHeapMinPriorityQueue {

    /*
     * ArrayList for representing the ternary heap. Use all positions, including
     * position 0 (the JUnit tests will assume so). You have to adapt
     * child/parent indexing formulas consequently.
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Create an empty queue.
     */
    public TernaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Return the current size of this queue.
     * 
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the ternary heap. The handle of the element will also be set
     * accordingly.
     * 
     * @param element
     *                    the new element to add
     * @throws NullPointerException
     *                                  if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        if (element == null)
            throw new NullPointerException("the element is null");

        //caso 1 - lo heap è vuoto. aggiungo l'elemento e setto la handle nella posizione 0.
        if (this.heap.isEmpty()) {
            this.heap.add(element);
            element.setHandle(0);
        }
        //caso 2 - lo heap ha gia elementi. inserisco l'elemento in fondo e controllo se deve essere scambiato
        // in base alla priorità.
        else {
            this.heap.add(element);
            //setto la handle all'ultimo nodo dove è stato inserito l'elemento.
            element.setHandle(this.heap.size() - 1);
            //salvo il padre
            PriorityQueueElement parent = this.heap.get(parent(element.getHandle()));
            //salvo il nodo inserito ovvero il figlio
            PriorityQueueElement child = this.heap.get(element.getHandle());
            //scambia il padre con il figlio se la priorità del padre è maggiore di quella del figlio.
            while (element.getHandle() > 0 && parent.getPriority() > child.getPriority())
                swap(parent(element.getHandle()), element.getHandle());
        }
    }

    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the ternary heap.
     * 
     * @return the current minimum element of this min-priority queue
     * 
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        if(this.heap.isEmpty())
            throw new NoSuchElementException("this deque is empty");
        return heap.get(0);
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     * 
     * @return the current minimum element
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        if(heap.isEmpty())
            throw new NoSuchElementException("this deque is empty");
        //prendo il minimo
        PriorityQueueElement min = this.heap.get(0);
        //effettuo lo scambio
        swap(0, heap.size()-1);     
        //rimuovo il nodo da estrarre, ovvero la foglia piu piccola
        this.heap.remove(min);
        //controllo se lo heap è min-heap.
        this.minHeapify(0);
        return min;
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the ternary heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     * 
     * @param element
     *                        the element whose priority will be decreased, it
     *                        must currently be inside this min-priority queue
     * @param newPriority
     *                        the new priority to assign to the element
     * 
     * @throws NoSuchElementException
     *                                      if the element is not currently
     *                                      present in this min-priority queue
     * @throws IllegalArgumentException
     *                                      if the specified newPriority is not
     *                                      strictly less than the current
     *                                      priority of the element
     */
    public void decreasePriority(PriorityQueueElement element,
            double newPriority) {
        if(!heap.contains(element))
            throw new NoSuchElementException("the element doesn't exists");
        if(element.getPriority() < newPriority)
            throw new IllegalArgumentException("the new priority has incorrect.");
        //setto la priorità dell'elemento a quella nuova
        element.setPriority(newPriority);
        //salvo il padre dell'elemento
        PriorityQueueElement padre = this.heap.get(parent(element.getHandle()));
        //scambio il padre con il figlio se dopo aver settato la priorità bisogna rifare un min heap.
        while (element.getHandle() >= 0 && padre.getPriority() > element.getPriority()){
            swap(element.getHandle(), padre.getHandle());
            element = padre;
        }
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    /**
     * ritorna il genitore del nodo passato.
     * @param i indice del nodo
     * @return il padre del nodo
     */
    private int parent(int i){
        return (i - 1) / 3;
    }

    /**
     * ritorna il figlio sinistro del nodo passato.
     *
     * @param i indice del nodo
     * @return figlio sinistro
     */
    private int left(int i){
        return 3*i + 1;
    }

    /**
     * ritorna il figlio centrale del nodo passato.
     *
     * @param i indice del nodo
     * @return figlio centrale
     */
    private int center(int i){
        return 3*i + 2;
    }

    /**
     * ritorna il figlio destro del nodo passato.
     *
     * @param i indice del nodo
     * @return figlio destro
     */
    private int right(int i){
        return 3*i + 3;
    }

    /**
     * procedura per trasformare lo heap in min-heap.
     *
     * @param i indice del nodo
     */
    private void minHeapify(int i){
        int left = left(i);
        int center = center(i);
        int right = right(i);
        int smallest;
        //controllo se la priorità del filgio sinistro è minore o uguale alla priorità del padre.
        //se si mi salvo in una variabile l'indice del figlio.
        if(left <= heap.size() - 1 && this.heap.get(left).getPriority() <= this.heap.get(i).getPriority()) {
            smallest = left;
        }
        //se non lo è salvo in una variabile l'indice del padre.
        else
            smallest = i;
        //controllo se la priorità del figlio centrale è minore o uguale alla priorità del padre.
        //se si mi salvo in una variabile l'indice del figlio.
        if(center <= heap.size() - 1 && this.heap.get(center).getPriority() <= this.heap.get(smallest).getPriority())
            smallest = center;
        //controllo se la priorità del filgio destro è minore o uguale alla priorità del padre.
        //se si mi salvo in una variabile l'indice del figlio.
        if(right <= heap.size() - 1 && this.heap.get(right).getPriority() <= this.heap.get(smallest).getPriority())
            smallest = right;
        //se l'indice salvato non corrisponde a quello del padre devo scambiare il padre con il figlio.
        //rifaccio la procedura di minHeapify fino a che lo heap non è min heap.
        if(smallest != i){
            swap(i,smallest);
            minHeapify(smallest);
        }
    }

    private void swap(int a, int b){
        //salvo i nodi corrispondenti agli indici dati.
        PriorityQueueElement element1To2 = this.heap.get(a);
        PriorityQueueElement element2To1 = this.heap.get(b);
        //setto la posizione di un elemento nella posizione dell'altro.
        //setto la handle dell elemento spostato.
        this.heap.set(a, element2To1);
        element2To1.setHandle(a);
        this.heap.set(b, element1To2);
        element1To2.setHandle(b);
    }
    
    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }

}
