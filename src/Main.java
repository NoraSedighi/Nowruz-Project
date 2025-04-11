import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        TodoOperations todoOps = new TodoOperations();

        while(true) {
            System.out.println("----------------------------------");
            System.out.println("Options:\n" + "\tadd task\n" + "\tadd step");
            System.out.println( "\tdelete\n" + "\tupdate task\n" + "\tupdate step");
            System.out.println("\tget task-by-id\n" + "\tget all-tasks\n" + "\tget incomplete-tasks\n" + "\texit");
            System.out.println("----------------------------------");
            System.out.println("Enter your choice: ");
            String input = scn.nextLine().trim();

            try {
                switch (input.toLowerCase()) {
                    case "add task":
                        TodoOperations.addTask(scn);
                        break;

                    case "add step":
                        TodoOperations.addStep(scn);
                        break;

                    case "delete":
                        TodoOperations.handleDelete(scn);
                        break;

                    case "update task":
                        TodoOperations.updateTask(scn);
                        break;

                    case "update step":
                        TodoOperations.updateStep(scn);
                        break;

                    case "get task-by-id":
                        TodoOperations.getTaskById(scn);
                        break;

                    case "get all-tasks":
                        TodoOperations.getAllTasks(scn);
                        break;

                    case "get incomplete-tasks":
                        TodoOperations.getIncompleteTasks(scn);
                        break;

                    case "exit":
                        System.out.println("Exiting program. Goodbye!");
                        scn.close();
                        return;

                    default:
                        System.out.println("Invalid input! Please enter a valid command.");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                scn.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}
