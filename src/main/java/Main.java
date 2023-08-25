import task.Task;

public class Main {

    public static void main(String... args) {
        if (!checkArgs(args)) {
            helpArgs();
            return;
        }

        Task task = new Task();
        task.makeTask(args[0]);
    }

    private static void helpArgs() {
        System.out.println("You should run this program with ONE argument - the name if json file with tickets!");
    }

    private static boolean checkArgs(String[] args) {
        return args.length == 1;
    }
}
