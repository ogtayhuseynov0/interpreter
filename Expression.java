public abstract class Expression {
    Expression part1, part2;

    public void evaluate(){

    }

    public static class PlusExp extends Expression{

    }
    public static class SubExp extends Expression{

    }
    public static class DivExp extends Expression{

    }
    public static class ProdExp extends Expression{

    }
    public static class IntExp extends Expression{
        public Integer integer;

        public IntExp(int exp){
            this.integer=exp;
        }

    }
    public static class BoolExp extends Expression{
        public boolean aBoolean;

        public BoolExp(boolean exp){
            this.aBoolean=exp;
        }

    }
    public static class GreaterThan extends Expression{

    }
    public static class LessThan extends Expression{

    }
    public static class GreaterEqualThan extends Expression{

    }
    public static class LessEqualThan extends Expression{

    }
    public static class Dequality extends Expression{

    }
    public static class NoEquality extends Expression{

    }
    public static class Conjuction extends Expression{

    }
    public static class Disjuncton extends Expression{

    }
    public static class StringExp extends Expression{
        public String string;
        public StringExp(String s){
            this.string=s;
        }
    }
    public static class IdentExp extends Expression{
        public String ident;

        public IdentExp(String exp){
            this.ident=exp;
        }

    }

}
