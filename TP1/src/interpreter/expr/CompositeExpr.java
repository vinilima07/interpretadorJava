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
 * @author Vinicius
 */
public class CompositeExpr extends Expr {
    private Expr left;
    private Expr right;
    private CompOp op;

    public CompositeExpr(Expr left, Expr right, CompOp op, int line) {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }
    public Value<?> rhs(Instance self, Arguments args){
        Value<?> valueL = left.rhs(self, args);
        Value<?> valueR = right.rhs(self, args);
        if((valueL instanceof IntegerValue) && (valueR instanceof IntegerValue)){
            IntegerValue vL = (IntegerValue) valueL;
            IntegerValue vR = (IntegerValue) valueR;
            int left = vL.value();
            int right = vR.value();
            switch(op){
                case ADD:
                    return (new IntegerValue(left+right));
                case SUB:
                    return (new IntegerValue(left-right));
                case MUL:
                    return (new IntegerValue(left*right));
                case DIV:
                    return (new IntegerValue(left/right));
                case MOD:
                    return (new IntegerValue(left%right));
                default:
                    break;
            }
        }
        if((valueL instanceof StringValue) && (valueR instanceof IntegerValue)){
           if(op == CompOp.ADD){
               StringValue vL = (StringValue) valueL;
               IntegerValue vR = (IntegerValue) valueR;
               String s = (vL.value() + vR.value());//concatena com inteiro????
               return (new StringValue(s));
            }
        }if((valueL instanceof StringValue) && (valueR instanceof StringValue))
               if(op == CompOp.ADD){
               StringValue vL = (StringValue) valueL;
               StringValue vR = (StringValue) valueR;
               String s = (vL.value() + vR.value());//concatena com string
               return (new StringValue(s));
           }
        InterpreterError.abort(this.getLine());
        return null;
    }
}
