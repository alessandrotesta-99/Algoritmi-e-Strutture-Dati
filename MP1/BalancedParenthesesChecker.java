package it.unicam.cs.asdl2021.mp1;


/**
 * An object of this class is an actor that uses an ASDL2021Deque<Character> as
 * a Stack in order to check that a sequence containing the following
 * characters: '(', ')', '[', ']', '{', '}' in any order is a string of balanced
 * parentheses or not. The input is given as a String in which white spaces,
 * tabs and newlines are ignored.
 * 
 * Some examples:
 * 
 * - " (( [( {\t (\t) [ ] } ) \n ] ) ) " is a string o balanced parentheses - " " is a
 * string of balanced parentheses - "(([)])" is NOT a string of balanced
 * parentheses - "( { } " is NOT a string of balanced parentheses - "}(([]))" is
 * NOT a string of balanced parentheses - "( ( \n [(P)] \t ))" is NOT a string
 * of balanced parentheses
 * 
 * @author Template: Luca Tesei,
 * Implementation: ALESSANDRO TESTA - alessandro.testa@studenti.unicam.it
 *
 */
public class BalancedParenthesesChecker {

    // The stack is to be used to check the balanced parentheses
    private ASDL2021Deque<Character> stack;

    /**
     * Create a new checker.
     */
    public BalancedParenthesesChecker() {
        this.stack = new ASDL2021Deque<Character>();
    }

    /**
     * Check if a given string contains a balanced parentheses sequence of
     * characters '(', ')', '[', ']', '{', '}' by ignoring white spaces ' ',
     * tabs '\t' and newlines '\n'.
     * 
     * @param s
     *              the string to check
     * @return true if s contains a balanced parentheses sequence, false
     *         otherwise
     * @throws IllegalArgumentException
     *                                      if s contains at least a character
     *                                      different form:'(', ')', '[', ']',
     *                                      '{', '}', white space ' ', tab '\t'
     *                                      and newline '\n'
     */
    public boolean check(String s) {
        //controllo se ogni carattere della stringa è corretto.
        for (Character t : s.toCharArray()) {
            if (!(t.charValue() == '('
                    || (t.charValue() == ')'
                    || (t.charValue() == '['
                    || (t.charValue() == ']'
                    || (t.charValue() == '{'
                    || (t.charValue() == '}'
                    || (t.charValue() == ' '
                    || (t.charValue() == '\t'
                    || (t.charValue() == '\n'))))))))))
                throw new IllegalArgumentException("this character is incorrect.");
        }
        //svuoto lo stack.
        this.stack.clear();
        //scorro tutti i caratteri della stringa.
        for (int i = 0; i < s.length(); i++) {
            //cursore per prendere i caratteri della stringa
            char cursorCharacter = s.charAt(i);
            //se un carattere è una parentesi aperta lo inserisco nello stack
            if (cursorCharacter == '{'
                    || cursorCharacter == '['
                    || cursorCharacter == '('){
                stack.push(cursorCharacter);
            }
            //se lo stack è vuoto (quindi non ci sono parentesi aperte inserite), e
            // la stringa contiene parentesi chiuse, la stringa non è bilanciata.
            else if((cursorCharacter == '}'
                    || cursorCharacter == ']'
                    || cursorCharacter == ')') && stack.isEmpty())
                return false;
           //altrimenti se lo stack non è vuoto, devo controllare se la stringa è bilanciata.
            else if (!stack.isEmpty()) {
                //primo elemento dello stack.
                char firstElementOfStack;
                //controllo se il prossimo carattere dopo quelli inseriti nello stack,
                // è una parentesi graffa chiusa
                if (cursorCharacter == '}') {
                    //recupero e elimino il primo elemento dello stack.
                    firstElementOfStack = stack.pop();
                    //se il primo elemento inserito nello stack non è una parentesi graffa aperta
                    //la stringa non è bilanciata
                    if (firstElementOfStack != '{')
                        return false;
                }
                //altrimenti, controllo se il prossimo carattere dopo quelli inseriti nello stack,
                // è una parentesi quadra chiusa
                else if (cursorCharacter == ']') {
                    //recupero e elimino il primo elemento dello stack.
                    firstElementOfStack = stack.pop();
                    //se il primo elemento inserito nello stack non è una parentesi quadra aperta
                    //la stringa non è bilanciata
                    if (firstElementOfStack != '[')
                        return false;
                }
                //altrimenti, controllo se il prossimo carattere dopo quelli inseriti nello stack,
                // è una parentesi tonda chiusa
                else if (cursorCharacter == ')') {
                    //recupero e elimino il primo elemento dello stack.
                    firstElementOfStack = stack.pop();
                    //se il primo elemento inserito nello stack non è una parentesi tonda aperta
                    //la stringa non è bilanciata
                    if (firstElementOfStack != '(')
                        return false;
                }
            }
        }
        if(stack.isEmpty())
            return true;
        else
            return false;
    }
}
