/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;
import interpreter.util.InterpreterError;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

/**
 *
 * @author aluno
 */
public class SingleBoolExpr extends BoolExpr{
    private Expr left;
    private Expr right;
    private RelOp op;

    public SingleBoolExpr(Expr left, Expr right, RelOp op, int line) {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }
    
    public boolean expr(Instance self, Arguments args){
        Value<?> valueL = left.rhs(self, args);
        Value<?> valueR = right.rhs(self, args);
        if ((valueL instanceof IntegerValue) && (valueR instanceof IntegerValue)){
            IntegerValue vL = (IntegerValue) valueL;
            IntegerValue vR = (IntegerValue) valueR;
            int left = vL.value();
            int right = vR.value();

            if(op == RelOp.EQUAL){
                if(left == right)
                    return true;
            }else if(op == RelOp.NOTEQUAL){
                if(left != right)
                    return true;
            }else if(op == RelOp.LOWERTHAN){
                if(left < right)
                    return true;
            }else if(op == RelOp.LOWEREQUAL){
                if(left <= right)
                    return true;
            }else if(op == RelOp.GREATERTHAN){
                if(left > right)
                    return true;
            }else if(op == RelOp.GREATEREQUAL){
                if(left >= right)
                    return true;
            }
            return false;
        }else if((valueL instanceof StringValue) && (valueR instanceof StringValue) ){
            StringValue vL = (StringValue) valueL;
            StringValue vR = (StringValue) valueR;
            String left = vL.value();
            String right = vR.value();
            if(op == RelOp.EQUAL){
                if(left.equals(right))
                    return true;
                else return false;
            }
        }
        InterpreterError.abort(this.getLine());
        return false;
    }
}
