import java.util.*;

/**
 * Extends Backtrack to add a Most-Restricted-Variable heuristic.
 */
public class MRVBacktrack extends Backtrack {

  /**
   * Starts the Backtracker given a blank assignment.
   */
  public MRVBacktrack(CSPProblem prob) {
    super(prob);
  }

  /**
   * Starts the Backtracker given a problem and a specific
   * assignment.
   */
  public MRVBacktrack(CSPProblem prob, Assignment initial) {
    super(prob, initial);
  }

  /**
   * Returns an assigned variable to try to assign.
   * Apply a Minimum remaining values heuristic.
   */
  protected Variable unassignedVar(Assignment assign) {
    int minDomain = Integer.MAX_VALUE;
    Variable minVar = null;

    // Find any non-assigned variable
    List<Variable> vars = problem.variables();
    if (vars.size() == assign.size()) return null;

    for (Variable v : vars) {
      if (assign.getValue(v) == null) {
        int domSize = problem.domainValues(assign, v).size();
        if (domSize < minDomain) {
          minDomain = domSize;
          minVar = v;
        }
      }
    }

    return minVar;
  }

  /**
   * Returns an ordered list of values to try to assign.
   * Apply a least constraining value heuristic
   */
  protected List<Object> orderValues(Assignment asign, List<Object> domain, Variable v) {
    PriorityQueue<ValueAssignment> priority = new PriorityQueue<ValueAssignment>();
    for (Object val : domain) {
      try {
        priority.add(new ValueAssignment(asign, v, val));
      } catch (IllegalStateException e) {}
    }

    LinkedList<Object> ordered = new LinkedList<Object>();
    int size = priority.size();
    for (int i=0;i<size;i++) {
      ValueAssignment next = priority.poll();
      ordered.add(next.value);
    }

    return ordered;
  }

  class ValueAssignment implements Comparable<ValueAssignment> {
    public Integer domain = 0;
    public Object value;

    public ValueAssignment(Assignment init, Variable v, Object val) throws IllegalStateException {
      // Make a new assignment
      value = val;
      Assignment newAssign = init.assign(v, val);

      // Check the consistency
      if (!problem.consistentAssignment(newAssign, v)) { 
        throw new IllegalStateException("Bad Value");
      }

      // Try making some inferences
      newAssign = problem.inference(newAssign, v);

      // Calculate the total domain size
      for (Variable var : problem.variables()) {
        domain += problem.domainValues(newAssign, var).size();
      }
    }

    public int compareTo(ValueAssignment other) {
      return domain.compareTo(other.domain);
    }
  }
}
