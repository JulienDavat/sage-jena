package org.gdd.sage.cli;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;

/**
 * Abstract query execution logic
 * @author Thomas Minier
 */
public interface QueryExecutor {
    void execute(Dataset dataset, Query query) throws TimeoutException, InterruptedException, ExecutionException;
}
