class Problem {
    public static void main(String[] args) {
        boolean modeExists = false;
        for (int i = 0; i < args.length; i++) {
            if ("mode".equals(args[i]) && i % 2 == 0) {
                System.out.println(args[i + 1]);
                modeExists = true;
            }
        }

        if (!modeExists) {
            System.out.println("default");
        }
    }
}