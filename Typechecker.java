public class Typechecker {
    public static Type typeOf(final Exp e, final Map<Variable, Type> env) throws TypeErrorException {
        if (e instanceof VarExp ve) {
            final Variable name = ve.v;
            if (env.containsKey(name)) {
                return env.get(name);
            } else {
                throw new TypeErrorException("Variable not in scope: " + name);
            }
        } else if (e instanceof IntExp) {
            return new IntType();
        } else if (e instanceof TrueExp || e instanceof FalseExp) {
            return new BoolType();
        } else if (e instanceof BinOpExp boe) {
            Type leftType = typeOf(boe.left, env);
            Type rightType = typeOf(boe.right, env);
            // int + int = int
            // (leftType.get, boe.op, rightType.get) match {
            //   case (IntType, PlusOp, IntType) => IntType
            //   case (IntType, LessThanOp, IntType) => BoolType
            //   case (BoolType, AndOp, BoolType) => BoolType
            // }
            if (boe.op instanceof PlusOp &&
                leftType instanceof IntType &&
                rightType instanceof IntType) {
                return new IntType();
            } else if (boe.op instanceof LessThanOp &&
                       leftType instanceof IntType &&
                       rightType instanceof IntType) { // int < int = bool
                return new BoolType();
            } else if (boe.op instanceof AndOp &&
                       leftType instanceof BoolType &&
                       rightType instanceof BoolType) {
                return new BoolType();
            } else {
                throw new TypeErrorException("Bad operator");
            }
        } else {
            throw new TypeErrorException("Unknown expression");
        }
    }
}
