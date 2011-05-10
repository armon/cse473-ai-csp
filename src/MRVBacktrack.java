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
   * This can be sub-classed to add heuristics.
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
    System.out.println("Min: "+minDomain);

    return minVar;
  }
}
