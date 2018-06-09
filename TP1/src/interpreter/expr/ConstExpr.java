package interpreter.expr;

import interpreter.util.Instance;
import interpreter.util.Arguments;
import interpreter.util.InterpreterError;
import interpreter.value.FunctionValue;
import interpreter.value.InstanceValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class ConstExpr extends Expr {

    private Value<?> value;

    public ConstExpr(Value<?> value, int line) {
        super(line);
        this.value = value;
    }

    public Value<?> rhs(Instance self, Arguments args) {//???????????
        if(value instanceof StringValue)
            return ((StringValue) value);
        if(value instanceof IntegerValue)
            return ((IntegerValue) value);
        if(value instanceof FunctionValue)
            return ((FunctionValue) value);
        if(value instanceof InstanceValue)
            return ((InstanceValue) value);
       
        return value;
    }
}
