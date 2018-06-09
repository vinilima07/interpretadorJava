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
public class NotBoolExpr extends BoolExpr{
    private BoolExpr expr;

    public NotBoolExpr(BoolExpr expr, int line) {
        super(line);
        this.expr = expr;
    }
    public boolean expr(Instance self, Arguments args){
        return !(expr.expr(self, args));
    }
    
}
