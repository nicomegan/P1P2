package fa.nfa;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.FAInterface;
import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

public class NFA implements FAInterface, NFAInterface {
	private Set<NFAState> states;
	private NFAState start;
	private Set<Character> ordAbc;
	private Set<NFAState> finalStateSet; //TODO: maybe have flag for final and override constructor
	

	public NFA() {
		states = new LinkedHashSet<NFAState>();
		ordAbc = new LinkedHashSet<Character>();
		finalStateSet = new LinkedHashSet<NFAState>();
		
	}
	 
	
	@Override
	public DFA getDFA() {
		// TODO Auto-generated method stub
//		DFAState dfaStartState = (DFAState) eClosure((NFAState) getStartState());
		return null;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		Set<NFAState> returnStates = new LinkedHashSet<NFAState>();
		returnStates = from.getStatesFromTransition(onSymb);
		
		return returnStates;
	}

	@Override
	public Set<NFAState> eClosure(NFAState s) {
//		// TODO Auto-generated method stub
//		Set<NFAState> returnStates = new LinkedHashSet<NFAState>();
//		if(getToState(s, 'e')==null) {
//			returnStates.add(s);
//			return returnStates;
//		}
//		returnStates=getToState(s, 'e');
//		Iterator it=returnStates.iterator();
//		while(it.hasNext()) {
//			NFAState next = (NFAState) it.next();
//			eClosure(next);
//		}
//		return returnStates;
		
		
		Set<NFAState> returnStates = new LinkedHashSet<NFAState>();
		if(getToState(s, 'e')==null) {
			returnStates.add(s);
			return returnStates;
		}
		returnStates=getToState(s, 'e');
		Iterator it=returnStates.iterator();
		while(it.hasNext()) {
			NFAState next = (NFAState) it.next();
			eClosure(next);
		}
		
	}
	
	
	
	
	
	

	@Override
	public void addStartState(String name) {
		//TODO: What if start state is final?
		NFAState s = new NFAState(name);
		this.start = s;
		if(!states.contains(getState(name))) {
			states.add(s);	
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
		s.addTransition(onSymb, s);		
	}

	private NFAState getState(String name) {
		NFAState ret = null;
		for(NFAState s: states) {
			if(s.getName().equals(name)) {
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
		// TODO Auto-generated method stub
		Iterator<NFAState> it = states.iterator();
		Set<NFAState> finalStates = new LinkedHashSet<NFAState>();
		while(it.hasNext()) {
			NFAState s = (NFAState) it.next();
			if(s.isFinal()) {
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
		// TODO Auto-generated method stub
		return ordAbc;
	}

}
