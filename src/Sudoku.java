import java.util.*;

/**
 * Implements all the proper interfaces to formulate
 * Sudoku as a Constraint Satisfaction Problem.
 */
public class Sudoku extends CSPProblem {
  /**
   * Implements each tile as a variable in a CSP
   */
  public static class SudokuTile extends Variable {
    public int tileNumber; // Between 0 and 80

    public SudokuTile(int i) {
      tileNumber = i;
    }
    public String description() {
      return "Tile "+tileNumber+" ("+(tileNumber/8)+","+(tileNumber%8)+")";
    }
    public List<Object> domain() {
      List<Object> vals = new LinkedList<Object>();
      for (int i=1;i<10;i++) {
        vals.add(i);
      }
      return vals;
    }
  }

  // List of variable tiles
  List<SudokuTile> tiles = new ArrayList<SudokuTile>(81);

  /**
   * Returns a list of variables associated with the problem.
   */
  public List<Variable> variables() {
    if (tiles.size() == 0) {
      for (int i=0;i<81;i++) {
        tiles.add(new SudokuTile(i));
      }
    }
    return new LinkedList<Variable>(tiles);
  }

  /**
   * Simple constraint that requires all variables
   * to have a unique value.
   */
  public static class AllDiff extends Constraint {
    public List<SudokuTile> variables = new LinkedList<SudokuTile>();

    public boolean satisfied(Assignment asign) {
      HashSet<Integer> values = new HashSet<Integer>(9);
      for (SudokuTile v : variables) {
        Integer val = (Integer)asign.getValue(v);
        if (val != null && values.contains(val)) return false;
        values.add(val);
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

  // List of all constraints
  List<AllDiff> constraints = new ArrayList<AllDiff>(27);

  /**
   * Returns a list of constraints associated with the problem.
   */
  public List<Constraint> constraints() {
    if (constraints.size() == 0) {
      // Add the row constraints
      for (int row=0;row<9;row++) {
        AllDiff constr = new AllDiff(); 
        for (int col=0;col<9;col++) {
          constr.variables.add(tiles.get(row*9+col));
        }
        constraints.add(constr);
      }

      // Add the column constraints
      for (int col=0;col<9;col++) {
        AllDiff constr = new AllDiff();
        for (int row=0;row<9;row++) {
          constr.variables.add(tiles.get(row*9+col));
        }
        constraints.add(constr);
      }

      // Add the "grid" constraints
      for (int gridRow=0;gridRow<3;gridRow++) {
        for (int gridCol=0;gridCol<3;gridCol++) {
          AllDiff constr = new AllDiff();

          // This is disgusting...
          for (int row=0;row<3;row++) {
            for (int col=0;col<3;col++) {
              int piece = (gridRow*3+row)*8 + gridCol+col;
              constr.variables.add(tiles.get(piece));
            }
          }

          constraints.add(constr);
        }
      }
    }
    return new LinkedList<Constraint>(constraints);
  }
}

