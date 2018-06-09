/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.command;

import interpreter.expr.BoolExpr;
import interpreter.util.Arguments;
import interpreter.util.Instance;

/**
 *
 * @author aluno
 */
public class IfCommand extends Command{
    private BoolExpr Ccond;
    private Command Cthen;
    private Command Celse;
    
    public IfCommand(BoolExpr Ccond, Command Cthen, int line) {
        super(line);
        this.Ccond = Ccond;
        this.Cthen = Cthen;
        this.Celse = Celse;
    }
    
    public IfCommand(BoolExpr Ccond, Command Cthen, Command Celse, int line) {
        super(line);
        this.Ccond = Ccond;
        this.Cthen = Cthen;
        this.Celse = Celse;
    }
    
    public void execute(Instance self, Arguments args){
        boolean value = Ccond.expr(self, args);
        if(value){
            Cthen.execute(self, args);
            if(Celse != null)
                Celse.execute(self, args);
        }
    }
}
