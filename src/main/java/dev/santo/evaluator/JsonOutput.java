package dev.santo.evaluator;
final class JsonOutput {
    private JsonOutput() { }
    static String success(String expression, double result) {
        return "{\"expression\":" + quote(expression) + ",\"result\":" + result + "}";
    }
    static String failure(String expression, EvaluationException error) {
        return "{\"expression\":" + quote(expression) + ",\"error\":" + quote(error.getMessage())
                + ",\"position\":" + error.position() + "}";
    }
    private static String quote(String text) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"': result.append("\\\""); break;
                case '\\': result.append("\\\\"); break;
                default:
                    if (c < 32 || Character.isSurrogate(c)) result.append(String.format("\\u%04x", (int)c));
                    else result.append(c);
            }
        }
        return result.append('"').toString();
    }
}
