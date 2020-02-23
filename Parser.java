import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.LinkedList;

public class Parser {
    private LinkedList<Lexer.Token> tokens;
    private Lexer.Token lookahead;

    Program program;
    public Parser(LinkedList<Lexer.Token> tokens) {
        this.tokens = tokens;
        lookahead =tokens.getFirst();
    }

    Program parseProg() {
        program = new Program();
        try {
//            System.out.println();
            try{
                program.sroot= ParseStatement();
            }catch (Exception e) {
                System.out.println("Error "+ e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return program;
    }
    private boolean consumeToken() {
        if (tokens.size()>1){

            Lexer.Token tmp =tokens.pop();
            lookahead = tokens.getFirst();
            return false;
        }
        else {
            System.err.println("erroirrrr");
            return true;
        }
    }

    private Lexer.Token llk(Integer k) {
        if (tokens.size() > k) {
            return tokens.get(k);
        }
        return new Lexer.Token(Lexer.TokenType.SEMIC, "", 0);
    }


    private boolean typeCheck(Lexer.Token a, Lexer.TokenType b){
        return a.getType()==b;
    }

    public Statement ParseStatement()  throws Exception{
        return parseStm();
    }

    public Statement parseStm() throws Exception {
        Lexer.Token next =llk(1);
        if (typeCheck(lookahead, Lexer.TokenType.IF) ){
            consumeToken();
            Statement.IfStatement ifs = new Statement.IfStatement();
            if (typeCheck(lookahead, Lexer.TokenType.LEFTTPARAN)){
                consumeToken();
            }else{
                throw new Exception("( token expected");
            }
            ifs.expression=ParseExpression();
            consumeToken();
            if (typeCheck(lookahead, Lexer.TokenType.RIGHTPARAN)){
                consumeToken();
            }else{
                throw new Exception(") token expected");
            }
            if (typeCheck(lookahead, Lexer.TokenType.LEFTTCURR)){
                consumeToken();
            }else{
                throw new Exception("{ token expected");
            }
            while (true){
                Statement st = parseStm();

                if (st==null) break;
                else {
//                    System.out.println(st.toString());
                    ifs.addStm(st);
                }
            }
            if (typeCheck(lookahead, Lexer.TokenType.RIGHTCURR)){
                consumeToken();
            }else{
                throw new Exception("} token expected");
            }
            if (typeCheck(lookahead, Lexer.TokenType.ELSE)){
                consumeToken();
                Statement.ElseStatement elses=new Statement.ElseStatement();
                if (typeCheck(lookahead, Lexer.TokenType.LEFTTCURR)){
                    consumeToken();
                }else{
                    throw new Exception("{ token expected");
                }
                while (true){
                    Statement st2 = parseStm();

                    if (st2==null) break;
                    else {
//                    System.out.println(st.toString());
                        elses.addStm(st2);
                    }
                }
                if (typeCheck(lookahead, Lexer.TokenType.RIGHTCURR)){
                    consumeToken();
                }else throw new Exception("} token expected");
                ifs.elseStatement=elses;
            }
            return ifs;

        }if (typeCheck(lookahead, Lexer.TokenType.WHILE) ){
            consumeToken();
            Statement.WhileStatement ifs = new Statement.WhileStatement();
            if (typeCheck(lookahead, Lexer.TokenType.LEFTTPARAN)){
                consumeToken();
            }else{
                throw new Exception("( token expected");
            }
            ifs.expression=ParseExpression();
            consumeToken();
            if (typeCheck(lookahead, Lexer.TokenType.RIGHTPARAN)){
                consumeToken();
            }else{
                throw new Exception(") token expected");
            }
            if (typeCheck(lookahead, Lexer.TokenType.LEFTTCURR)){
                consumeToken();
            }else{
                throw new Exception("{ token expected");
            }
            while (true){
                Statement st = parseStm();

                if (st==null) break;
                else {
//                    System.out.println(st.toString());
                    ifs.addStm(st);
                }
            }
            if (typeCheck(lookahead, Lexer.TokenType.RIGHTCURR)){
                consumeToken();
            }else{
                throw new Exception("} token expected");
            }
            return ifs;

        }else if(typeCheck(lookahead, Lexer.TokenType.PRINT)){
            Statement.PrintStatement print = new Statement.PrintStatement();
            consumeToken();
            if (typeCheck(lookahead, Lexer.TokenType.LEFTTPARAN)){
                consumeToken();
            }else{
                throw new Exception("( token expected");
            }
            print.expression=ParseExpression();
            consumeToken();
            if (typeCheck(lookahead, Lexer.TokenType.RIGHTPARAN)){
                consumeToken();
            }else{
                throw new Exception(") token expected");
            }
            return print;
        }
        else if(typeCheck(lookahead, Lexer.TokenType.TYPEINT)  && typeCheck(next,Lexer.TokenType.IDENT)){
            Statement.AssignmentStm assignmentStm= new Statement.AssignmentStm();
            assignmentStm.expressionType=lookahead.getType();
            System.out.println(lookahead.getData());
            consumeToken();//type gone
            Expression.IdentExp identExp= new Expression.IdentExp(lookahead.getData());
            System.out.println(lookahead.getData());
            consumeToken();//ident gone
            assignmentStm.identExp=identExp;
            consumeToken();//equality gone
            assignmentStm.expression=ParseExpression();
            System.out.println(assignmentStm.expression.toString());
            consumeToken();
            return assignmentStm;
        }
       return null;
    }

    public Expression ParseExpression() throws Exception {
        Expression exp =parseDisjuctionExp();
//        consumeToken();
        return exp;
    }

    public Expression parseDisjuctionExp() throws Exception{
        Expression el = parseConjuctionExp();
        Lexer.Token next =llk(1);

        while ( typeCheck(next, Lexer.TokenType.DISJ)) {
            if (consumeToken()) break;
            consumeToken();

            Expression sum;
            sum=new Expression.Disjuncton();
//            System.out.println(sum.toString());
            sum.part2 = parseConjuctionExp();
            sum.part1 = el;
            el = sum;
            next =llk(1);
        }
        return el;
    }

    public Expression parseConjuctionExp() throws Exception{
        Expression el = parseEqualityExp();
        Lexer.Token next =llk(1);

        while ( typeCheck(next, Lexer.TokenType.CONJ)) {
            if (consumeToken()) break;
            consumeToken();

            Expression sum;
            sum=new Expression.Conjuction();
//            System.out.println(sum.toString());
            sum.part2 = parseEqualityExp();
            sum.part1 = el;
            el = sum;
            next =llk(1);
        }
        return el;
    }
    public Expression parseEqualityExp() throws Exception{
        Expression el = parseBooleanExps();
        Lexer.Token next =llk(1);

        while ( typeCheck(next, Lexer.TokenType.DEQUALITY) || typeCheck(next, Lexer.TokenType.NEQUALITY)) {
            if (consumeToken()) break;
            consumeToken();

            Expression sum;
            if (typeCheck(next, Lexer.TokenType.DEQUALITY))
                sum= new Expression.Dequality();
            else
                sum=new Expression.NoEquality();
//            System.out.println(sum.toString());
            sum.part2 = parseBooleanExps();
            sum.part1 = el;
            el = sum;
            next =llk(1);
        }
        return el;
    }

    public Expression parseBooleanExps() throws Exception{
        Expression el = parseExpSum();
        Lexer.Token next =llk(1);

        while ( typeCheck(next, Lexer.TokenType.GTHEN) || typeCheck(next, Lexer.TokenType.LTHEN)
                || typeCheck(next, Lexer.TokenType.LETHEN) || typeCheck(next, Lexer.TokenType.GETHEN)) {
            if (consumeToken()) break;
            consumeToken();

            Expression sum = null;
            if (typeCheck(next, Lexer.TokenType.GTHEN))
                sum= new Expression.GreaterThan();
            else if (typeCheck(next, Lexer.TokenType.LTHEN))
                sum=new Expression.LessThan();
            else if (typeCheck(next, Lexer.TokenType.LETHEN))
                sum=new Expression.LessEqualThan();
            else if (typeCheck(next, Lexer.TokenType.GETHEN))
                sum= new Expression.GreaterEqualThan();

//            System.out.println(sum.toString());
            sum.part2 = parseExpSum();
            sum.part1 = el;
            el = sum;
            next =llk(1);
        }
        return el;
    }

    public Expression parseExpSum() throws Exception{
        Expression el = parseExpProd();
        Lexer.Token next =llk(1);

        while ( typeCheck(next, Lexer.TokenType.PLUS) || typeCheck(next, Lexer.TokenType.MINUS)) {
            if (consumeToken()) break;
            consumeToken();

            Expression sum;
            if (typeCheck(next, Lexer.TokenType.PLUS))
                sum= new Expression.PlusExp();
            else
                sum=new Expression.SubExp();
//            System.out.println(sum.toString());
            sum.part2 = parseExpProd();
            sum.part1 = el;
            el = sum;
            next =llk(1);
        }
        return el;
    }

    public Expression parseExpProd() throws Exception{
        Expression el = parseElement();
        Lexer.Token next =llk(1);
        while (typeCheck(next, Lexer.TokenType.MUL) || typeCheck(next, Lexer.TokenType.DIV)){
            if (consumeToken()) break;
            consumeToken();
            Expression mul ;
            if (typeCheck(next, Lexer.TokenType.MUL)) mul=new Expression.ProdExp();
            else mul=new Expression.DivExp();
            mul.part2=parseElement();
            mul.part1=el;
            el=mul;
            next =llk(1);
        }
        return el;
    }

    private Expression parseElement() throws Exception {

        if (typeCheck(lookahead,Lexer.TokenType.INT)){
            return new Expression.IntExp(Integer.parseInt(lookahead.getData()));
        }else if (typeCheck(lookahead,Lexer.TokenType.STRING)){
            return new Expression.StringExp(lookahead.getData());
        }
        else if (typeCheck(lookahead,Lexer.TokenType.TRUE) ||typeCheck(lookahead,Lexer.TokenType.FALSE)  ){
            return new Expression.BoolExp(Boolean.parseBoolean(lookahead.getData()));
        }else if (typeCheck(lookahead,Lexer.TokenType.LEFTTPARAN)) {
            consumeToken();
            Expression exp = ParseExpression();
            consumeToken();
            return exp;
        }else if (typeCheck(lookahead, Lexer.TokenType.IDENT)){
            return new Expression.IdentExp(lookahead.getData());
        }
            return null;
    }

    private boolean checkForSemiColon( Expression expression) {
        if (typeCheck(lookahead,Lexer.TokenType.SEMIC)){
//            expression.evaluate();

            return true;
        }
        return false;
    }



    private boolean CheckFuncCall() {
        return typeCheck(llk(1),Lexer.TokenType.LEFTTPARAN);
    }

}