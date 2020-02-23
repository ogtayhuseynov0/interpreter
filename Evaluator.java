import javax.swing.*;

public class Evaluator {
    JTextPane console;
    Expression root;
    Statement sroot;
    public Evaluator(JTextPane console, Statement root) {
        this.console = console;
        this.sroot = root;
//        this.console.setText(String.valueOf(evaluateStatement(this.sroot)));
        try {
            evaluateStatement(this.sroot);
        } catch (Exception e) {
            console.setText(e.getMessage());
        }
    }
    private Object ecaluateExpressions(Expression exp) throws Exception {
        if(exp instanceof Expression.DivExp){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){
                if ((int)b==0){
                    throw   new Exception("Devide by 0 not accepted");
                }else {
                    return (int) a / (int) b;
                }
            }else {
                throw  new Exception("type error a and b not same");
            }
        }else if (exp instanceof Expression.ProdExp){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a*(int)b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }else if(exp instanceof Expression.SubExp){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a-(int)b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }else if(exp instanceof Expression.PlusExp){
            Object a=  ecaluateExpressions(exp.part1);
            Object b =ecaluateExpressions(exp.part2);
            if (a instanceof String && b instanceof String){
                return a.toString()+b.toString();
            }else if(a instanceof Integer && b instanceof Integer){

                return (int)a+(int)b;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }
        else if(exp instanceof Expression.Conjuction){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if (a instanceof Boolean && b instanceof Boolean){
                return (Boolean)a&&(Boolean)b ;
            }else{
                throw new Exception("Both side of conjuction should be boolean");
            }
        }
        else if(exp instanceof Expression.Disjuncton){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);

            if (a instanceof Boolean && b instanceof Boolean){
                return (Boolean)a||(Boolean)b ;
            }else{
                throw new Exception("Both side of Disjunction should be boolean");
            }
        }
        else if(exp instanceof Expression.Dequality){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if (a instanceof String && b instanceof String){
                return String.valueOf(a).equals(String.valueOf(b)) ;
            }else if(a instanceof Integer && b instanceof Integer){

                return a==b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }

        }else if(exp instanceof Expression.NoEquality){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if (a instanceof String && b instanceof String){
                return !String.valueOf(a).equals(String.valueOf(b)) ;
            }else if(a instanceof Integer && b instanceof Integer){

                return a!=b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }
        else if(exp instanceof Expression.GreaterThan){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a>(int)b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }
        else if(exp instanceof Expression.LessThan){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a<(int)b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }else if(exp instanceof Expression.GreaterEqualThan){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a>=(int)b ;
            }else {
                throw  new Exception("type error &a and &b not same");
            }
        }else if(exp instanceof Expression.LessEqualThan){
            Object a = ecaluateExpressions(exp.part1);
            Object b = ecaluateExpressions(exp.part2);
            if(a instanceof Integer && b instanceof Integer){

                return (int)a<=(int)b ;
            }else {
                throw  new Exception("type error a and b not same");
            }
        }else if(exp instanceof Expression.IntExp){
            return ((Expression.IntExp) exp).integer;
        }else if(exp instanceof Expression.BoolExp){
            return ((Expression.BoolExp) exp).aBoolean;
        }else if(exp instanceof Expression.StringExp){
            return ((Expression.StringExp) exp).string.replaceAll("\"","") ;
        }else if (exp instanceof Expression.IdentExp){
            Expression expression= Memory.variables.get(((Expression.IdentExp) exp).ident);
            Object a = ecaluateExpressions(expression);
            return a;
        } else{
            return 0;
        }
    }

    private Object evaluateStatement(Statement stm) throws Exception {
        if (stm instanceof Statement.IfStatement){
            Object bolExp =ecaluateExpressions(((Statement.IfStatement) stm).expression);
            if (bolExp instanceof Boolean){
               if ((Boolean) bolExp){
                   for (int i = 0; i< ((Statement.IfStatement) stm).statements.size(); i++){
                       evaluateStatement(((Statement.IfStatement) stm).statements.get(i));
                   }
//                   System.out.println("Expresiion is True");
//                   return  "Expresiion is True";
               }else
               {
                   if (((Statement.IfStatement) stm).elseStatement!=null){
                       for (int i = 0; i< ((Statement.IfStatement) stm).elseStatement.statements.size(); i++){
                           evaluateStatement(((Statement.IfStatement) stm).elseStatement.statements.get(i));
                       }
                   }
               }
            }

        }else   if (stm instanceof Statement.WhileStatement){
            Object bolExp =ecaluateExpressions(((Statement.WhileStatement) stm).expression);
            if (bolExp instanceof Boolean){
                while ((Boolean) bolExp){
                    System.out.println(((Statement.WhileStatement) stm).statements.size());
                    for (int i = 0; i< ((Statement.WhileStatement) stm).statements.size(); i++){
                        evaluateStatement(((Statement.WhileStatement) stm).statements.get(i));
                    }
//                   System.out.println("Expresiion is True");
//                   return  "Expresiion is True";
                }
            }

        }

        else if (stm instanceof Statement.PrintStatement) {
            String text= console.getText();
            if (!text.equals("")){
                text=text+"\n";
            }
            this.console.setText(text+String.valueOf(ecaluateExpressions(((Statement.PrintStatement) stm).expression)));

            return String.valueOf(ecaluateExpressions(((Statement.PrintStatement) stm).expression));
        }else if (stm instanceof Statement.AssignmentStm) {
//            TODO typechek for assignment
            if (((Statement.AssignmentStm) stm).checkUniqueness()){
                throw new Exception("Variable with this name already exist");
            }else{

                ((Statement.AssignmentStm) stm).addToMemory();
            }
            return stm;
        }
        return "Null";
    }

}
