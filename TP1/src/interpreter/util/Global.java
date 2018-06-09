package interpreter.util;

import interpreter.value.FunctionValue;
import interpreter.value.InstanceValue;

public class Global extends Memory {

    private static Global global;

    private Global() {
        Instance system = new Instance();
        system.setValue("print", new FunctionValue(new SpecialFunction(FunctionType.PRINT)));
        system.setValue("println", new FunctionValue(new SpecialFunction(FunctionType.PRINTLN)));
        system.setValue("read", new FunctionValue(new SpecialFunction(FunctionType.READ)));
        system.setValue("random", new FunctionValue(new SpecialFunction(FunctionType.RANDOM)));
        system.setValue("get", new FunctionValue(new SpecialFunction(FunctionType.GET)));
        system.setValue("set", new FunctionValue(new SpecialFunction(FunctionType.SET)));
        system.setValue("abort", new FunctionValue(new SpecialFunction(FunctionType.ABORT)));
        system.setValue("type", new FunctionValue(new SpecialFunction(FunctionType.TYPE)));
        system.setValue("lenght", new FunctionValue(new SpecialFunction(FunctionType.LENGHT)));
        system.setValue("substring", new FunctionValue(new SpecialFunction(FunctionType.SUBSTRING)));
        system.setValue("clone", new FunctionValue(new SpecialFunction(FunctionType.CLONE)));
// Add the others.

        this.setValue("system", new InstanceValue(system));
    }

    public static Global getGlobalTable() {
        if (global == null)
            global = new Global();

        return global;
    }
}
