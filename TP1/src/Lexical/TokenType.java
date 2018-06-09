package lexical;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,
    

    // symbols
    OPEN_CUR,
    CLOSE_CUR,
    DOT_COMA,
    DOT,
    ASSIGN,
    OPEN_PAR,
    CLOSE_PAR,
    COMMA,
    COMMENT,


    // keywords
    IF,
    ELSE,
    WHILE,
    FUNCTION,
    RETURN,
    SYSTEM,
    SELF,
    ARGS,
    
    // operators
    NOT,
    AND,
    OR,
    EQUAL,
    DIFF,
    LOWER,
    GREATER,
    LOWER_Q,
    GREATER_Q,
    
    //arith operators
    ADD,
    SUB,
    MULT,
    DIV,
    MOD,    

    // others
    NUMBER,
    STRING,
    NAME,
    

};
