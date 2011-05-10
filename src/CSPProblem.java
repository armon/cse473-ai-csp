import java.util.*;

/**
 * This class represents an abstract "problem".
 * A problem is anything that can be modeled in as a CSP.
 */
public abstract class CSPProblem {

  /**
   * Returns a list of variables associated with the problem.
   */
  public abstract List<Variable> variables();


  /**
   * Returns a list of constraints associated with the problem.
   */
  public abstract List<Constraint> constraints();

  /**
   * Checks if a given assignment satisfies the problem.
   */
  public boolean satisfiedByAssignment(Assignment asign) {
    List<Constraint> constraints = constraints();

    // Check that we have enough assignments for this to
    // even make sense
    if (constraints.size() > asign.size()) { return false; }

    for (Constraint c : constraints) {
      if (!c.satisfied(asign)) {
        return false;
      }
    }

    return true;
  }


  /**
   * Map that stores the constraints
   * on any given variable.
   */
  Map<Variable, List<Constraint>> varConstraints = null;

  /**
   * Returns all the constraints that rely on a given variable.
   */
  public List<Constraint> variableConstraints(Variable v) {
    if (varConstraints != null) return varConstraints.get(v);
    varConstraints = new HashMap<Variable, List<Constraint>>();

    for (Constraint c : constraints()) {
      List<Variable> vars = c.reliesOn();
      for (Variable constrVar : vars) {
        // Add the constraint if we have a mapping
        if (varConstraints.containsKey(constrVar)) {
          varConstraints.get(constrVar).add(c);

        // Create a mapping between the variable and this constraint
        } else {
          List<Constraint> constr = new LinkedList<Constraint>();
          constr.add(c);
          varConstraints.put(constrVar, constr);
        }
      }
    }
    return varConstraints.get(v);
  }

  
  /**
   * Returns a list of potential domain values
   * for a varaible. This can be sub-classed to
   * add heuristics.
   */
  public List<Object> domainValues(Assignment assign, Variable v) {
    if (assign.getDomain(v) != null) return assign.getDomain(v);
    return v.domain();
  }

  /**
   * Returns if the new variable assignment is
   * consistent. This can be sub-classed to
   * add heuristics.
   */
  public boolean consistentAssignment(Assignment assign, Variable v) {
    // Get all the constraints on the variable
    List<Constraint> constr = variableConstraints(v);

    // Check everything is consistent
    for (Constraint c : constr) {
      if (!c.consistent(assign)) return false;
    }

    return true;
  }

  /**
   * Returns a new assignment based on some inferences.
   * This can be sub-classed to add heuristics.
   */
  public Assignment inference(Assignment assign, Variable v) {
    return assign;
  }
}
