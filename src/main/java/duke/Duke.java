package duke;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.*;

/**
 * Main class of the Duke chat-bot.
 */
public class Duke {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructor for the Duke class.
     * @param filePath Provides the file location for loading and storing tasks.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Starts the Duke chat-bot.
     */
    public void run() {
        Parser parser = new Parser();
        this.ui.start();
        for (Task t : this.tasks.getTasks()) {
            System.out.println("     " + t.toString());
        }
        while (!(ui.getResponse().equals("bye"))) {
            ui.askForInput();
            parser.parse(ui.getResponse(), this.tasks);
        }
        storage.save(tasks);
        System.out.println("     Sad to see you go! Visit me again soon!");
    }

    public static void main(String[] args) {
        String workingDir = System.getProperty("user.dir");
        Path pathToDuke = Paths.get(workingDir, "data", "duke.txt");
        new Duke(String.valueOf(pathToDuke)).run();
    }
}