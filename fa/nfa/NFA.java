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

//	@Override
//	public Set<NFAState> eClosure(NFAState s) {
////		// TODO Auto-generated method stub
////		Set<NFAState> returnStates = new LinkedHashSet<NFAState>();
////		if(getToState(s, 'e')==null) {
////			returnStates.add(s);
////			return returnStates;
////		}
////		returnStates=getToState(s, 'e');
////		Iterator it=returnStates.iterator();
////		while(it.hasNext()) {
////			NFAState next = (NFAState) it.next();
////			eClosure(next);
////		}
////		return returnStates;
//		
//		
//		Set<NFAState> returnStates = new LinkedHashSet<NFAState>();
//		if(getToState(s, 'e') == null) {
//			returnStates.add(s);
//			return returnStates;
//		}
//		returnStates=getToState(s, 'e');
//		returnStates.add(s);
//		Iterator it=returnStates.iterator();
//		while(it.hasNext()) {
//			NFAState next = (NFAState) it.next();
//			eClosure(next);
//		}
//		closure(s, new LinkedHashSet<NFAState>());
//		
//	}
//	
//	
//	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet){
//		if(getToState(s, 'e') == null) {
//			eSet.add(s);
//			return eSet;
//		}
//		eSet=getToState(s, 'e');
//		if(!eSet.contains(s)) // if self loop dont need to add s again? - check that state isnt already in there
//			eSet.add(s);
//		Iterator it=eSet.iterator();
//		while(it.hasNext()) {
//			NFAState next = (NFAState) it.next();
//			State n = eClosure(next);
//			if()
//		}
//		closure(s, new LinkedHashSet<NFAState>());
//		
//		
//		
//		
//		return null;
//	}
	
	
	//////////////////////////////////////////////////////
	//Below and above are random efforts at figuring out the recursion lol
	
	
	public Set<NFAState> eClosure(NFAState s){	
		Set<NFAState> newSet = new LinkedHashSet<NFAState>();
		Set<NFAState> totalSet = new LinkedHashSet<NFAState>();

		newSet = getToState(s, 'e');
		if(newSet.isEmpty()) {
			return totalSet;
		}
		Iterator<NFAState> itr1 = newSet.iterator();
			while(itr1.hasNext()) {
				NFAState newState = (NFAState) itr1.next();
				if(!totalSet.contains(newState)) {
					totalSet.add(newState);
				}
			}
			
		Iterator<NFAState> itr2 = totalSet.iterator();
			while(itr2.hasNext()) {
				closure(itr2.next(), totalSet); 
			}
	
	}
	
	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet){
			
		
	
	}
	
	
	/////////////////////////////////////////////////////////////
	// Global method mostly done
	int i = 0;
	int tempi = 0;
	Set<NFAState> totalSet = new LinkedHashSet<NFAState>();
	Set<NFAState> newSet = new LinkedHashSet<NFAState>();

	
	public Set<NFAState> newClosure(NFAState s){
		
		if((tempi + 1) == i) {
			return totalSet;
		}
		if(totalSet.contains(s)) {
			totalSet.add(s);
			i++;
		}
		
		newSet = getToState(s, 'e');
		
		Iterator<NFAState> itr = newSet.iterator();
		while(itr.hasNext()) {
			NFAState newState = itr.next();
			if(!totalSet.contains(newState)){
				totalSet.add(newState);
			}
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