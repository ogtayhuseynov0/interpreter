import java.util.ArrayList;
import java.util.List;

public abstract class Statement {


    public static class AssignmentStm extends Statement{
            Lexer.TokenType expressionType;
            Expression.IdentExp identExp;
            Expression expression;
            public void addToMemory(){
                Memory.variables.put(identExp.ident,expression);
            }
            public boolean checkUniqueness(){
                return Memory.variables.containsKey(identExp.ident);
            }
    }
    public static class ReAssigmentStm extends Statement{

    }
    public static class IfStatement extends Statement{
        Expression expression;
        ArrayList<Statement> statements = new ArrayList<>();
        ElseStatement elseStatement=null;
        public void addStm(Statement stm){
            this.statements.add(stm);
        }
    }
    public static class ForStatement extends Statement{
        Expression.IntExp intExp;
        Expression.BoolExp expression;
        Expression expressionOp;
        ArrayList<Statement> statements = new ArrayList<>();
        public void addStm(Statement stm){
            this.statements.add(stm);
        }
    }
    public static class WhileStatement extends Statement{
        Expression expression;
        ArrayList<Statement> statements = new ArrayList<>();
        public void addStm(Statement stm){
            this.statements.add(stm);
        }
    }
    public static class ElseStatement extends Statement{
        ArrayList<Statement> statements = new ArrayList<>();

        public void addStm(Statement stm){
            this.statements.add(stm);
        }
    }
    public static class PrintStatement extends Statement{
        Expression expression;
    }
    public static class ErrorStatement extends Statement{
        String errorS;

        public ErrorStatement(String errorS) {
            this.errorS = errorS;
        }
    }
}
