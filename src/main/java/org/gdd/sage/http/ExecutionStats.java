package org.gdd.sage.http;

import org.apache.jena.ext.com.google.common.math.Stats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An utility class used to record statistic during query execution
 * @author Thomas Minier
 */
public class ExecutionStats {
    private double executionTime;
    private int nbCallsRead;
    private int nbCallsWrite;
    private int dataTransferRead;
    private int dataTransferWrite;
    private List<Double> httpTimesRead;
    private List<Double> httpTimesWrite;
    private List<Double> resumeTimesRead;
    private List<Double> suspendTimesRead;
    private List<Double> resumeTimesWrite;
    private List<Double> suspendTimesWrite;

    public ExecutionStats() {
        executionTime = -1;
        nbCallsRead = 0;
        nbCallsWrite = 0;
        dataTransferRead = 0;
        dataTransferWrite = 0;
        httpTimesRead = new ArrayList<>();
        httpTimesWrite = new ArrayList<>();
        resumeTimesRead = new ArrayList<>();
        suspendTimesRead = new ArrayList<>();
        resumeTimesWrite = new LinkedList<>();
        suspendTimesWrite = new LinkedList<>();
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public int getNbCallsRead() {
        return nbCallsRead;
    }

    public int getNbCallsWrite() {
        return nbCallsWrite;
    }

    public int getDataTransferRead() {
        return dataTransferRead;
    }

    public int getDataTransferWrite() {
        return dataTransferWrite;
    }

    public synchronized Double getMeanHTTPTimesRead() {
        if (httpTimesRead.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(httpTimesRead);
    }

    public synchronized Double getMeanHTTPTimesWrite() {
        if (httpTimesWrite.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(httpTimesWrite);
    }

    public synchronized Double getMeanResumeTimeRead() {
        if (resumeTimesRead.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(resumeTimesRead);
    }

    public synchronized Double getMeanSuspendTimeRead() {
        if (suspendTimesRead.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(suspendTimesRead);
    }

    public synchronized Double getMeanResumeTimeWrite() {
        if (resumeTimesWrite.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(resumeTimesWrite);
    }

    public synchronized Double getMeanSuspendTimeWrite() {
        if (suspendTimesWrite.isEmpty()) {
            return 0.0;
        }
        return Stats.meanOf(suspendTimesWrite);
    }

    public synchronized void startTimer() {
        executionTime = System.nanoTime();
    }

    public synchronized void stopTimer() {
        double endTime = System.nanoTime();
        executionTime = (endTime - executionTime) / 1e9;
    }

    public synchronized void reportDataTransferRead(int bytes) {
        dataTransferRead += bytes;
    }

    public synchronized void reportDataTransferWrite(int bytes) {
        dataTransferWrite += bytes;
    }

    public synchronized void reportHTTPQueryRead(double execTime) {
        nbCallsRead++;
        httpTimesRead.add(execTime);
    }

    public synchronized void reportHTTPQueryWrite(double execTime) {
        nbCallsWrite++;
        httpTimesWrite.add(execTime);
    }

    public synchronized void reportOverheadRead(double resumeTime, double suspendTime) {
        resumeTimesRead.add(resumeTime);
        suspendTimesRead.add(suspendTime);
    }

    public synchronized void reportOverheadWrite(double resumeTime, double suspendTime) {
        resumeTimesWrite.add(resumeTime);
        suspendTimesWrite.add(suspendTime);
    }
}
