package graph_editor.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Algorithme {

    protected IntegerProperty stepNumber;

    public Algorithme(){
        stepNumber = new SimpleIntegerProperty();
    }

    public abstract boolean graphValid();

    public abstract boolean ready();

    public abstract boolean nextStep();

    public abstract boolean previousStep();

    public abstract void reset();


    /*
        STEP NUMBER PROPERTY
     */
    public final void setStepNumber(int i){
        stepNumber.setValue(i);
    }

    public final int getStepNumber(){
        return stepNumber.get();
    }

    public final void incStepNumber(){
        stepNumber.setValue(stepNumber.get()+1);
    }

    public void bindStepNumber(IntegerProperty n){
        stepNumber.bindBidirectional(n);
    }

    public void unbindStepNumber(IntegerProperty n){
        stepNumber.unbindBidirectional(n);
    }

    public final IntegerProperty stepNumBerProperty(){
        return stepNumber;
    }
}
