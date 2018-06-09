package lexical;

import java.util.Map;
import java.util.HashMap;

class SymbolTable {

    private Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();
        
        // symbols
        st.put("{", TokenType.OPEN_CUR);
        st.put("}", TokenType.CLOSE_CUR);
        st.put(";", TokenType.DOT_COMA);
        st.put(".", TokenType.DOT);
        st.put("=", TokenType.ASSIGN);
        st.put("(", TokenType.OPEN_PAR);
        st.put(")", TokenType.CLOSE_PAR);
        st.put(",", TokenType.COMMA);
        st.put("//", TokenType.COMMENT);

        // keywords
        st.put("if", TokenType.IF);
        st.put("else", TokenType.ELSE);
        st.put("while", TokenType.WHILE);
        st.put("function", TokenType.FUNCTION);
        st.put("return", TokenType.RETURN);
        st.put("system", TokenType.SYSTEM);
        st.put("self", TokenType.SELF);
        st.put("args", TokenType.ARGS);
        

        // operators
        st.put("!", TokenType.NOT);
        st.put("&", TokenType.AND);
        st.put("|", TokenType.OR);
        st.put("==", TokenType.EQUAL);
        st.put("!=", TokenType.DIFF);
        st.put("<", TokenType.LOWER);
        st.put(">", TokenType.GREATER);
        st.put("<=", TokenType.LOWER_Q);
        st.put(">=", TokenType.GREATER_Q);
        
        //arith operators
        st.put("+", TokenType.ADD);
        st.put("-", TokenType.SUB);
        st.put("*", TokenType.MULT);
        st.put("/", TokenType.DIV);
        st.put("%", TokenType.MOD);
        
        //others
    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ?
            st.get(token) : TokenType.INVALID_TOKEN;
    }
}
