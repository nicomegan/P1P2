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
		// TODO Auto-generated method stub
		NFAState dfaStartState = start;
		eClosure(start);
		Iterator itr = states.iterator();
		while(itr.hasNext()){
			Set<NFAState> s = eClosure((NFAState) itr.next());
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
		// TODO Auto-generated method stub
		Set<NFAState> tSet = new LinkedHashSet<NFAState>();
		if(getToState(s, 'e')==null) {
			tSet.add(s);
			return tSet;
		}
		tSet=getToState(s, 'e');
		Iterator it=tSet.iterator();
//		while(it.hasNext() ) {
//			NFAState next = (NFAState) it.next();
//			eClosure(next);
//		}
		closure(s, tSet);
		return tSet;
		
		//think the stuff bellow is duplicate.
//		if(getToState(s, 'e') == null) {
//			returnStates.add(s);
//			return returnStates;
//		}
//		returnStates=getToState(s, 'e');
//		returnStates.add(s);
//		Iterator itr =returnStates.iterator();
//		while(it.hasNext()) {
//			NFAState next = (NFAState) it.next();
//			eClosure(next);
//		}
		
	}
	
	
	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet){
		if(getToState(s, 'e') == null) {
			eSet.add(s);
			return eSet;
		}
		Set<NFAState> setToAdd=getToState(s, 'e');//this is just returning the same state
		if(!eSet.contains(s)) // if self loop dont need to add s again? - check that state isnt already in there
			eSet.add(s);
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
//		closure(s, new LinkedHashSet<NFAState>());
		return eSet;
	
	}
	
	
	//////////////////////////////////////////////////////
	//Below and above are random efforts at figuring out the recursion lol
	
//	
//	public Set<NFAState> eClosure(NFAState s){	
//		Set<NFAState> newSet = new LinkedHashSet<NFAState>();
//		Set<NFAState> totalSet = new LinkedHashSet<NFAState>();
//
//		newSet = getToState(s, 'e');
//		if(newSet.isEmpty()) {
//			return totalSet;
//		}
//		Iterator<NFAState> itr1 = newSet.iterator();
//			while(itr1.hasNext()) {
//				NFAState newState = (NFAState) itr1.next();
//				if(!totalSet.contains(newState)) {
//					totalSet.add(newState);
//				}
//			}
//			
//		Iterator<NFAState> itr2 = totalSet.iterator();
//			while(itr2.hasNext()) {
//				closure(itr2.next(), totalSet); 
//				
//			}
//			return totalSet;
//	
//	}
//	
//	public Set<NFAState> closure(NFAState s, Set<NFAState> eSet){
//		return eSet;
//	
//	}
	
	
	/////////////////////////////////////////////////////////////
	// Global method mostly done
	int i = 0;
	int tempi = 0;

	
	public Set<NFAState> newClosure(NFAState s){
		boolean canTransOnE = true;
		if((tempi + 1) == i) {
			return totalSet;
		}
		if(!totalSet.contains(s)) {//dont need to add if it contains. Or call closure on it too?
			totalSet.add(s);
			i++;
		}
		newSet = getToState(s, 'e');
		if(newSet==null){//cannot transition on empty. Base case.
			canTransOnE=false;
			totalSet.add(s);
			return totalSet;
		}
		//getToStateReturns null
		Iterator<NFAState> itr = newSet.iterator();
		while(itr.hasNext()) {
			NFAState newState = itr.next();
			if(!totalSet.contains(newState)){
				totalSet.add(newState);
			}
		}
		
		return totalSet;
		
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
		s.addTransitionToState(onSymb, s);
		if(s.getName().equals(start.getName())){
			start.addTransitionToState(onSymb, s);
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