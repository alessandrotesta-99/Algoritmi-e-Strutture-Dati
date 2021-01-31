/**
 * 
 */
package it.unicam.cs.asdl2021.mp1;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the Java SE Double-ended Queue (Deque) interface
 * (<code>java.util.Deque</code>) based on a double linked list. This deque does
 * not have capacity restrictions, i.e., it is always possible to insert new
 * elements and the number of elements is unbound. Duplicated elements are
 * permitted while <code>null</code> elements are not permitted. Being
 * <code>Deque</code> a sub-interface of
 * <code>Queue<code>, this class can be used also as an implementaion of a <code>Queue</code>
 * and of a <code>Stack</code>.
 * 
 * The following operations are not supported:
 * <ul>
 * <li><code>public <T> T[] toArray(T[] a)</code></li>
 * <li><code>public boolean removeAll(Collection<?> c)</code></li>
 * <li><code>public boolean retainAll(Collection<?> c)</code></li>
 * <li><code>public boolean removeFirstOccurrence(Object o)</code></li>
 * <li><code>public boolean removeLastOccurrence(Object o)</code></li>
 * </ul>
 * 
 * @author Template: Luca Tesei,
 * Implementation: ALESSANDRO TESTA - alessandro.testa@studenti.unicam.it
 *
 */
public class ASDL2021Deque<E> implements Deque<E> {

    /*
     * Current number of elements in this deque
     */
    private int size;

    /*
     * Pointer to the first element of the double-linked list used to implement
     * this deque
     */
    private Node<E> first;

    /*
     * Pointer to the last element of the double-linked list used to implement
     * this deque
     */
    private Node<E> last;

    /*
     * Number of changes in the list.
     */
    private int actualChanges;

