import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Main {

  /**
   * The problem we are solving.
   */
  public Sudoku sud;

  /**
   * The initial assignment state.
   */
  public Assignment initial = Assignment.blank();

  /**
   * Setup a blank assignment, size 16 sudoku board.
   */
  public Main() {
    sud = new Sudoku(16);
  }

  /**
   * Setup from an input file.
   */
  public Main(String filename) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(filename));
      int boardSize = new Integer(in.readLine());
      sud = new Sudoku(boardSize);

      List<Variable> vars = sud.variables();
      int numInputs = new Integer(in.readLine());

      Pattern p = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\d+)$");

      // Assign all the values
      for (int i=0;i<numInputs;i++) {
        String input = in.readLine();
        Matcher m = p.matcher(input);
        if (m.matches()) {
          int row = new Integer(m.group(1)) - 1;
          int col = new Integer(m.group(2)) - 1;
          Integer val = new Integer(m.group(3));

          // Assign the variable and trigger any inference that is required.
          Variable var = vars.get(row*boardSize+col);
          initial = initial.assign(var, val);
          initial = sud.inference(initial, var);

        } else {
          System.out.println("Bad input on line "+(i+3)+". Ignoring.");
        }
      }

      // Done
      in.close();

    } catch (IOException e) {
      System.out.println("Failed to load input file. Using defaults.");
      initial = Assignment.blank();
      sud = new Sudoku(16);
    }
  }


  public Assignment solve() {
    Backtrack solve = new MRVBacktrack(sud, initial);
    return solve.solve();
  }


  public static void main(String[] args) {
    Main m;
    if (args.length > 0)
      m = new Main(args[0]);
    else
      m = new Main();

    Assignment solu = m.solve();

    if (solu == null) {
      System.out.println("Failed to find a solution!");
      System.exit(1);
    }

    System.out.println("Solution:");
    List<Variable> vars = m.sud.variables();
    for (Variable v : vars) {
      System.out.println(v.description()+" "+solu.getValue(v));
    }

  }
}
