/**
 * Driver class for TicTacToe.java
 *
 * @author Leroy Valencia
 */
import java.util.Scanner;
public class TicTacToeTest {
    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe();
        System.out.println("Ready to play?");
        ttt.play();

        //end play again functionality?
        System.out.print("\nDo you want to play again? (Y/n)\n");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        if(validateInput(input)){
            System.out.println("Loading...");
            TicTacToe ttt1 = new TicTacToe();
            ttt1.play();
        }else{
            System.out.println("Thank you for playing!");
            sc.close();
            System.exit(0);
        }
    }

    /**
     * Validates the input of y to play again. Anything else just quits.
     * @param input the string value that will be validated
     * @return true if the string == y or Y
     *         false anything other than y or Y
     */
    private static boolean validateInput(String input) {
        if(input.equals("Y") || input.equals("y")){
            return true;
        }
        return false;
    }
}
