import java.util.*;

public class Main {

  public static void main(String[] args) {
    Sudoku sud = new Sudoku(16);
    List<Variable> vars = sud.variables();
    List<Constraint> constr = sud.constraints();

    Backtrack solve = new Backtrack(sud);
    Assignment solu = solve.solve();

    if (solu == null) {
      System.out.println("Failed to find a solution!");
      System.exit(1);
    }

    System.out.println("Solution:");
    for (Variable v : vars) {
      System.out.println(v.description()+" "+solu.getValue(v));
    }

  }
}
