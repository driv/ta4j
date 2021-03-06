/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.analysis.criteria;

import eu.verdelhan.ta4j.AnalysisCriterion;
import eu.verdelhan.ta4j.Operation;
import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.TATestsUtils;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;


public class LinearTransactionCostCriterionTest {

    @Test
    public void calculateLinearCost() {
        MockTimeSeries series = new MockTimeSeries(100, 150, 200, 100, 50, 100);
        List<Trade> trades = new ArrayList<Trade>();
        AnalysisCriterion transactionCost = new LinearTransactionCostCriterion(1000, 0.005, 0.2);

        trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
        assertEquals(12.861, transactionCost.calculate(series, trades), TATestsUtils.TA_OFFSET);

        trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(3, OperationType.SELL)));
        assertEquals(24.3473, TATestsUtils.TA_OFFSET, transactionCost.calculate(series, trades));

        Trade t = new Trade();
        trades.add(t);
        assertEquals(24.3473, TATestsUtils.TA_OFFSET, transactionCost.calculate(series, trades));

        t.operate(5);
        assertEquals(28.2204, TATestsUtils.TA_OFFSET, transactionCost.calculate(series, trades));
    }

    @Test
    public void calculateFixedCost() {
        MockTimeSeries series = new MockTimeSeries(100, 105, 110, 100, 95, 105);
        List<Trade> trades = new ArrayList<Trade>();
        AnalysisCriterion transactionCost = new LinearTransactionCostCriterion(1000, 0, 1.3d);
        
        trades.add(new Trade(new Operation(0, OperationType.BUY), new Operation(1, OperationType.SELL)));
        assertEquals(2.6d, transactionCost.calculate(series, trades), TATestsUtils.TA_OFFSET);
        
        trades.add(new Trade(new Operation(2, OperationType.BUY), new Operation(3, OperationType.SELL)));
        assertEquals(5.2d, transactionCost.calculate(series, trades), TATestsUtils.TA_OFFSET);

        Trade t = new Trade();
        trades.add(t);
        assertEquals(5.2d, transactionCost.calculate(series, trades), TATestsUtils.TA_OFFSET);

        t.operate(0);
        assertEquals(6.5d, transactionCost.calculate(series, trades), TATestsUtils.TA_OFFSET);
    }

    @Test
    public void calculateFixedCostWithOneTrade() {
        MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
        Trade trade = new Trade();
        AnalysisCriterion transactionCost = new LinearTransactionCostCriterion(1000, 0, 0.75d);

        assertEquals(0, (int) transactionCost.calculate(series, trade));

        trade.operate(1);
        assertEquals(0.75d, transactionCost.calculate(series, trade), TATestsUtils.TA_OFFSET);

        trade.operate(3);
        assertEquals(1.5d, transactionCost.calculate(series, trade), TATestsUtils.TA_OFFSET);

        trade.operate(4);
        assertEquals(1.5d, transactionCost.calculate(series, trade), TATestsUtils.TA_OFFSET);
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = new LinearTransactionCostCriterion(1000, 0.5);
        assertTrue(criterion.betterThan(3.1, 4.2));
        assertFalse(criterion.betterThan(2.1, 1.9));
    }
}
