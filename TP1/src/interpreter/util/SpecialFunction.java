package interpreter.util;

import interpreter.value.FunctionValue;
import interpreter.value.InstanceValue;
import java.util.Scanner;
import interpreter.value.Value;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import java.util.Random;

public class SpecialFunction extends Function {

    private FunctionType type;
    private Scanner in;

    public SpecialFunction(FunctionType type) {
        this.type = type;
        this.in = new Scanner(System.in);
    }

    public Value<?> call(Instance self, Arguments args) {
        Value<?> v;

        switch (type) {
            case PRINT:
                v = this.print(args);
                break;
            case PRINTLN:
                v = this.println(args);
                break;
            case READ:
                v = this.read(args);
                break;
            case RANDOM:
                v = this.random(args);
                break;
            case GET:
                v = this.get(args);
                break;
            case SET:
                v = this.set(args);
                break;
            case ABORT:
                v = this.abort(args);
                break;
            case TYPE:
                v = this.type(args);
                break;
            case LENGHT:
                v = this.substring(args);
                break;
            case SUBSTRING:
                v = this.lenght(args);
                break;
            case CLONE:
                v = this.clone(args);
                break;
            default:
                throw new RuntimeException("FIXME: implement me!");
        }

        return v;
    }

    private Value<?> print(Arguments args) {//porque recebe um argumento?
        if (args.contains("arg1")) {

            Value<?> v = args.getValue("arg1");
            if (v instanceof IntegerValue) {
                IntegerValue iv = (IntegerValue) v;
                System.out.print(v.value());
            } else if (v instanceof StringValue) {
                StringValue sv = (StringValue) v;
                System.out.print(sv.value());
            }else{
                InterpreterError.abort("fodeu");;
            } 
            
        }

        return IntegerValue.Zero;
    }

    private Value<?> println(Arguments args) {
        Value<?> v = print(args);
        System.out.println();
        return v;
    }

    private Value<?> read(Arguments args) {
        // Print the argument.
        this.print(args);

        String str = in.nextLine();
        try {
           int n = Integer.parseInt(str);
           IntegerValue iv = new IntegerValue(n);
           return iv;
        } catch (Exception e) {
           StringValue sv = new StringValue(str);
           return sv;
        }
    }
    private Value<?> random(Arguments args){
        
        if (!args.contains("arg1"))
            InterpreterError.abort("system.random: primeiro argumento inexistente");
        if (!args.contains("arg2"))
            InterpreterError.abort("system.random: segundo argumento inexistente");

        Value<?> n = args.getValue("arg1");
        Value<?> m = args.getValue("arg2");
            
        if (!(n instanceof IntegerValue))
            InterpreterError.abort("system.random: primeiro argumento não é inteiro");
	if (!(m instanceof IntegerValue))
            InterpreterError.abort("system.random: segundo argumento não é inteiro");

        IntegerValue ivN = (IntegerValue)n;
        IntegerValue ivM = (IntegerValue)m;
        int i = ivN.value();//int recebendo integer?
        int j = ivM.value();
        Random r = new Random();
        i = i+r.nextInt(j);
                
        return (new IntegerValue(i));
    }
    
    private Value<?> get(Arguments args){
        if (args.contains("arg1") && args.contains("arg2")){
            Object obj = args.getValue("arg1");
            StringValue name = (StringValue)args.getValue("arg2");
            
            // @TODO:
            return name;
        }
        return IntegerValue.Zero;
    }
    
    private Value<?> set(Arguments args){
        if (args.contains("arg1") && args.contains("arg2") && args.contains("arg3")){
            Object obj = args.getValue("arg1");
            StringValue name = (StringValue)args.getValue("arg2");
            Value<?> value = args.getValue("arg3");
        }
        return args.getValue("arg1");
    }
    private Value<?> abort(Arguments args){
        if (args.contains("arg1")) {
            Value<?> v = args.getValue("arg1");
            if (v instanceof IntegerValue) {
                IntegerValue iv = (IntegerValue) v;
                System.out.print(v.value());
            } else if (v instanceof StringValue) {
                StringValue sv = (StringValue) v;
                    System.out.print(sv.value());
            }
        }
        InterpreterError.abort("exit");
        return IntegerValue.Zero;
    }
    private Value<?> type(Arguments args){
        //Quer retornar uma msg com o tipo, ou os tipos IntegerValue, StringValue
        Value<?> value = args.getValue("arg1");
        if(value instanceof StringValue)
            return (new StringValue("string"));
        else if(value instanceof IntegerValue)
            return (new StringValue("integer"));
        else if(value instanceof InstanceValue)
            return (new StringValue("instance"));
        else if(value instanceof InstanceValue)//tirar esse e o null
            return (new StringValue("function"));
        return null;
    }
    private Value<?> lenght(Arguments args){
        if (args.contains("arg1")) {
            Value<?> v = args.getValue("arg1");
            if (v instanceof StringValue){ 
                StringValue iv = (StringValue) v;
                String s = iv.value();
                return (new IntegerValue(s.length()));
            }
        }
        return IntegerValue.Zero;
    }
    
    private Value<?> substring(Arguments args){
        if (args.contains("arg1") && args.contains("arg2") && (args.contains("arg3"))) {
            Value<?> valueS = args.getValue("arg1");
            Value<?> valueI = args.getValue("arg2");
            Value<?> valueF = args.getValue("arg3");
            //i é um caractere ou uma posição????????
            if((valueS instanceof StringValue) && (valueI instanceof IntegerValue) && (valueF instanceof IntegerValue)){
                StringValue strSV = (StringValue)valueS;
                IntegerValue iIV = (IntegerValue)valueI;
                IntegerValue fIV = (IntegerValue)valueF;
                String str = strSV.value();
                int i = iIV.value();
                int f = fIV.value();
                return (new StringValue(str.substring(i, f)));
            }
        }
        return null;
    }
    private Value<?> clone(Arguments args){
        //eu só consigo retornarnar Instance?
        if (!args.contains("arg1"))
            InterpreterError.abort("clone: primeiro argumento inexistente");
        
        Value<?> value = args.getValue("arg1");
        
        if(value instanceof InstanceValue){
            Instance i = ((InstanceValue) value).value();
            return (new InstanceValue(i.dup()));
        }else
            InterpreterError.abort("clone: o argumento nao e do tipo instance");
            return null;
    }
}
