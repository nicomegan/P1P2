package fa.nfa;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import fa.FAInterface;
import fa.State;
import fa.dfa.DFA;
import sun.misc.Queue;

/**
 * This makes an NFA with some methods to get state, add states and transitions,
 * and get a DFA.
 * 
 * @author Anne Brinegar, Megan Pierce
 *
 */
public class NFA implements FAInterface, NFAInterface {
	private Set<NFAState> states;
	private NFAState start;
	private Set<Character> ordAbc;
	@SuppressWarnings("unused")
	private Set<NFAState> finalStateSet;
	@SuppressWarnings("unused")
	private Set<NFAState> totalSet = new LinkedHashSet<NFAState>();
	@SuppressWarnings("unused")
	private Set<NFAState> newSet = new LinkedHashSet<NFAState>();
	private Queue<NFAState> q = new Queue<NFAState>();
	private DFA dfa = new DFA();
	private Queue<NFAState> queue = new Queue<NFAState>();

	/**
	 * NFA default contructor.
	 */
	public NFA() {
		states = new LinkedHashSet<NFAState>();
		ordAbc = new LinkedHashSet<Character>();
		finalStateSet = new LinkedHashSet<NFAState>();
	}

	@Override
	public DFA getDFA() {
		Iterator<NFAState> itr = states.iterator();
		while (itr.hasNext()) {
			NFAState state = (NFAState) itr.next();
//			state.addClosure(
					eClosure(state);
		}
//		start.addClosure(
				eClosure(start);

		getNFATable();

		Set<NFAState> startSet = eClosure(start);
		if(start.isFinal()) {
			dfa.addFinalState(makeTreeSet(startSet).toString());
		}
		dfa.addStartState(makeTreeSet(startSet).toString());

		Set<String> dfaStates = new LinkedHashSet<String>();
		dfaStates.add(startSet.toString());

		constructDFA(startSet, dfaStates);

		return dfa;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		Set<NFAState> returnStates = from.getStatesFromTransition(onSymb);
		return returnStates;
	}

