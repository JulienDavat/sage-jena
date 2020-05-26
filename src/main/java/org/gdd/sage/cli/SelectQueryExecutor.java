package org.gdd.sage.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.jena.query.*;

/**
 * Execute a SPARQL SELECT query and output results in stdin
 * @author Thomas Minier
 */
public class SelectQueryExecutor implements QueryExecutor {
    private OutputStream out;
    private String format;
    private long timeout;

    public SelectQueryExecutor(OutputStream out, String format, long timeout) {
        this.out = out;
        this.format = format;
        this.timeout = timeout;
    }

    @Override
    public void execute(Dataset dataset, Query query) throws TimeoutException, InterruptedException, ExecutionException {

        Duration duration = Duration.ofSeconds(timeout);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> handler = executor.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                try(QueryExecution qexec = QueryExecutionFactory.create(query, dataset)) {
                    ResultSet results = qexec.execSelect();
                    switch (format) {
                        case "raw":
                            results.forEachRemaining(x -> {
                                String raw = String.format("%s\n", x.toString());
                                try {
                                    out.write(raw.getBytes());
                                } catch (IOException e) { }
                            });
                            break;
                        case "xml":
                            ResultSetFormatter.outputAsXML(out, results);
                            break;
                        case "json":
                            ResultSetFormatter.outputAsJSON(out, results);
                            break;
                        case "csv":
                            ResultSetFormatter.outputAsCSV(out, results);
                            break;
                        case "tsv":
                            ResultSetFormatter.outputAsTSV(out, results);
                            break;
                        default:
                            ResultSetFormatter.outputAsSSE(out, results);
                            break;
                    }
                    return null;
                }
            }

        });

        if (query.isSelectType()) {
            try {
                handler.get(duration.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                handler.cancel(true);
                throw e;
            } finally {
                executor.shutdownNow();
            }
        }
    }
}
