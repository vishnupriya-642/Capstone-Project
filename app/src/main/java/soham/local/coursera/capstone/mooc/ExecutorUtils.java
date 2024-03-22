package soham.local.coursera.capstone.mooc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
This class is used to perform all the operation needed to be done through a Separate Thread.
 */
public class ExecutorUtils {
    final public static ExecutorService executor = Executors.newSingleThreadExecutor();
}
