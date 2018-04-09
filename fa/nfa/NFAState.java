package fa.nfa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import fa.dfa.DFAState;

public class NFAState extends fa.State {

	private Set<NFAState> finalStateSet = new LinkedHashSet<NFAState>();
	private Boolean isFinal = false;
	private HashMap<Character, Set<NFAState>> hMap;// delta
	private Set<NFAState> toStates = new LinkedHashSet<NFAState>();
	private Set<NFAState> closure;

	public NFAState(String name) {
		this.name = name;
		hMap = new HashMap<Character, Set<NFAState>>();
		isFinal = false;
		closure = new LinkedHashSet();

	}

	public NFAState(String name, Boolean isFinal) {
		this.name = name;
		this.isFinal = true;
		hMap = new HashMap<Character, Set<NFAState>>();
		closure = new LinkedHashSet();
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(NFAState s) {
		isFinal = true;
	}

	public void addTransitionToState(char onSymb, NFAState toState) {
		// toState.
		// hMap.put(onSymb, (Set<NFAState>) toStates);
		if (hMap.get(onSymb) == null) {
			// toStates.add(toState);
			hMap.put(onSymb, new LinkedHashSet());
		}
		hMap.get(onSymb).add(toState);// Think we want to add to the list that is stored
	}

	public Set<NFAState> getStatesFromTransition(char onSymb) {
		Set s = hMap.get(onSymb);
		return hMap.get(onSymb);
	}

	public HashMap<Character, Set<NFAState>> getMap() {
		return hMap;
	}

	public void addClosure(Set<NFAState> s) {
		Iterator<NFAState> itr = s.iterator();
		while (itr.hasNext()) {
			closure.add((NFAState) itr.next());
		}
	}
	public Set getClosure() {
		return closure;
	}
	
	public String getTransName(char c) {
		String s="";
		Set set = hMap.get(c);
		Iterator itr = set.iterator();
		while(itr.hasNext()) {
			NFAState state = (NFAState) itr.next();
			String stateString = state.getName();
			s+=stateString;
		}
		
		return s;
	}
}
