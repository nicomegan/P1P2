package fa.nfa;

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
	private Set<NFAState> finalStateSet; //TODO: maybe have flag for final and override constructor
	Set<NFAState> totalSet = new LinkedHashSet<NFAState>();
	Set<NFAState> newSet = new LinkedHashSet<NFAState>();
	private Queue q = new Queue();
	

	public NFA() {
		states = new LinkedHashSet<NFAState>();
		ordAbc = new LinkedHashSet<Character>();
		finalStateSet = new LinkedHashSet<NFAState>();
		
	}
	 
	
	@Override
	public DFA getDFA() {
		System.out.println(states);
		NFAState dfaStartState = start;
		eClosure(start);
		Iterator itr = states.iterator();
		int i = 0;
		while(itr.hasNext()){
			NFAState state = (NFAState) itr.next();
			Set<NFAState> s = eClosure(state);
			System.out.println("Set: "+i + " "+s);
			i++;
		}
		return null;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		//from isnt null
		Set<NFAState> returnStates =from.getStatesFromTransition(onSymb);
		//getStatesFromTransisiton is null
		return returnStates;
	}

	//I think the two bellow work.
	@Override
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> tSet = new LinkedHashSet<NFAState>();
		if(getToState(s, 'e')==null) {
			tSet.add(s);
			return tSet;
		}
		tSet=getToState(s, 'e');
		closure(s, tSet);
		return tSet;
		
	}
	
	
	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet){
		if(getToState(s, 'e') == null) {
			eSet.add(s);
			return eSet;
		}
		Set<NFAState> setToAdd=getToState(s, 'e');//this is just returning the same state
		if(!eSet.contains(s)) { // if self loop dont need to add s again? - check that state isnt already in there
			eSet.add(s);
		}else {
			return eSet;
		}
		Iterator it=setToAdd.iterator();
		while(it.hasNext()) {
			NFAState next = (NFAState) it.next();
			eSet.add(next);
			q.enqueue(next);
		}
		if(!q.isEmpty()){
			NFAState nextState = null;
			try {
				nextState = (NFAState) q.dequeue();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(nextState.getStatesFromTransition('e')!=null){
				Set<NFAState> n = closure(nextState,eSet); //currently causes overflow. not leaving loop????
			}
		}
		return eSet;
	
	}
	@Override
	public void addStartState(String name) {
		//TODO: What if start state is final?
		NFAState s = new NFAState(name);
		this.start = s;
		if(!states.contains(getState(name))) {
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
		s.addTransitionToState(onSymb, newS);
		if(s.getName().equals(start.getName())){
			start.addTransitionToState(onSymb, newS);
		}
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