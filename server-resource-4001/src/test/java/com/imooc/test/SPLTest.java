package com.imooc.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@SpringBootTest
@Slf4j
public class SPLTest {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Test
    public void canCallFunction() {
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("industryId", 1);

        String expression1 = parser.parseExpression("#industryId").getValue(context, String.class);
        log.info("expression1 is:{}", expression1);

        String e2 = "T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industryId,1)";
        String expression2 = parser.parseExpression(e2).getValue(context, String.class);
        log.info("expression2 is:{}", expression2);


    }
}
