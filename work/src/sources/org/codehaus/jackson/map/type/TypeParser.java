package org.codehaus.jackson.map.type;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.codehaus.jackson.type.JavaType;

/* JADX INFO: loaded from: classes.dex */
public class TypeParser {
    final TypeFactory _factory;

    public TypeParser(TypeFactory f) {
        this._factory = f;
    }

    public JavaType parse(String canonical) throws Exception {
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        JavaType type = parseType(tokens);
        if (tokens.hasMoreTokens()) {
            throw _problem(tokens, "Unexpected tokens after complete type");
        }
        return type;
    }

    protected JavaType parseType(MyTokenizer tokens) throws Exception {
        if (!tokens.hasMoreTokens()) {
            throw _problem(tokens, "Unexpected end-of-string");
        }
        Class<?> base = findClass(tokens.nextToken(), tokens);
        if (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if ("<".equals(token)) {
                return this._factory._fromParameterizedClass(base, parseTypes(tokens));
            }
            tokens.pushBack(token);
        }
        return this._factory._fromClass(base, null);
    }

    protected List<JavaType> parseTypes(MyTokenizer tokens) throws IllegalArgumentException {
        ArrayList<JavaType> types = new ArrayList<>();
        while (tokens.hasMoreTokens()) {
            types.add(parseType(tokens));
            if (!tokens.hasMoreTokens()) {
                break;
            }
            String token = tokens.nextToken();
            if (">".equals(token)) {
                return types;
            }
            if (!",".equals(token)) {
                throw _problem(tokens, "Unexpected token '" + token + "', expected ',' or '>')");
            }
        }
        throw _problem(tokens, "Unexpected end-of-string");
    }

    protected Class<?> findClass(String className, MyTokenizer tokens) throws Exception {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            return Class.forName(className, true, loader);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            throw _problem(tokens, "Can not locate class '" + className + "', problem: " + e.getMessage());
        }
    }

    protected IllegalArgumentException _problem(MyTokenizer tokens, String msg) {
        return new IllegalArgumentException("Failed to parse type '" + tokens.getAllInput() + "' (remaining: '" + tokens.getRemainingInput() + "'): " + msg);
    }

    static final class MyTokenizer extends StringTokenizer {
        protected int _index;
        protected final String _input;
        protected String _pushbackToken;

        public MyTokenizer(String str) {
            super(str, "<,>", true);
            this._input = str;
        }

        @Override // java.util.StringTokenizer
        public boolean hasMoreTokens() {
            return this._pushbackToken != null || super.hasMoreTokens();
        }

        @Override // java.util.StringTokenizer
        public String nextToken() {
            String token;
            if (this._pushbackToken != null) {
                token = this._pushbackToken;
                this._pushbackToken = null;
            } else {
                token = super.nextToken();
            }
            this._index += token.length();
            return token;
        }

        public void pushBack(String token) {
            this._pushbackToken = token;
            this._index -= token.length();
        }

        public String getAllInput() {
            return this._input;
        }

        public String getUsedInput() {
            return this._input.substring(0, this._index);
        }

        public String getRemainingInput() {
            return this._input.substring(this._index);
        }
    }
}
