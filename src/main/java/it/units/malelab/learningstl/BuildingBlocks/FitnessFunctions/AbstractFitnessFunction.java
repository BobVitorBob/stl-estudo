package it.units.malelab.learningstl.BuildingBlocks.FitnessFunctions;

import it.units.malelab.learningstl.TreeNodes.AbstractTreeNode;
import it.units.malelab.learningstl.BuildingBlocks.SignalBuilders.SignalBuilder;
import eu.quanticol.moonlight.signal.Signal;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public abstract class AbstractFitnessFunction<T> implements Function<AbstractTreeNode, Double> {

    public final static double PENALTY_VALUE_MISS = 1.0;
    public final static double PENALTY_VALUE_MISSING_VALUE = 1.0;
    public final static double PENALTY_VALUE_MODAL_OUT_OF_RANGE = 100;
    protected SignalBuilder<T> signalBuilder;
    protected final boolean isLocalSearch;

    public AbstractFitnessFunction(boolean localSearch) {
        this.isLocalSearch = localSearch;
    }

    public SignalBuilder<T> getSignalBuilder() {return this.signalBuilder;}

    public List<T> getPositiveTraining() {
        return null;
    }

    public List<T> getNegativeTraining() {
        return null;
    }

    public List<T> getPositiveTest() {
        return null;
    }

    public List<T> getNegativeTest() {
        return null;
    }

    public double monitorSignal(Signal<Map<String, Double>> signal, AbstractTreeNode solution, boolean isNegative) {
        int a = solution.getNecessaryLength();
        int b = signal.size();
        if (signal.size() <= solution.getNecessaryLength()) {
            return - PENALTY_VALUE_MODAL_OUT_OF_RANGE;
        }
        double temp = solution.getOperator().apply(signal).monitor(signal).valueAt(signal.end());
        return (isNegative) ? - temp : temp;
    }

    public abstract Double apply(AbstractTreeNode monitor, double[] params);
}
