import java.util.*;

/**
 * This class represents an abstract variable.
 * The variable should be associated with a problem,
 * and can be part of a partial assignment.
 */
public abstract class Variable {

  /**
   * A human readable description of the variable.
   * To help debugging.
   */
  public abstract String description();

  /**
   * Returns the domain of possible values for the Variable.
   */
  public abstract List<Object> domain();
}
