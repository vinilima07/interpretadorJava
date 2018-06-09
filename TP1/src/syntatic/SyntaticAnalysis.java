package syntatic;

import interpreter.command.*;
import interpreter.expr.*;
import interpreter.value.*;
import interpreter.util.*;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import lexical.*;


public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() throws IOException {
        Command c = procCode();
        matchToken(TokenType.END_OF_FILE);
        return c;
    }

    private void matchToken(TokenType type) throws IOException {
        //System.out.println("Match token: " + current.type + " == " + type + "?");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }
        System.exit(1);
    }
    
    //<code> ::= { <statement> }
    private CommandsBlock procCode()throws IOException{
        CommandsBlock cb = new CommandsBlock();
        while(current.type == TokenType.IF ||
              current.type == TokenType.WHILE ||
              current.type == TokenType.SYSTEM ||
              current.type == TokenType.SELF ||
              current.type == TokenType.ARGS ||
              current.type == TokenType.NAME){
            Command c = procStatement();
            cb.addCommand(c);
        }
        return cb;
    }
    //<statement> ::= <if> | <while> | <cmd>
    private Command procStatement() throws IOException{
        Command c = null;
        if(current.type == TokenType.IF){
            c = procIf();
        }else if(current.type == TokenType.WHILE){
            c = procWhile();
        }else {
            c = procCmd();
        }
        return c;
    }
    
    //<if> ::= if '(' <boolexpr> ')' '{' <code> '}' [else '{' <code> '}' ]
    private IfCommand procIf() throws IOException{
        int line = lex.getLine();
        IfCommand icm;
        Command cElse;
        Command cThen;
        matchToken(TokenType.IF);
        matchToken(TokenType.OPEN_PAR);
        BoolExpr be = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        cThen = procCode();
        icm = new IfCommand(be, cThen, line);
        matchToken(TokenType.CLOSE_CUR);
        if(current.type == TokenType.ELSE){
            matchToken(TokenType.ELSE);
            matchToken(TokenType.OPEN_CUR);
            cElse = procCode();
            icm = new IfCommand(be, cThen, cElse, line);
            matchToken(TokenType.CLOSE_CUR);
        }
        return icm;
    }
    
    // <while> ::= while '('<boolexpr>')' '{' <code> '}'
    private WhileCommand procWhile() throws IOException{
        int line = lex.getLine();
        matchToken(TokenType.WHILE);
        matchToken(TokenType.OPEN_PAR);
        BoolExpr be = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        Command cmd = procCode();
        matchToken(TokenType.CLOSE_CUR);
        
        return (new WhileCommand(be, cmd, line));
    }
    
    //<cmd> ::= <access> ( <assign> | <call> ) ';'
    private AssignCommand procCmd() throws IOException{
        AccessPath path = procAccess();
        AssignCommand ac = null;
        if(current.type == TokenType.ASSIGN){
            ac = procAssign(path);
        }else if(current.type == TokenType.OPEN_PAR){
            int line = lex.getLine();
            FunctionCallExpr fce = procCall(path);
            ac = new AssignCommand(path, fce, line);
        }else{
            showError();
        }
        matchToken(TokenType.DOT_COMA);
        return ac;
    }
    
    //<access> ::= <var> { '.' <name> }
    private AccessPath procAccess() throws IOException{
        int line = lex.getLine();
        String name = procVar();
        AccessPath path = new AccessPath(name, line);
        while(current.type== TokenType.DOT){
            matchToken(TokenType.DOT);
            name = procName();
            path.addName(name);
        }
        return path;
    }
    //<assign> ::= '=' <rhs>
    private AssignCommand procAssign(AccessPath path) throws IOException{
        int line = lex.getLine();
        matchToken(TokenType.ASSIGN);
        Rhs rhs= procRhs();
        AssignCommand ac = new AssignCommand(path, rhs, line);
        return ac;
    }
    
    //<call> ::= '(' [ <rhs> { ',' <rhs> } ] ')'
    private FunctionCallExpr procCall(AccessPath path) throws IOException{
        FunctionCallExpr fce = new FunctionCallExpr(path, lex.getLine());
        matchToken(TokenType.OPEN_PAR);
        if(current.type == TokenType.FUNCTION 
            ||current.type == TokenType.NUMBER
            || current.type == TokenType.STRING
            || current.type == TokenType.SYSTEM 
            || current.type == TokenType.SELF 
            || current.type == TokenType.ARGS
            || current.type == TokenType.NAME
            || current.type == TokenType.OPEN_PAR){
            Rhs rhs = procRhs();
            fce.addParam(rhs);
            while(current.type == TokenType.COMMA){
                matchToken(TokenType.COMMA);
                rhs = procRhs();
                fce.addParam(rhs);
            }
        }
        matchToken(TokenType.CLOSE_PAR);
        return fce;
    }
    
    //<boolexpr> ::= [ '!' ] <cmpexpr> [ ('&' | '|') <boolexpr> ]
    private BoolExpr procBoolExpr() throws IOException{
        BoolExpr be = null;
        BoolExpr left = null;
        BoolExpr right = null;
        int line = lex.getLine();
        if(current.type == TokenType.NOT){
            matchToken(TokenType.NOT);
            left = new NotBoolExpr(procCmpExpr(), line); 
        }else
            left = procCmpExpr();
        if(current.type == TokenType.AND){
            matchToken(TokenType.AND);
            right = procBoolExpr();
            be = new CompositeBoolExpr(left, right, BoolOp.AND, line);
        }else if(current.type == TokenType.OR){
            matchToken(TokenType.OR);
            right = procBoolExpr();
            be = new CompositeBoolExpr(left, right, BoolOp.OR, line);
        }
        return be;
    }

    //<cmpexpr> ::= <expr> <relop> <expr>
    private SingleBoolExpr procCmpExpr() throws IOException{
        int line = lex.getLine();
        Expr exprL = procExpr();
        RelOp rop = procRelop();
        Expr exprR = procExpr();
        SingleBoolExpr sbe = new SingleBoolExpr(exprL, exprR, rop, line);
        return sbe;
    }
    
    //<relop> ::= '==' | '!=' | '<' | '>' | '<=' | '>='
    private RelOp procRelop() throws IOException{
        if(current.type == TokenType.EQUAL){
            matchToken(TokenType.EQUAL);
            return RelOp.EQUAL;
        }else if(current.type == TokenType.DIFF){
            matchToken(TokenType.DIFF);
            return RelOp.NOTEQUAL;
        }else if(current.type == TokenType.LOWER){
            matchToken(TokenType.LOWER);
            return RelOp.LOWERTHAN;
        }else if(current.type == TokenType.GREATER){
            matchToken(TokenType.GREATER);
            return RelOp.GREATERTHAN;
        }else if(current.type == TokenType.LOWER_Q){
            matchToken(TokenType.LOWER_Q);
            return RelOp.LOWEREQUAL;
        }else if(current.type == TokenType.GREATER_Q){
            matchToken(TokenType.GREATER_Q);
            return RelOp.GREATEREQUAL;
        }else{
            showError();
        }
        return null;
    }
    
    //<rhs> ::= <function> | <expr>
    private Rhs procRhs() throws IOException{
        Rhs rhs = null;
        if(current.type == TokenType.FUNCTION)
            rhs = procFunction();
        else
            rhs = procExpr();
        return rhs;
    }
    
    //<function> ::= function '{' <code> [ return <rhs> ] '}'
    private FunctionRhs procFunction() throws IOException{
        int line = lex.getLine();
        Function funct = null;
        Rhs rhs = null;
        matchToken(TokenType.FUNCTION);
        matchToken(TokenType.OPEN_CUR);
        CommandsBlock cdb = procCode();
        if(current.type == TokenType.RETURN){
            matchToken(TokenType.RETURN);
            rhs = procRhs();
            matchToken(TokenType.DOT_COMA);
            funct = new StandardFunction(cdb, rhs);
        }else
            funct = new StandardFunction(cdb);
        FunctionValue fv = new FunctionValue(funct);
        FunctionRhs frhs= new FunctionRhs(fv, line);
        matchToken(TokenType.CLOSE_CUR);
        return frhs;
    }
    
    //<expr> ::= <term> { ('+' | '-') <term> }
    private Expr procExpr() throws IOException{
        Expr e = procTerm();
        while(current.type == TokenType.ADD || current.type == TokenType.SUB){
            if(current.type == TokenType.ADD){
                matchToken(TokenType.ADD);
            }else{
                matchToken(TokenType.SUB);
            }
            e = procTerm();
        }
        return e;
    }
    
    //<term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() throws IOException{
        Expr e = procFactor();
        while(current.type == TokenType.MULT ||
              current.type == TokenType.DIV ||
              current.type == TokenType.MOD){
              if(current.type == TokenType.MULT){
                  matchToken(TokenType.MULT);
              }else if(current.type == TokenType.DIV){
                  matchToken(TokenType.DIV);
              }else if(current.type == TokenType.MOD){
                  matchToken(TokenType.MOD);
              }
              e = procFactor();
        }
        return e;
    }
    
    //<factor> ::= <number> | <string> | <access> [ <call> ]
    private Expr procFactor() throws IOException{
        Expr e = null;
        if(current.type == TokenType.NUMBER){
            e = procNumber();
        }else if(current.type == TokenType.STRING){
            e = procString();
        }else if(current.type == TokenType.OPEN_PAR){
            matchToken(TokenType.OPEN_PAR);
            e = procExpr();
            matchToken(TokenType.CLOSE_PAR);
        }else{
            int line = lex.getLine();
            AccessPath path = procAccess();
            if (current.type == TokenType.OPEN_PAR)
                e = procCall(path);
            else{
                e = new AccessExpr(path, line);
            }
        }
        return e;
    }

    //<var> ::= system | self | args | <name>
    private String procVar() throws IOException{
        String var = null;
        if(current.type == TokenType.SYSTEM){
            var = current.token;
            matchToken(TokenType.SYSTEM);
        }else if(current.type == TokenType.SELF){
            var = current.token;
            matchToken(TokenType.SELF);
        }else if(current.type == TokenType.ARGS){
            var = current.token;
            matchToken(TokenType.ARGS);
        }else{
            var = procName();
        }
        return var;
    }
    
    //special Procedures
    private ConstExpr procNumber() throws IOException{
        int line = lex.getLine();
        String tmp = current.token;
        matchToken(TokenType.NUMBER);
        int n = Integer.parseInt(tmp);
        IntegerValue iv = new IntegerValue(n);
        ConstExpr ce = new ConstExpr(iv, line);
        return ce;
    }
    
    private ConstExpr procString() throws IOException{
        int line = lex.getLine();
        String tmp = current.token;
        matchToken(TokenType.STRING);
        StringValue iv = new StringValue(tmp);
        ConstExpr ce = new ConstExpr(iv, line);
        return ce;
    }
    
    private String procName() throws IOException{
        String name = current.token;
        matchToken(TokenType.NAME);
        return name;
        //....
    }
}

