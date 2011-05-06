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

    boolean satisfied = true;
    for (Constraint c : constraints) {
      if (!c.satisfied(asign)) {
        satisfied = false;
        break;
      }
    }
    return satisfied;
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
}
