package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFAState;

public class NFAState extends fa.State { 


	private Set<NFAState> finalStateSet = new LinkedHashSet<NFAState>();
	private Boolean isFinal = false;
	private HashMap<Character,Set<NFAState> > hMap;//delta
	private Set<NFAState> toStates= new LinkedHashSet<NFAState>();



	
	
	public NFAState(String name) {
		this.name=name;
		hMap = new HashMap<Character,Set<NFAState>>();
	
	}
	
	public NFAState(String name, Boolean isFinal) {
		this.name= name;
		this.isFinal = true;
		hMap = new HashMap<Character,Set<NFAState>>();

		
	}
	
	public void addTransition(char onSymb, NFAState toState){
		toState.
		hMap.put(onSymb, toState);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
