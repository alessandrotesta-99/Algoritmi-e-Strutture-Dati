package it.unicam.cs.asdl2021.mp2;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Class that solves an instance of the the El Mamun's Caravan problem using
 * dynamic programming.
 * 
 * Template: Daniele Marchei and Luca Tesei, Implementation: Alessandro Testa - alessandro.testa@studenti.unicam.it
 *
 */
public class ElMamunCaravanSolver {

    // the expression to analyse
    private final Expression expression;

    // table to collect the optimal solution for each sub-problem,
    // protected just for Junit Testing purposes
    protected Integer[][] table;

    // table to record the chosen optimal solution among the optimal solution of
    // the sub-problems, protected just for JUnit Testing purposes
    protected Integer[][] tracebackTable;

    // flag indicating that the problem has been solved at least once
    private boolean solved;

    /**
     * Create a solver for a specific expression.
     * 
     * @param expression
     *                       The expression to work on
     * @throws NullPointerException
     *                                  if the expression is null
     */
    public ElMamunCaravanSolver(Expression expression) {
        if (expression == null)
            throw new NullPointerException(
                    "Creazione di solver con expression null");
        this.expression = expression;
        this.table = new Integer[expression.size()][expression.size()];
        this.tracebackTable = new Integer[expression.size()][expression.size()];
        this.solved = false;
    }

    /**
     * Returns the expression that this solver analyse.
     * 
     * @return the expression of this solver
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     * Solve the problem on the expression of this solver by using a given
     * objective function.
     * 
     * @param function
     *                     The objective function to be used when deciding which
     *                     candidate to choose
     * @throws NullPointerException
     *                                  if the objective function is null
     */
    public void solve(ObjectiveFunction function) {
        if(function == null)
            throw new NullPointerException("function null");
        if(this.isSolved())
            return;
        //creo la diagonale iniziale.
        for(int n = 0; n < expression.size(); n++){
            //metto tutti i valori della diagonale a 0 inizialmente.
            table[n][n] = 0;
            //inizialmente non ci sono k, quindi i valori della diagonale sono null.
            tracebackTable[n][n] = null;
        }
        List<Integer> in = new ArrayList<>();
        //cicli di riempimento della matrice.
        for(int h = 1; h < expression.size(); h ++){
            for(int i = 0; i < expression.size() - h + 1; i++){
                //valore di j
                int j = i + h - 1;
                //se mi trovo nella diagonale principale, inserico i numeri dell'espressione.
                if(i == j && expression.get(i).getType() == ItemType.DIGIT)
                    table[i][j] = (Integer) expression.get(i).getValue();
                //se j è minore di i, riempio le altre diagonali
                else if(i < j && expression.get(i).getType() == ItemType.DIGIT
                        && expression.get(j).getType() == ItemType.DIGIT){
                    for(int k = i; i+k+2 <= j; k++){
                       //operatore che divide i due sottoproblemi (le sottoespressioni)
                       Object e = expression.get(i+k+1).getValue();     
                        //TODO - da continuare e correggere.                      
                          tracebackTable[i][j] = k;
                        }
                    }
                }
            }
        }
        solved = true;
    }

    /**
     * Returns the current optimal value for the expression of this solver. The
     * value corresponds to the one obtained after the last solving (which used
     * a particular objective function).
     * 
     * @return the current optimal value
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public int getOptimalSolution() {
        if(!isSolved())
            throw new IllegalStateException("problema non risolto.");
        return table[0][expression.size() - 1];
    }

    /**
     * Returns an optimal parenthesization corresponding to an optimal solution
     * of the expression of this solver. The parenthesization corresponds to the
     * optimal value obtained after the last solving (which used a particular
     * objective function).
     * 
     * If the expression is just a digit then the parenthesization is the
     * expression itself. If the expression is not just a digit then the
     * parethesization is of the form "(<parenthesization>)". Examples: "1",
     * "(1+2)", "(1*(2+(3*4)))"
     * 
     * @return the current optimal parenthesization for the expression of this
     *         solver
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public String getOptimalParenthesization() {
        if(!isSolved())
            throw new IllegalStateException("problema non risolto.");


        return null;
    }

    /**
     * Determines if the problem has been solved at least once.
     * 
     * @return true if the problem has been solved at least once, false
     *         otherwise.
     */
    public boolean isSolved() {
        return this.solved;
    }

    @Override
    public String toString() {
        return "ElMamunCaravanSolver for " + expression;
    }
}
