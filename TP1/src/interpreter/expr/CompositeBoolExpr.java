/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;

/**
 *
 * @author aluno
 */
public class CompositeBoolExpr extends BoolExpr{
    private BoolExpr left;
    private BoolExpr right;
    private BoolOp op;

    public CompositeBoolExpr(BoolExpr left, BoolExpr right, BoolOp op, int line) {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }
    public boolean expr(Instance self, Arguments args){
        if(op == BoolOp.AND){
            if(left.expr(self, args) && right.expr(self, args))
                return true;
        }else{
            if(left.expr(self, args) || right.expr(self, args))
                return true;
        }
        return false;
    }
    
    
}
