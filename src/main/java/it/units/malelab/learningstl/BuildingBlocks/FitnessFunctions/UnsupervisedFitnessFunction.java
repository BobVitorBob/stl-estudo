package it.units.malelab.learningstl.BuildingBlocks.FitnessFunctions;

import it.units.malelab.learningstl.TreeNodes.AbstractTreeNode;
import it.units.malelab.learningstl.BuildingBlocks.SignalBuilders.UnsupervisedSignalBuilder;
import eu.quanticol.moonlight.signal.Signal;
import it.units.malelab.learningstl.LocalSearch.LocalSearch;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UnsupervisedFitnessFunction extends AbstractFitnessFunction<Signal<Map<String, Double>>> {

    private final List<Signal<Map<String, Double>>> signals;

    private List<String> necessaryVariables;

    public UnsupervisedFitnessFunction(String path, boolean localSearch, List<String> necessaryVariables) throws IOException {
        super(localSearch);
        this.signalBuilder = new UnsupervisedSignalBuilder();
        this.necessaryVariables = necessaryVariables;
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
            if (!new HashSet<>(monitor.getVariables().stream().map(vars -> vars[0]).collect(Collectors.toList())).containsAll(this.necessaryVariables)) {
                count += PENALTY_VALUE_MISSING_VALUE;
            } else {
                Signal<Double> signalEval = monitor.getOperator().apply(s).monitor(s);
                double avg = signalEval.reduce((pair, sum) -> {
                   if (monitor.getStart() > pair.getFirst() || monitor.getEnd() < pair.getFirst()){
                       return sum + PENALTY_VALUE_MODAL_OUT_OF_RANGE;
                   }
                   return sum + Math.abs(pair.getSecond());
                }, 0.0) / signalEval.end();
                count += avg;
            }
        }
        if (count == 0) {
            int a = 2;
        }
        return count / this.signals.size();
    }
    @Override
    public Double apply(AbstractTreeNode monitor, double[] params) {
        monitor.propagateParameters(params);
        return apply(monitor);
    }

}
