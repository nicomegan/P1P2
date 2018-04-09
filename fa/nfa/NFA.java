package fa.nfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.FAInterface;
import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;
import sun.misc.Queue;

public class NFA implements FAInterface, NFAInterface {
	private Set<NFAState> states;
	private NFAState start;
	private Set<Character> ordAbc;
	private Set<NFAState> finalStateSet; // TODO: maybe have flag for final and override constructor
	Set<NFAState> totalSet = new LinkedHashSet<NFAState>();
	Set<NFAState> newSet = new LinkedHashSet<NFAState>();
	private Queue<NFAState> q = new Queue();
	private DFA dfa = new DFA();

	public NFA() {
		states = new LinkedHashSet<NFAState>();
		ordAbc = new LinkedHashSet<Character>();
		finalStateSet = new LinkedHashSet<NFAState>();

	}

	@Override
	public DFA getDFA() {
		System.out.println(states);
		Iterator<NFAState> itr = states.iterator();
		System.out.println("Closures");
		while (itr.hasNext()) {
			NFAState state = (NFAState) itr.next();
			state.addClosure(eClosure(state));
		}
		start.addClosure(eClosure(start));

		getNFATable();

		return dfa;
	}

	// fills in the hMap for the states so the 'e' is not needed
	public void getNFATable() {
		LinkedHashSet<NFAState> startClosure = (LinkedHashSet<NFAState>) eClosure(start);
		for (NFAState state : states) {
			LinkedHashSet<NFAState> set = (LinkedHashSet<NFAState>) eClosure(state);

			for (char c : ordAbc) {
				for (NFAState s : set) {
					if (c != 'e') {
						fillTableCell(s, c, new LinkedHashSet<NFAState>());
//						if (eClosure(s).size() > 1) {
////							fillCellEmptyTrans(s, c, new LinkedHashSet<NFAState>());
//						}
					}
				}
			}
		}
		Iterator itr = states.iterator();
		while (itr.hasNext()) {
			NFAState n = (NFAState) itr.next();
			System.out.println(n.getName() + ": \n " + n.getMap());
		}
	}

	Queue<NFAState> queue = new Queue<NFAState>();

	public void addToQueue(Set s, LinkedHashSet ls) {
		Iterator<NFAState> itr = s.iterator();
		while (itr.hasNext()) {
			NFAState n = itr.next();
			if (!ls.contains(n)) {
				queue.enqueue(n);
				ls.add(n);
			}
		}
	}

	public void fillTableCell(NFAState s, char c, LinkedHashSet<NFAState> ls) {

		// character first
		LinkedHashSet<NFAState> transSet = (LinkedHashSet<NFAState>) s.getStatesFromTransition(c);
		if (transSet != null) {
			addToQueue(transSet, ls);
		}

		while (!queue.isEmpty()) {
			NFAState ns = null;
			try {
				ns = queue.dequeue();

				s.addTransitionToState(c, ns);
				LinkedHashSet<NFAState> anotherS = (LinkedHashSet<NFAState>) eClosure(ns);
				if (anotherS != null) {
					addToQueue(anotherS, ls);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// handle ones with empty transitions???
	public void fillCellEmptyTrans(NFAState s, char c, LinkedHashSet<NFAState> ls) {
		LinkedHashSet<NFAState> anotherS = (LinkedHashSet<NFAState>) eClosure(s);// gets empty transitions
		addToQueue(anotherS, ls);
		Queue eQ = new Queue();
		while (!queue.isEmpty()) {
			NFAState ns = null;
			try {
				ns = queue.dequeue();
				HashMap m = ns.getMap();
				LinkedHashSet<NFAState> goThrough = (LinkedHashSet<NFAState>) ns.getStatesFromTransition(c);
				if (goThrough != null) {
					s.addTransitionToState(c, ns);
				}

				// eQ.enqueue(ns);
				// LinkedHashSet<NFAState> as = (LinkedHashSet<NFAState>) eClosure(ns);
				// addToQueue(as, ls);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	// public void fillTableCell(NFAState s, char c, LinkedHashSet<NFAState> ls) {
	//
	// //use transition then
	// LinkedHashSet<NFAState> transSet = (LinkedHashSet<NFAState>)
	// s.getStatesFromTransition(c);
	//
	// if (transSet !=null) {
	// addToQueue(transSet, ls);
	// }else {
	//
	// s.addTransitionToState(c, s);
	// }
	//
	// while (!queue.isEmpty()) {
	// NFAState ns = null;
	// try {
	// ns = queue.dequeue();
	// ts.add(ns);
	// s.addTransitionToState(c, ns);
	// if (ns.getStatesFromTransition(c) != null) {
	// LinkedHashSet<NFAState> newTs = (LinkedHashSet) s.getStatesFromTransition(c);
	// LinkedHashSet<NFAState> anotherS = (LinkedHashSet<NFAState>) eClosure(s);
	// if (anotherS != null) {
	// Iterator<NFAState> i = anotherS.iterator();
	// while (i.hasNext()) {
	// newTs.add(i.next());
	// }
	// }
	// if (newTs != null) {
	// addToQueue(newTs, ls);
	// }
	// }
	//
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// from isnt null
		Set<NFAState> returnStates = from.getStatesFromTransition(onSymb);
		return returnStates;
	}

	// I think the two bellow work.
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

	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet) {
		if (getToState(s, 'e') == null) {
			eSet.add(s);
			return eSet;
		}
		Set<NFAState> setToAdd = getToState(s, 'e');// this is just returning the same state
		if (!eSet.contains(s)) { // if self loop dont need to add s again? - check that state isnt already in
									// there
			eSet.add(s);
		} else {
			return eSet;
		}
		Iterator it = setToAdd.iterator();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (nextState.getStatesFromTransition('e') != null) {
				closure(nextState, eSet); // currently causes overflow. not leaving loop????
			}
		}
		return eSet;

	}

	@Override
	public void addStartState(String name) {
		NFAState s = new NFAState(name);
		this.start = s;
		if (!states.contains(getState(name))) {
			states.add(start);
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
		System.out.println("ADDING Tansition: from " + s + ", to " + newS);
		s.addTransitionToState(onSymb, newS);
		if (s.getName().equals(start.getName())) {
			start.addTransitionToState(onSymb, newS);
		}
		ordAbc.add(onSymb);
	}

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