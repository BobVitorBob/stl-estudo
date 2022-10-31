package it.units.malelab.learningstl.TreeNodes;

import eu.quanticol.moonlight.formula.DoubleDomain;
import eu.quanticol.moonlight.monitoring.temporal.TemporalMonitor;
import it.units.malelab.jgea.representation.tree.Tree;
import it.units.malelab.learningstl.BuildingBlocks.STLFormulaMapper;

import java.util.List;
import java.util.Objects;


public class OrTreeNode extends AbstractTreeNode {

    public OrTreeNode(List<Tree<String>> siblings, List<Tree<String>> ancestors) {
        super();
        this.firstChild = STLFormulaMapper.parseSubTree(siblings.get(0), ancestors);
        this.secondChild = STLFormulaMapper.parseSubTree(siblings.get(1), ancestors);
        this.symbol = "OR";
        this.func = x -> TemporalMonitor.orMonitor(this.firstChild.getOperator().apply(x), new DoubleDomain(),
                this.secondChild.getOperator().apply(x));
    }

    @Override
    public int getNecessaryLength() {
        return Math.max(this.firstChild.getNecessaryLength(), this.secondChild.getNecessaryLength());
    }

    @Override
    public int getStart() {
        return Math.min(this.firstChild.getStart(), this.secondChild.getStart());
    }

    @Override
    public int getEnd() {
        return Math.max(this.firstChild.getEnd(), this.secondChild.getEnd());
    }

    @Override
    public boolean isTemporal() {
        return false;
    }

    @Override
    public void getVariablesAux(List<String[]> temp) {
        this.firstChild.getVariablesAux(temp);
        this.secondChild.getVariablesAux(temp);
    }

    @Override
    public int getNumBounds() {
        return this.firstChild.getNumBounds() + this.secondChild.getNumBounds();
    }

    @Override
    public int[] propagateParametersAux(double[] parameters, int[] idxs) {
        idxs = this.firstChild.propagateParametersAux(parameters, idxs);
        idxs = this.secondChild.propagateParametersAux(parameters, idxs);
        return idxs;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        final AbstractTreeNode other = (AbstractTreeNode) o;
        if (!Objects.equals(this.firstChild, other.getFirstChild())) {
            return false;
        }
        return Objects.equals(this.secondChild, other.getSecondChild());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result += 31 * result + (this.firstChild == null ? 0 : this.firstChild.hashCode());
        result += 31 * result + (this.secondChild == null ? 0 : this.secondChild.hashCode());
        return result;
    }

}
