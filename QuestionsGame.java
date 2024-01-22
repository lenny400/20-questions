 // This class represents a guessing game similar to 20 Questions where the user picks an
// object to guess and the computer tries to guess what the object is by asking yes/no
// questions. After asking a series of yes/no questions, the computer makes a guess. If
// the guess is correct, the computer wins; if not then the computer loses and the user's
// object gets added to the database so that it can guess it next game. Yes/no guesses are
// case-insensitive.

import java.util.*;
import java.io.*;

public class QuestionsGame {
    private QuestionNode overallRoot;
    private Scanner console;

    private static class QuestionNode {
        public String text;
        public QuestionNode left;
        public QuestionNode right;

        // Constructs a new leaf QuestionNode, which represents an answer.
        // Parameters:
        //    String text -- the data that the node contains.
        // Exceptions:
        //    none
        // Returns:
        //    none
        public QuestionNode(String text) {
            this.text = text;
            this.left = null;
            this.right = null;
        }

        // Constructs a new branch QuestionNode with references to left and right nodes, which
        // represents a question.
        // Parameters:
        //    String text -- the data that the node contains.
        //    QuestionNode left -- reference to left leaf QuestionNode.
        //    QuestionNode right -- reference to right leaf QuestionNode.
        // Exceptions:
        //    none
        // Returns:
        //    none
        public QuestionNode(String text, QuestionNode left, QuestionNode right) {
            this.text = text;
            this.left = left;
            this.right = right;
        }
    }
    
    // Constructs a new game of 20 Questions.
    public QuestionsGame() {
        console = new Scanner(System.in);
        overallRoot = new QuestionNode("computer");
    }

    // Reads and replaces current tree with the tree that is passed in the parameter.
    // Parameters:
    //    Scanner input -- the tree that is replacing the current tree.
    // Exceptions:
    //    none
    // Returns:
    //    none    
    public void read(Scanner input) {
        overallRoot = readTree(input);
    }
    
    // Stores the current tree and writes to an output file, also is used to play another game
    // with the same question tree.
    // Parameters:
    //    PrintStream output -- the output file that contains the tree with questions and answers.
    // Exceptions:
    //    none
    // Returns:
    //    none    
    public void write(PrintStream output) {
        write(output, overallRoot);
    }

    // Asks user questions with current question tree and makes one guess. Prints a message
    // saying the computer wins if the computer guesses correctly. If the computer doesn't
    // guess correctly, it will prompt the user for the name of the object they were thinking
    // of, a question related to that object, and whether the answer to that question is yes
    // or no.
    // Parameters:
    //    none
    // Exceptions:
    //    none
    // Returns:
    //    none
    public void askQuestions() {
        overallRoot = askQuestions(overallRoot);
    }

    // Do not modify this method in any way
    // post: asks the user a question, forcing an answer of "y" or "n";
    //       returns true if the answer was yes, returns false otherwise
    private boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }

    // Reads the input file from the Scanner. Returns a leaf node if the line in the file starts
    // with an answer, returns a branch node if the line starts with a question.
    // Parameters:
    //    Scanner input -- the text file containing questions and answers that is being scanned.
    // Exceptions:
    //    none
    // Returns:
    //    QuestionNode -- returns leaf QuestionNode with an answer as data or a branch
    //                    QuestionNode with a question as data and references to left and right
    //                    children nodes.
    private QuestionNode readTree(Scanner input) {
        String qOrA = input.nextLine();
        if (qOrA.equals("A:")) {
            return new QuestionNode(input.nextLine());
        } else {
            return new QuestionNode(input.nextLine(), readTree(input), readTree(input));
        }
    }

    // Checks if node passed in parameter contains a question or answer and writes down to
    // output file accordingly.
    // Parameters:
    //    PrintStream output -- the output file that is generated from the tree containing the
    //                          questions and answers.
    //    QuestionNode node -- the current QuestionNode that is being checked to see if it's a
    //                         leaf node or branch node.
    // Exceptions:
    //    none
    // Returns:
    //    none 
    private void write(PrintStream output, QuestionNode node) {
        if (isLeaf(node)) {
            output.println("A:");
            output.println(node.text);
        } else {
            output.println("Q:");
            output.println(node.text);
            write(output, node.left);
            write(output, node.right);
        }
    }

    // If the current QuestionNode is a leaf node, the computer makes a guess as to what the
    // user's object is. If the current QuestionNode is a branch node, the computer asks the
    // question.
    // Parameters:
    //    QuestionNode node -- the current QuestionNode that is being checked.
    // Exceptions:
    //    none
    // Returns:
    //    QuestionNode -- returns a question tree containing all of the QuestionNodes referencing
    //                    each other.
    private QuestionNode askQuestions(QuestionNode node) {
        if (isLeaf(node)) {
            if (yesTo("Would your object happen to be " + node.text + "?")) {
                System.out.println("Great, I got it right!");
            } else {
                System.out.print("What is the name of your object? ");
                String name = console.nextLine();
                QuestionNode newNode = new QuestionNode(name);
                System.out.println("Please give me a yes/no question that");
                System.out.println("distinguishes between your object");
                System.out.print("and mine--> ");
                String question = console.nextLine();
                if (yesTo("And what is the answer for your object?")) {
                    node = new QuestionNode(question, newNode, node);
                } else {
                    node = new QuestionNode(question, node, newNode);
                }
            }
        } else {
            if (yesTo(node.text)) {
                node.left = askQuestions(node.left);
            } else {
                node.right = askQuestions(node.right);
            }
        }
        return node;
    }

    // Checks to see if the current QuestionNode is a leaf node.
    // Parameters:
    //    QuestionNode node -- the current QuestionNode that is being checked.
    // Exceptions:
    //    none
    // Returns:
    //    boolean -- true if both left and right nodes of current node are null, false if not.
    private boolean isLeaf(QuestionNode node) {
        return node.left == null && node.right == null;
    }
}