    /**
     * Constructs an empty deque.
     */
    public ASDL2021Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
        this.actualChanges = 0;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0)
            return true;
        else
            return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int counter = 0;
        for(Node n = first; n != null; n = n.next){
            array[counter++] = n.item;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException(
                "This class does not implement this service.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean flag = false;
        //scorro tutti gli elementi della collezione e controllo se ogni elemento è contenuto in questa coda.
        for(Object o : c) {
            if (o == null)
                throw new NullPointerException();
            else
                if(this.contains(o))
                    flag = true;
                else
                    flag = false;
        }
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        //ogni elemento della collezione passata, se non è nullo, lo inserisco in fondo a questa coda.
        for (E o : c) {
            if(o == null)
                throw new NullPointerException("the element is null. this list doesn't permit null element.");
            this.addLast(o);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(
                "This class does not implement this service.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(
                "This class does not implement this service.");
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
        actualChanges++;
    }

    @Override
    public void addFirst(E e) {
        if(e == null)
            throw new NullPointerException("the element is null.");
        //creo un nuovo nodo
        Node n = new Node(null, e,null);
        //caso 1 - la coda è vuota. inserisco il nuovo nodo nella testa
        if(first == null){
            first = last = n;
        }
        //caso 2 - la coda non è vuota. inserisco il nuovo nodo nella testa.
        else {
            n.next = first;
            first.prev = n;
            first = n;
        }
        size++;
        actualChanges++;
    }

    @Override
    public void addLast(E e) {
       if(e == null)
           throw new NullPointerException("this element is null.");
       Node n = new Node(null, e,null);
       //caso 1 - la coda è vuota. inserisco il primo elemento.
        if(this.isEmpty())
            first = last = n;
        //caso 2 - la coda non è vuota. inserisco l'elemento in fondo.
        else{
            last.next = n;
            n.prev = last;
            last = n;
       }
       size++;
       actualChanges++;
    }

    @Override
    public boolean offerFirst(E e) {
        this.addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        this.addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        //se la coda è vuota lancio l'eccezione
        if(this.isEmpty())
            throw new NoSuchElementException("the deque is empty.");
        //elemento da recuperare.
        E item = first.item;
        //caso 1 - la coda ha un solo elemento. elimino quell'elemento.
        if(first == last) {
            first = last = null;
        }
        //caso 2 - la coda ha piu di un elemento. elimino il primo.
        else{
            first = first.next;
            first.prev = null;
        }
        size--;
        actualChanges++;
        return item;
    }

    @Override
    public E removeLast() {
        if(this.isEmpty())
            throw new NoSuchElementException("the deque is empty");
        //ultimo elemento da recuperare.
        E lastItem = last.item;
        //primo elemento da recuperare nel caso 1.
        E firstItem = first.item;
       //caso 1 - la lista ha un solo elemento che è sia la testa che la coda.
        if(first.next == null){
            first = last;
            first = last = null;
            size--;
            actualChanges++;
            return firstItem;
        }
        //caso 2 - la lista ha piu di un elemento. elimino l'ultimo elemento.
        else {
            last = last.prev;
            last.next = null;
            size--;
            actualChanges++;
            return lastItem;
        }
    }

    @Override
    public E pollFirst() {
        if(this.isEmpty())
            return null;
        //elemento da recuperare.
        E item = first.item;
        //caso 1 - la coda ha un solo elemento. elimino quell'elemento.
        if(first == last) {
            first = last = null;
        }
        //caso 2 - la coda ha piu di un elemento. elimino il primo.
        else{
            first = first.next;
            first.prev = null;
        }
        size--;
        actualChanges++;
        return item;
    }

    @Override
    public E pollLast() {
        if(this.isEmpty())
            return null;
        //ultimo elemento da recuperare.
        E lastItem = last.item;
        //primo elemento da recuperare.
        E firstItem = first.item;
        //caso 1 - la lista ha un solo elemento che è sia la testa che la coda.
        if(first.next == null){
            first = last;
            first = last = null;
            size--;
            actualChanges++;
            return firstItem;
        }
        //caso 2 - la lista ha piu di un elemento. elimino l'ultimo elemento.
        else {
            last = last.prev;
            last.next = null;
            size--;
            actualChanges++;
            return lastItem;
        }
    }

    @Override
    public E getFirst() {
        if(this.isEmpty())
            throw new NoSuchElementException("this list is empty.");
        return this.first.item;
    }

    @Override
    public E getLast() {
        if(this.isEmpty())
            throw new NoSuchElementException("this list is empty.");
        return this.last.item;
    }

    @Override
    public E peekFirst() {
        if(this.isEmpty())
            return null;
        else
            return this.first.item;
    }

    @Override
    public E peekLast() {
       if(this.isEmpty())
           return null;
       else
           return this.last.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException(
                "This class does not implement this service.");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException(
                "This class does not implement this service.");
    }

    @Override
    public boolean add(E e) {
       this.addLast(e);
       return true;
    }

    @Override
    public boolean offer(E e) {
        return this.offerLast(e);
    }

    @Override
    public E remove() {
       return this.removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return this.getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        this.addFirst(e);
    }

    @Override
    public E pop() {
       return this.removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        if(o == null)
            throw new NullPointerException("the element is null");
        boolean removed = false;
        Node pointer = first;
        //caso 0 - la lista è vuota.
        if(this.isEmpty())
            return false;
        //caso 1 - la lista ha un solo elemento.
        if(size == 1) {
            if ((this.contains(o) && removed) == false) {
                first = last = null;
                removed = true;
            }
        }
        //caso 2 - l'elemento da eliminare è il primo
        if(this.contains(o) && removed == false){
            if(first.item.equals(o)){
                first = first.next;
                first.prev = null;
                removed = true;
            } else
                removed = false;
        }
        //caso 3 - l'elemento da eliminare è l'ultimo.
        if(this.contains(o) && removed == false) {
            if (last.item.equals(o)) {
                last = last.prev;
                last.next = null;
                removed = true;
            }
            else
                removed = false;
        }
        //caso 4 - l'elemento da eliminare è in mezzo
        while (pointer.next != null  && removed == false){
            pointer = pointer.next;
            if(this.contains(o)){
                if(pointer.item.equals(o)){
                    Node nodoPrima = pointer.prev;
                    Node nodoDopo = pointer.next;
                    pointer.prev.next = null;
                    nodoPrima.next = nodoDopo;
                    removed = true;
                }             
            }
        }
        if(removed){
            size--;
            actualChanges++;
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean contains(Object o) {
        if(o == null)
           throw new NullPointerException("the element is null.");
        //salvo il primo nodo.
        Node firstNode = this.first;
        //scorro tutta la coda e controllo se ci sono item che corrispondono.
       while(firstNode != null)
           if(o.equals(firstNode.item))
               return true;
           else
               firstNode = firstNode.next;
           return false;
    }

    @Override
    public int size() {
        return size;
    }

    /*
     * Class for representing the nodes of the double-linked list used to
     * implement this deque. The class and its members/methods are protected
     * instead of private only for JUnit testing purposes.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        protected Node<E> prev;

        protected Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /*
     * Class for implementing an iterator for this deque. The iterator is
     * fail-safe: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class Itr implements Iterator<E> {

        //cursore
        private Node<E> currentNode;
        //modifiche attese
        private int expectedChanges;

        Itr() {
            currentNode = null;
            expectedChanges = ASDL2021Deque.this.actualChanges;
        }

        public boolean hasNext() {
            //se il cursore è nullo controllo se la coda è vuota o no
            if(currentNode == null){
                if(ASDL2021Deque.this.isEmpty())
                    return false;
                else
                    return true;
            }
            //se il prossimo nodo è nullo non ci sono piu elementi.
            if(currentNode.next == null)
                return false;
            else
                return true;
        }

        public E next() {
            //se le modifiche attuali sono maggiori di quelle attese lancio un eccezione
            if(actualChanges > expectedChanges)
                throw new ConcurrentModificationException("the changes don't match.");
            if(!this.hasNext())
                throw new NoSuchElementException("the list has no more elements");
          //se il cursore è nullo lo imposto al primo nodo della coda.
            if(currentNode == null)
                currentNode = first;
            //il cursore non è nullo e lo faccio andare avanti impostandolo al prossimo nodo
            else
                currentNode = currentNode.next;
            return currentNode.item;
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescItr();
    }

    /*
     * Class for implementing a descendign iterator for this deque. The iterator
     * is fail-safe: it detects if during the iteration a modification to the
     * original deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the
     * method <code>next()</code> is done.
     */
    private class DescItr implements Iterator<E> {

        //cursore
        private Node<E> currentNode;
        //modifiche attese
        private int expectedChanges;

        DescItr() {
            this.currentNode = null;
            this.expectedChanges = ASDL2021Deque.this.actualChanges;
        }

        public boolean hasNext() {
            //se il cursore è nullo controllo se la coda è vuota o no
            if(currentNode == null){
                if(ASDL2021Deque.this.isEmpty())
                    return false;
                else
                    return true;
            }
            //se il nodo precedente è nullo non ci sono piu elementi.
            if(currentNode.prev == null)
                return false;
            else
                return true;
        }

        public E next() {
            //se le modifiche attuali sono maggiori di quelle attese lancio un eccezione
            if(actualChanges > expectedChanges)
                throw new ConcurrentModificationException("the changes don't match.");
            if(!this.hasNext())
                throw new NoSuchElementException("the list has no more elements");
            //se il cursore è nullo lo imposto all'ultimo nodo della coda.
            if(currentNode == null)
                currentNode = last;
            //il cursore non è nullo e lo faccio andare avanti impostandolo al nodo precedente.
            else
                currentNode = currentNode.prev;
            return currentNode.item;
        }

    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getFirstNode() {
        return this.first;
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getLastNode() {
        return this.last;
    }
}
