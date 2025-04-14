public class Typechecker {
    public static Map<Variable, Type> typecheck(Stmt stmt, Map<Variable, Type> env) throws TypeErrorException {
        if (stmt instanceof WhileStmt ws) {
            if (typeOf(ws.guard, env) instanceof BoolType) {
                typecheck(ws.body, env);
                return env;
            } else {
                throw new TypeErrorException("Guard non-boolean");
            }
        } else if (stmt instanceof BlockStmt block) {
            Map<Variable, Type> innerEnv = env;
            // for (int index = 0; index < block.stmts.size(); index++) {
            //     Stmt innerStmt = block.get(index);                
            for (Stmt innerStmt : block.stmts) {
                innerEnv = typecheck(innerStmt, innerEnv);
            }
            return env;
        } // TODO: variable declaration   
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
