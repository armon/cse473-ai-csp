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
  List<SudokuTile> tiles;

  // List of all constraints
  List<AllDiff> constraints;

  public Sudoku(int size) {
    // Generate everything
    boardSize = size;
    tiles = new ArrayList<SudokuTile>(boardSize*boardSize);
    constraints = new ArrayList<AllDiff>(boardSize*3);
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
      List<Object> vals = new LinkedList<Object>();
      for (int i=1;i<=boardSize;i++) {
        vals.add(i);
      }
      return vals;
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
    return new LinkedList<Variable>(tiles);
  }

  /**
   * Simple constraint that requires all variables
   * to have a unique value.
   */
  public class AllDiff extends Constraint {
    public List<SudokuTile> variables = new LinkedList<SudokuTile>();

    public boolean satisfied(Assignment asign) {
      HashSet<Integer> values = new HashSet<Integer>(boardSize);
      for (SudokuTile v : variables) {
        Integer val = (Integer)asign.getValue(v);
        if (val == null || values.contains(val))  {
          return false;
        }
        values.add(val);
      }
      return true;
    }

    public boolean consistent(Assignment asign) {
      HashSet<Integer> values = new HashSet<Integer>(boardSize);
      for (SudokuTile v : variables) {
        Integer val = (Integer)asign.getValue(v);
        if (val != null) {
          if (values.contains(val))  {
            return false;
          }
          values.add(val);
        }
      }
      return true;
    }

    public List<Variable> reliesOn() {
      return new LinkedList<Variable>(variables);
    }

    public String description() {
      String desc = "Constraint on {\n";
      for (SudokuTile v : variables) {
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
    return new LinkedList<Constraint>(constraints);
  }

  /**
   * Returns a new assignment based on some inferences.
   * We will override the domain of other variables
   */
  public Assignment inference(Assignment assign, Variable v) {
    // Get all the affected constraints
    List<Constraint> constr = variableConstraints(v);

    // Get all the affected variables
    Set<Variable> vars = new HashSet<Variable>();
    for (Constraint c : constr) {
      vars.addAll(c.reliesOn());
    }

    // Get the assigned value
    Object val = assign.getValue(v);

    // Retrict the domain of all other variables
    for (Variable rel : vars) {
      List<Object> domain = new LinkedList<Object>(domainValues(assign, rel));
      int pre = domain.size();
      domain.remove(val);
      assign.restrictDomain(rel, domain);
    }

    return assign;
  }
}

