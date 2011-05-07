import java.util.*;

/**
 * This class represents an assignment for a CSP problem.
 * The assignment may be partial or complete.
 */
public class Assignment {
  /**
   * This private map is used to track the current variable assignments.
   */
  Map<Variable, Object> assignments = null;

  /**
   * This private map is used to track the restricted domain of variables
   */
  Map<Variable, List<Object>> domain = null;


  /**
   * Creates a new blank assignment.
   */
  static Assignment blank() {
    Assignment blank = new Assignment();
    blank.assignments = new HashMap<Variable, Object>();
    blank.domain = new HashMap<Variable,List<Object>>();
    return blank;
  }

  /**
   * Assigns a value to a variable and returns a new
   * assignment representing this state.
   */
  public Assignment assign(Variable v, Object val) {
    Assignment n = new Assignment();
    n.assignments = new HashMap<Variable,Object>(assignments);
    n.assignments.put(v, val);
    n.domain = new HashMap<Variable,List<Object>>(domain);
    return n;
  }

  /**
   * Gets the valuable of a variable.
   * @param v The variable to retrieve
   * @return The assigned value or null if it is unassigned.
   */
  public Object getValue(Variable v) {
    return assignments.get(v);
  }

  /**
   * Assigns a restricted domain to a variable.
   */
  public void restrictDomain(Variable v, List<Object> dom) {
    domain.put(v,dom);
  }

  /**
   * Returns the restricted domain of a variable.
   */
  public List<Object> getDomain(Variable v) {
    return domain.get(v);
  }

  /**
   * Returns the number of assignments made
   */
  public int size() {
    return assignments.size();
  }
}
