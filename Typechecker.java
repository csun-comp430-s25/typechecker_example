public class Typechecker {
    public static Optional<Type> typeOf(final Exp e, final Map<Variable, Type> env) {
        if (e instanceof VarExp ve) {
            final Variable name = ve.v;
            if (env.containsKey(name)) {
                return Optional.of(env.get(name));
            } else {
                return Optional.empty();
            }
        } else if (e instanceof IntExp) {
            return Optional.of(new IntType());
        } else if (e instanceof TrueExp || e instanceof FalseExp) {
            return Optional.of(new BoolType());
        } else if (e instanceof BinOpExp boe) {
            Optional<Type> leftType = typeOf(boe.left, env);
            if (leftType.isPresent()) {
                Optional<Type> rightType = typeOf(boe.right, env);
                if (rightType.isPresent()) {
                    // int + int = int
                    // (leftType.get, boe.op, rightType.get) match {
                    //   case (IntType, PlusOp, IntType) => IntType
                    //   case (IntType, LessThanOp, IntType) => BoolType
                    //   case (BoolType, AndOp, BoolType) => BoolType
                    // }
                    if (boe.op instanceof PlusOp &&
                        leftType.get() instanceof IntType &&
                        rightType.get() instanceof IntType) {
                        return Optional.of(new IntType());
                    } else if (boe.op instanceof LessThanOp &&
                               leftType.get() instanceof IntType &&
                               rightType.get() instanceof IntType) { // int < int = bool
                        return Optional.of(new BoolType());
                    } else if (boe.op instanceof AndOp &&
                               leftType.get() instanceof BoolType &&
                               rightType.get() instanceof BoolType) {
                        return Optional.op(new BoolType());
                    } else {
                        return Optional.empty();
                    }
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
