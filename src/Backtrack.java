import java.util.*;

/**
 * This class implements a Backtracking search to find
 * a solution to a CSP Problem.
 */
public class Backtrack {

  CSPProblem problem;
  Assignment start;

  /**
   * Starts the Backtracker given a blank assignment.
   */
  public Backtrack(CSPProblem prob) {
    this(prob, Assignment.blank());
  }

  /**
   * Starts the Backtracker given a problem and a specific
   * assignment.
   */
  public Backtrack(CSPProblem prob, Assignment initial) {
    problem = prob;
    start = initial;
  }

  /**
   * Solves the problem.
   * @return An assignment that satisfies all constraints or null
   * if none is found.
   */
  public Assignment solve() {
    // Start solving from the initial state
    return recursiveSolve(start); 
  }


  private Assignment recursiveSolve(Assignment assign) {
    if (problem.satisfiedByAssignment(assign)) {
      return assign;
    }

    // Get an unassigned variable
    Variable v = unassignedVar(assign);
    if (v == null) return null;

    // Get the domain values for a variable
    List<Object> values = domainValues(assign, v);

    for (Object value : values) {
      // Make a new assignment
      Assignment newAssign = assign.assign(v, value);

      // Check the consistency
      if (!consistentAssignment(newAssign, v)) { continue; }

      // Recurse
      newAssign = recursiveSolve(assign);
      if (newAssign != null) return newAssign;
    }

    // Failed
    return null;
  }

  /**
   * Returns an assigned variable to try to assign.
   * This can be sub-classed to add heuristics.
   */
  protected Variable unassignedVar(Assignment assign) {
    // Find any non-assigned variable
    for (Variable v : problem.variables()) {
      if (assign.getValue(v) == null) return v;
    }
    return null;
  }

  /**
   * Returns a list of potential domain values
   * for a varaible. This can be sub-classed to
   * add heuristics.
   */
  protected List<Object> domainValues(Assignment assign, Variable v) {
    return v.domain();
  }

  /**
   * Returns if the new variable assignment is
   * consistent. This can be sub-classed to
   * add heuristics.
   */
  protected boolean consistentAssignment(Assignment assign, Variable v) {
    // Get all the constraints on the variable
    List<Constraint> constr = problem.variableConstraints(v);

    // Check everything is consistent
    for (Constraint c : constr) {
      if (!c.satisfied(assign)) return false;
    }

    return true;
  }
}
