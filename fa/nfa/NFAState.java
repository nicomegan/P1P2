package fa.nfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import fa.dfa.DFAState;

/**
 * Contains the data for a NFA state.
 * 
 * @author Anne Brinegar, Megan Pierce
 *
 */
public class NFAState extends fa.State {

	private Set<NFAState> finalStateSet = new LinkedHashSet<NFAState>();
	private Boolean isFinal = false;
	private HashMap<Character, Set<NFAState>> hMap;// delta
	private Set<NFAState> toStates = new LinkedHashSet<NFAState>();
	private Set<NFAState> closure;

	/**
	 * Constructor for an NFAState
	 * 
	 * @param name
	 *            - string
	 */
	public NFAState(String name) {
		this.name = name;
		hMap = new HashMap<Character, Set<NFAState>>();
		isFinal = false;
		closure = new LinkedHashSet();

	}

	/**
	 * Constructor for NFAstate
	 * 
	 * @param name
	 *            - string
	 * @param isFinal
	 *            - boolean flag
	 */
	public NFAState(String name, Boolean isFinal) {
		this.name = name;
		this.isFinal = true;
		hMap = new HashMap<Character, Set<NFAState>>();
		closure = new LinkedHashSet();
	}

	/**
	 * returns if the state is final.
	 * 
	 * @return - boolean
	 */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * Sets a state as a final state.
	 * 
	 * @param s
	 *            - state to set as final.
	 */
	public void setFinal() {
		isFinal = true;
	}

	/**
	 * adds a transition to the map.
	 * 
	 * @param onSymb
	 *            - char transition letter
	 * @param toState
	 *            - NFAState
	 */
	public void addTransitionToState(char onSymb, NFAState toState) {

		if (hMap.get(onSymb) == null) {
			hMap.put(onSymb, new LinkedHashSet());
		}
		hMap.get(onSymb).add(toState);// Think we want to add to the list that is stored
	}

	/**
	 * gets the set of states that can be transitioned to on a specific charter
	 * 
	 * @param onSymb
	 *            - char
	 * @return - set of states
	 */
	public Set<NFAState> getStatesFromTransition(char onSymb) {
		Set s = hMap.get(onSymb);
		return hMap.get(onSymb);
	}

	/**
	 * gets the hashmap of transitions
	 * 
	 * @return - hashmap
	 */
	public HashMap<Character, Set<NFAState>> getMap() {
		return hMap;
	}

//	/**
//	 * adds the closure to a state.
//	 * 
//	 * @param s
//	 *            - set of states to add closures to.
//	 */
//	public void addClosure(Set<NFAState> s) {
//		Iterator<NFAState> itr = s.iterator();
//		while (itr.hasNext()) {
//			closure.add((NFAState) itr.next());
//		}
//	}
//
//	/**
//	 * Gets the closure set.
//	 * 
//	 * @return - closure set.
//	 */
//	public Set getClosure() {
//		return closure;
//	}

	/**
	 * Returns a string representation of a transition set.
	 * 
	 * @param c
	 *            - char to transition on
	 * @return - string
	 */
	public String getTransName(char c) {
		String s = "";
		Set set = hMap.get(c);
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			NFAState state = (NFAState) itr.next();
			String stateString = state.getName();
			s += stateString;
		}

		return s;
	}
}