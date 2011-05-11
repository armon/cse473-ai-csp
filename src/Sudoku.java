import java.util.*;

/**
 * Implements all the proper interfaces to formulate
 * Sudoku as a Constraint Satisfaction Problem.
 */
public class Sudoku extends CSPProblem {

  /**
   * The size of the board.
   */
  int boardSize = 9;

  // List of variable tiles
  List<Variable> tiles;

  // List of all constraints
  List<Constraint> constraints;
 
  // List of tile domain
  List<Object> domain = new LinkedList<Object>();

  public Sudoku(int size) {
    // Generate everything
    boardSize = size;
    for (int i=1;i<=boardSize;i++) {
      domain.add(i);
    }
    tiles = new ArrayList<Variable>(boardSize*boardSize);
    constraints = new ArrayList<Constraint>(boardSize*3);
    variables();
    constraints();
  }

  /**
   * Implements each tile as a variable in a CSP
   */
  public class SudokuTile extends Variable {

    public int tileNumber; // Between 0 and 80

    public SudokuTile(int i) {
      tileNumber = i;
    }
    public String description() {
      return "Tile "+tileNumber+" ("+(tileNumber/boardSize)+","+(tileNumber%boardSize)+")";
    }
    public List<Object> domain() {
      return domain;
    }
  }


  /**
   * Returns a list of variables associated with the problem.
   */
  public List<Variable> variables() {
    if (tiles.size() == 0) {
      for (int i=0;i<boardSize*boardSize;i++) {
        tiles.add(new SudokuTile(i));
      }
    }
    return tiles;
  }

  /**
   * Simple constraint that requires all variables
   * to have a unique value.
   */
  public class AllDiff extends Constraint {
    public List<Variable> variables = new LinkedList<Variable>();

    public boolean satisfied(Assignment asign) {
      boolean[] seen = new boolean[boardSize+1];
      for (Variable v : variables) {
        Integer val = (Integer)asign.getValue(v);
        if (val == null || seen[val])  {
          return false;
        }
        seen[val] = true;
      }
      return true;
    }

    public boolean consistent(Assignment asign) {
      boolean[] seen = new boolean[boardSize+1];
      boolean[] avail = new boolean[boardSize+1];
      int constraintDomain = 0;

      for (Variable v : variables) {
        // Check if this variable adds to the domain of the constraint
        for (Object val : domainValues(asign, v)) {
          if (!avail[(Integer)val]) {
            constraintDomain++;
            avail[(Integer)val] = true;
          }
        }

        // Check for a duplicate value
        Integer val = (Integer)asign.getValue(v);
        if (val != null) {
          if (seen[val])  {
            return false;
          }
          seen[val] = true;
        }
      }

      // Check if there are not enough values
      if (variables.size() > constraintDomain) {
        return false;
      }
      return true;
    }

    public List<Variable> reliesOn() {
      return variables;
    }

    public String description() {
      String desc = "Constraint on {\n";
      for (Variable v : variables) {
        desc += "\t"+v.description()+"\n";
      }
      desc +="}";
      return desc;
    }
  }


  /**
   * Returns a list of constraints associated with the problem.
   */
  public List<Constraint> constraints() {
    if (constraints.size() == 0) {
      // Add the row constraints
      for (int row=0;row<boardSize;row++) {
        AllDiff constr = new AllDiff(); 
        for (int col=0;col<boardSize;col++) {
          constr.variables.add(tiles.get(row*boardSize+col));
        }
        constraints.add(constr);
      }

      // Add the column constraints
      for (int col=0;col<boardSize;col++) {
        AllDiff constr = new AllDiff();
        for (int row=0;row<boardSize;row++) {
          constr.variables.add(tiles.get(row*boardSize+col));
        }
        constraints.add(constr);
      }

      // Add the "grid" constraints
      int numGrids = (int)Math.round(Math.sqrt(boardSize));
      for (int gridRow=0;gridRow<numGrids;gridRow++) {
        for (int gridCol=0;gridCol<numGrids;gridCol++) {
          AllDiff constr = new AllDiff();

          // This is disgusting...
          for (int row=0;row<numGrids;row++) {
            for (int col=0;col<numGrids;col++) {
              int piece = (gridRow*numGrids+row)*boardSize + gridCol*numGrids+col;
              constr.variables.add(tiles.get(piece));
            }
          }

          constraints.add(constr);
        }
      }
    }
    return constraints;
  }

  /**
   * Returns a new assignment based on some inferences.
   * We will override the domain of other variables
   */
  public Assignment inference(Assignment assign, Variable v) throws IllegalStateException {
    // Get all the affected constraints
    List<Constraint> constr = variableConstraints(v);

    // Get the assigned value
    Object val = assign.getValue(v);

    // Get all the affected variables
    for (Constraint c : constr) {
      for (Variable rel : c.reliesOn()) {
        // Skip the current variable
        if (rel == v) continue;

        List<Object> domain = domainValues(assign, rel);
        if (domain.contains(val)) {
          domain = new LinkedList<Object>(domain);
          domain.remove(val);
          assign.restrictDomain(rel, domain);
          if (assign.getValue(rel) == null) {
            if (domain.size() == 1) {
              assign = assign.assign(rel, domain.get(0));
            } else if (domain.size() == 0) {
              throw new IllegalStateException("No remaining assignments for variable: "+rel.description());
            }
          }
        }
      }
    }

    return assign;
  }
}

