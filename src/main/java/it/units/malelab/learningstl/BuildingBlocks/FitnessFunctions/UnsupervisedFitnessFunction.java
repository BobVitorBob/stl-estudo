package it.units.malelab.learningstl.BuildingBlocks.FitnessFunctions;

import it.units.malelab.learningstl.TreeNodes.AbstractTreeNode;
import it.units.malelab.learningstl.BuildingBlocks.SignalBuilders.UnsupervisedSignalBuilder;
import eu.quanticol.moonlight.signal.Signal;
import it.units.malelab.learningstl.LocalSearch.LocalSearch;

import java.io.IOException;
import java.util.*;


public class UnsupervisedFitnessFunction extends AbstractFitnessFunction<Signal<Map<String, Double>>> {

    private final List<Signal<Map<String, Double>>> signals;

    public UnsupervisedFitnessFunction(String path, boolean localSearch) throws IOException {
        super(localSearch);
        this.signalBuilder = new UnsupervisedSignalBuilder();
        this.signals = this.signalBuilder.parseSignals(path);
    }

    @Override
    public Double apply(AbstractTreeNode monitor) {
        double count = 0.0;
        if (this.isLocalSearch) {
            double[] newParams = LocalSearch.optimize(monitor, this, 1);
            monitor.propagateParameters(newParams);
        }
        for (Signal<Map<String, Double>> s : this.signals) {
            if (s.end() <= monitor.getNecessaryLength()) {
                count += PENALTY_VALUE;
            }
            else {
                Signal<Double> signalEval = monitor.getOperator().apply(s).monitor(s);
                double avg = signalEval.reduce((pair, sum) -> sum + Math.abs(pair.getSecond()), 0.0) / signalEval.end();
                count += avg;
            }
        }
        return count / this.signals.size();
    }
    @Override
    public Double apply(AbstractTreeNode monitor, double[] params) {
        monitor.propagateParameters(params);
        return apply(monitor);
    }

}
