package ch.IP12.fish.utils;

public class StackUtils {
    public static String getMethodName(){
        return getMethodName(0);
    }

    public static String getMethodName(int skip){
        int skipAmount = 2 + skip;

        return StackWalker.getInstance()
                .walk(s -> s.skip(skipAmount).findFirst())
                .get()
                .getMethodName();
    }

    public static String getClassName(){
        return getClassName(0);
    }

    public static String getClassName(int skip){
        int skipAmount = 2+skip;

        String[] nameStack = StackWalker.getInstance()
                .walk(s -> s.skip(skipAmount).findFirst())
                .get()
                .getClassName()
                .split("\\.");

        return nameStack[nameStack.length - 1];
    }
}