	@Override
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> tSet = new LinkedHashSet<NFAState>();
		if (getToState(s, 'e') == null) {
			tSet.add(s);
			return tSet;
		}
		tSet = getToState(s, 'e');
		closure(s, tSet);
		return tSet;

	}

	/**
	 * Recursive method to find the closure and return it in a set.
	 * 
	 * @param s
	 *            - State to find closure of.
	 * @param eSet
	 *            - return set that will be recursively updated.
	 * @return - set of the states in closure.
	 */
	private Set<NFAState> closure(NFAState s, Set<NFAState> eSet) {
		if (getToState(s, 'e') == null) {
			eSet.add(s);
			return eSet;
		}
		Set<NFAState> setToAdd = getToState(s, 'e');
		boolean contains = false;
		Iterator itr = eSet.iterator();
		while(itr.hasNext()) {
			NFAState next = (NFAState) itr.next();
			if(s.getName().equals(next.getName())) {
				contains = true;
			}
		}
		if (!contains) {
			eSet.add(s);
		} else {
			return eSet;
		}
		Iterator<NFAState> it = setToAdd.iterator();
		while (it.hasNext()) {
			NFAState next = (NFAState) it.next();
			eSet.add(next);
			q.enqueue(next);
		}
		if (!q.isEmpty()) {
			NFAState nextState = null;
			try {
				nextState = (NFAState) q.dequeue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (nextState.getStatesFromTransition('e') != null) {
				closure(nextState, eSet);
			}
		}
		return eSet;

	}

	/**
	 * Completes the NFA table.
	 */
	private void getNFATable() {
		for (NFAState state : states) {
			LinkedHashSet<NFAState> set = (LinkedHashSet<NFAState>) eClosure(state);

			for (char c : ordAbc) {
				for (NFAState s : set) {
					if (c != 'e') {
						fillTableCell(s, c, new LinkedHashSet<NFAState>());
					}
				}
			}
		}
	}

	/**
	 * adds elements in a set to a queue.
	 * 
	 * @param s
	 *            - set of NFA states.
	 * @param ls
	 *            - set to track if it needs to be added to queue.
	 */
	public void addToQueue(Set<NFAState> s, LinkedHashSet<NFAState> ls) {
		Iterator<NFAState> itr = s.iterator();
		while (itr.hasNext()) {
			NFAState n = itr.next();
			if (!ls.contains(n)) {
				queue.enqueue(n);
				ls.add(n);
			}
		}
	}

	/**
	 * Fills an inividual cell of a NFA table.
	 * 
	 * @param s
	 *            - the NFA state.
	 * @param c
	 *            - the character to read in.
	 * @param ls
	 *            - a linkedHashSet of all states seen.
	 */
	private void fillTableCell(NFAState s, char c, LinkedHashSet<NFAState> ls) {
		LinkedHashSet<NFAState> cSet = (LinkedHashSet<NFAState>) eClosure(s);
		addToQueue(cSet, ls);

		while (!queue.isEmpty()) {
			NFAState ns = null;
			try {
				ns = queue.dequeue();

				LinkedHashSet<NFAState> anotherS = (LinkedHashSet<NFAState>) getToState(ns, c);
				if (anotherS != null) {
					Iterator<NFAState> i = anotherS.iterator();
					while (i.hasNext()) {
						NFAState next = (NFAState) i.next();
						s.addTransitionToState(c, next);
						Iterator<NFAState> it = eClosure(next).iterator();
						while (it.hasNext()) {
							NFAState next2 = (NFAState) it.next();
							s.addTransitionToState(c, next2);
						}
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Makes dfa given the start state has already been found and passed in.
	 * 
	 * @param initialState
	 *            - the initial state that will start a transition
	 * @param statesInDFA
	 *            - a list of all states in the dfa.
	 */
	private void constructDFA(Set<NFAState> initialState, Set<String> statesInDFA) {
		TreeSet<String> tsInitialState = makeTreeSet(initialState);
		for (NFAState s : initialState) {
			if (s.isFinal()) {
				if (!statesInDFA.contains(tsInitialState.toString())) {
					dfa.addFinalState(tsInitialState.toString());
					statesInDFA.add(tsInitialState.toString());
					break;
				}
			}
		}

		if (!statesInDFA.contains(tsInitialState.toString())) {
			dfa.addState(tsInitialState.toString());
			statesInDFA.add(tsInitialState.toString());
		}
		for (char c : ordAbc) {
			if (c != 'e') {
				Set<NFAState> toStates = new LinkedHashSet<NFAState>();
				for (NFAState s : initialState) {
					Set<NFAState> tansStates = getToState(s, c);
					if (tansStates != null && !tansStates.isEmpty()) {
						toStates.addAll(tansStates);
					}
				}
				TreeSet<String> ts = makeTreeSet(toStates);
				if (!statesInDFA.contains(ts.toString())) {
					constructDFA(toStates, statesInDFA);
				}
				dfa.addTransition(tsInitialState.toString(), c, ts.toString());
			}
		}
	}

	/**
	 * Makes a treeset given a set of nfa states.
	 * 
	 * @param s
	 *            - set of NFAStates.
	 * @return - treeset of the nfa states.
	 */
	private TreeSet<String> makeTreeSet(Set<NFAState> s) {
		TreeSet<String> ts = new TreeSet<String>();
		for (NFAState ns : s) {
			ts.add(ns.toString());
		}
		return ts;
	}

	@Override
	public void addStartState(String name) {
		NFAState s = new NFAState(name);
		this.start = s;
		if (!states.contains(getState(name))) {
			states.add(start);
		}else {
			if(start.getName().equals(name)) {
				start.setFinal();
			}
		}

	}

	@Override
	public void addState(String name) {
		states.add(new NFAState(name));

	}

	@Override
	public void addFinalState(String name) {
		NFAState s = new NFAState(name, true);
		states.add(s);

	}

	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		NFAState s = getState(fromState);
		NFAState newS = getState(toState);
		s.addTransitionToState(onSymb, newS);
		if (s.getName().equals(start.getName())) {
			start.addTransitionToState(onSymb, newS);
		}
		ordAbc.add(onSymb);
	}

	/**
	 * Gets the state given a string.
	 * 
	 * @param name
	 *            - String name associated with the state.
	 * @return - State that has the given name
	 */
	private NFAState getState(String name) {
		NFAState ret = null;

		for (NFAState s : states) {
			if (s.getName().equals(name)) {
				ret = s;
				break;
			}
		}
		return ret;
	}

	@Override
	public Set<? extends State> getStates() {
		return states;
	}

	@Override
	public Set<? extends State> getFinalStates() {
		Iterator<NFAState> it = states.iterator();
		Set<NFAState> finalStates = new LinkedHashSet<NFAState>();
		while (it.hasNext()) {
			NFAState s = (NFAState) it.next();
			if (s.isFinal()) {
				finalStates.add(s);
			}
		}
		return finalStates;
	}

	@Override
	public State getStartState() {
		return start;
	}

	@Override
	public Set<Character> getABC() {
		return ordAbc;
	}
}