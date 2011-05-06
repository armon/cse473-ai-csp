import java.util.*;

/**
 * This class represents an abstract Constraint
 * associated with a CSP Problem.
 */
public abstract class Constraint {

  /**
   * A human readable description of the constraint.
   * To help debugging.
   */
  public String description;

  /**
   * Checks if the constraint is satisfied by the an assignment.
   */
  public abstract boolean satisfied(Assignment asgn);

  /**
   * Returns the variables that the constraint relies on
   */
  public abstract List<Variable> reliesOn();
}
