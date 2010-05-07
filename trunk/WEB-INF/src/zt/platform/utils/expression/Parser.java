package zt.platform.utils.expression;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private List stack = new ArrayList();
    private int bracketLevel;
    static Pattern doublePattern = Pattern.compile(
            "[+-]?((\\d*\\.\\d+)|(\\d+\\.?))([eE][+-]?\\d+)?");
    static Pattern spacePattern = Pattern.compile("\\s");

    public void parse(String s) throws Exception {
        String ss = s.trim();
        if (ss == null || ss.equals(""))
            return;
        stack.clear();
        bracketLevel = 0;
        int i = 0;
        while (true) {
            // ȥ���ײ��հף��س������С��ո񡢻س����С�TAB��
            Matcher matcher = spacePattern.matcher(s);
            for (; matcher.find(i) && matcher.start() == i;)
                i += matcher.end() - matcher.start();
            // ����Ϊ0���ַ�����ֹ����
            if (s.substring(i).equals("")) {
                if (bracketLevel != 0)
                    throw new ParserException("���ʽ����������Ų�δ���", 0, s.length());
                evaluate(0, 0);
                break;
            }
            // ����С����()
            char c = s.charAt(i);
            if (c == '(') {
                bracketLevel++;
                i++;
                continue;
            }
            if (c == ')') {
                bracketLevel--;
                if (bracketLevel < 0)
                    throw new ParserException("�����Ų�ƥ��", i, i + 1);
                i++;
                continue;
            }
            // �ж�Ԫ�����ǲ�����(Operator)��������(Data)
            boolean expectNumber = false;
            if (stack.isEmpty())
                expectNumber = true;
            else {
                int n = stack.size();
                Token token = (Token) stack.get(stack.size() - 1);
                if (Operator.class.isInstance(token.data))
                    expectNumber = true;
            }
            // ��������
            if (expectNumber) {
                matcher = doublePattern.matcher(s);
                if (!matcher.find(i) || matcher.start() != i)
                    throw new ParserException("Ӧ��������", i, i + 1);
                push(Double.parseDouble(matcher.group()), matcher.start(), matcher.end());
                i += matcher.end() - matcher.start();
                continue;
            }
            // ���������
            c = s.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/')
                push(c, i, i + 1);
            else
                throw new ParserException("��ʶ�� " + c + " �Ƿ�", i, i + 1);
            i++;
        }
    }

    public double getResult() throws Exception {
        int n = stack.size();
        if (n == 0)
            throw new Exception("�������ʽ�����⣡");
        if (n > 1)
            throw new Exception("��������������");
        Token token = (Token) stack.get(0);
        if (!Number.class.isInstance(token.data))
            throw new Exception("�����������ݲ���Number���ͣ�");
        Number number = (Number) token.data;
        return number.value;
    }

    private void evaluate(int bracketLevel, int priority) throws Exception {
        while (true) {
            int n = stack.size();
            if (n < 3)
                break;
            Token tokenLeft = (Token) stack.get(n - 3);
            Number left = (Number) tokenLeft.data;
            Token tokenOperator = (Token) stack.get(n - 2);
            Operator operator = (Operator) tokenOperator.data;
            Token tokenRight = (Token) stack.get(n - 1);
            Number right = (Number) tokenRight.data;

            if (operator.bracketLevel < bracketLevel)
                break;
            if (operator.bracketLevel == bracketLevel && operator.priority < priority)
                break;
            Expression expression = new Expression();
            expression.left = left;
            expression.operator = operator.data;
            expression.right = right;
            expression.calc();
            // �滻���µı��ʽ
            Token token = new Token();
            token.data = expression;
            token.start = tokenLeft.start;
            token.end = tokenRight.end;
            stack.remove(n - 1);
            stack.remove(n - 2);
            stack.remove(n - 3);
            stack.add(token);
        }
    }

    private void push(double d, int start, int end) throws Exception {
        // ���Ϸ���
        if (!stack.isEmpty()) {
            int n = stack.size();
            Token token = (Token) stack.get(n - 1);
            if (!Operator.class.isInstance(token.data))
                throw new ParserException("���� Number", start, end);
        }
        // ��ջ
        Number number = new Number();
        number.value = d;
        Token token = new Token();
        token.data = number;
        token.start = start;
        token.end = end;
        stack.add(token);
    }

    private void push(char c, int start, int end) throws Exception {
        // ���Ϸ���
        if (stack.isEmpty())
            throw new ParserException("��һ����ʶ�������ǲ�����", start, end);
        int n = stack.size();
        Token token = (Token) stack.get(n - 1);
        if (!Number.class.isInstance(token.data))
            throw new ParserException("���������", start, end);
        // ��ջ
        Operator operator = new Operator();
        operator.data = c;
        operator.bracketLevel = bracketLevel;
        if (c == '+' || c == '-')
            operator.priority = 1;
        else if (c == '*' || c == '/')
            operator.priority = 2;
        else
            throw new ParserException("������ " + c + " �Ƿ�", start, end);
        token = new Token();
        token.data = operator;
        token.start = start;
        token.end = end;
        evaluate(operator.bracketLevel, operator.priority);
        stack.add(token);
    }

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (String s; (s = in.readLine()) != null;) {
            try {
                parser.parse(s);
            } catch (ParserException ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }

}
