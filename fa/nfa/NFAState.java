package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFAState;

public class NFAState extends fa.State {

	private Set<NFAState> finalStateSet = new LinkedHashSet<NFAState>();
	private Boolean isFinal = false;
	private HashMap<Character, Set<NFAState>> hMap;// delta
	private Set<NFAState> toStates = new LinkedHashSet<NFAState>();

	public NFAState(String name) {
		this.name = name;
		hMap = new HashMap<Character, Set<NFAState>>();
		isFinal= false;

	}

	public NFAState(String name, Boolean isFinal) {
		this.name = name;
		this.isFinal = true;
		hMap = new HashMap<Character, Set<NFAState>>();

	}
	
	public boolean isFinal() {
		return isFinal;
	}
	
	public void setFinal(NFAState s) {
		isFinal=true;
	}

	public void addTransition(char onSymb, NFAState toState) {
		// toState.
		// hMap.put(onSymb, (Set<NFAState>) toStates);
		if(hMap.get(onSymb)==null) {
			Set<NFAState> newSet = new LinkedHashSet<NFAState>();
			newSet.add(toState);
			hMap.put(onSymb, newSet);
		}
		hMap.get(onSymb).add(toState);// Think we want to add to the list that is stored
	}
	
	public Set<NFAState> getStatesFromTransition(char onSymb) {
		return hMap.get(onSymb);
	}

}
